package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.Customer;
import br.com.laercioskt.backend.data.Customer_;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Customer> find(String filterText, Pageable pageable, List<Sort.Order> sortOrders) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> from = query.from(Customer.class);
        query.where(restrictions(filterText, cb, from))
                .orderBy(getSortOrders(sortOrders, cb, from));

        return entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private List<Order> getSortOrders(List<Sort.Order> sortOrders, CriteriaBuilder cb, Root<Customer> from) {
        return sortOrders.stream()
                .map(s -> s.isAscending() ? cb.asc(from.get(s.getProperty())) : cb.desc(from.get(s.getProperty())))
                .collect(toList());
    }

    @Override
    public long count(String filterText) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Customer> from = query.from(Customer.class);
        query.select(cb.countDistinct(from)).where(restrictions(filterText, cb, from));

        return entityManager.createQuery(query).getSingleResult();
    }

    private Predicate restrictions(String filterText, CriteriaBuilder cb, Root<Customer> from) {
        if (isEmpty(filterText)) return cb.and();

        return cb.or(
                nameLikePredicate(filterText, cb, from),
                codeLikePredicate(filterText, cb, from)
        );
    }

    private Predicate nameLikePredicate(String filterText, CriteriaBuilder cb, Root<Customer> from) {
        return cb.like(cb.upper(from.get(Customer_.name)), "%" + filterText.toUpperCase() + "%");
    }

    private Predicate codeLikePredicate(String filterText, CriteriaBuilder cb, Root<Customer> from) {
        return cb.like(cb.upper(from.get(Customer_.code)), "%" + filterText.toUpperCase() + "%");
    }

}
