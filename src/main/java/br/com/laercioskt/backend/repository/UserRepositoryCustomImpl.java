package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.*;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static javax.persistence.criteria.JoinType.LEFT;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findWithCategories(String filterText, Pageable pageable, List<UserOrder> sortOrders) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> from = query.from(User.class);
        SetJoin<User, Category> join = from.join(User_.category, LEFT);
        from.fetch(User_.category, LEFT);
        query.where(restrictions(filterText, cb, from, join))
                .orderBy(getSortOrders(sortOrders, cb, from));

        return entityManager.createQuery(query)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private List<Order> getSortOrders(List<UserOrder> sortOrders, CriteriaBuilder cb, Root<User> from) {
        return sortOrders.stream()
                .map(s -> s.isAscending() ? cb.asc(from.get(s.getSorted())) : cb.desc(from.get(s.getSorted())))
                .collect(toList());
    }

    @Override
    public long count(String filterText) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> from = query.from(User.class);
        SetJoin<User, Category> join = from.join(User_.category, LEFT);
        query.select(cb.countDistinct(from)).where(restrictions(filterText, cb, from, join));

        return entityManager.createQuery(query).getSingleResult();
    }

    private Predicate[] restrictions(String filterText, CriteriaBuilder cb, Root<User> from, SetJoin<User, Category> join) {
        List<Predicate> restrictions = new ArrayList<>();

        if (isNotEmpty(filterText)) {
            restrictions.add(cb.or(
                    cb.like(cb.upper(from.get(User_.userName)), "%" + filterText.toUpperCase() + "%"),
                    cb.like(cb.upper(join.get(Category_.name)), "%" + filterText.toUpperCase() + "%"),
                    getStatusClause(filterText, cb, from)
            ));
        }

        return restrictions.toArray(new Predicate[]{});
    }

    private Predicate getStatusClause(String filterText, CriteriaBuilder cb, Root<User> from) {
        List<UserStatus> statuses = statuses(filterText);
        return statuses.isEmpty() ? cb.or() : from.get(User_.status).in(statuses);
    }

    private List<UserStatus> statuses(String filterText) {
        return stream(UserStatus.values())
                .filter(s -> s.toString().toUpperCase().contains(filterText.toUpperCase())).collect(toList());
    }

}
