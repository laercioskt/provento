package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.Customer_;
import br.com.laercioskt.backend.data.Protocol;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

import static br.com.laercioskt.backend.data.Customer_.name;
import static br.com.laercioskt.backend.data.Protocol_.*;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@SuppressWarnings("unused")
public class ProtocolRepositoryCustomImpl implements ProtocolRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder cb() {
        return entityManager.getCriteriaBuilder();
    }

    @Override
    public List<Protocol> find(String filterText, Pageable pageable, List<Sort.Order> sortOrders) {
        CriteriaQuery<Protocol> query = cb().createQuery(Protocol.class);
        Root<Protocol> from = query.from(Protocol.class);
        query.where(restrictions(filterText, from)).orderBy(getSortOrders(sortOrders, from));

        return entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private List<Order> getSortOrders(List<Sort.Order> sortOrders, Root<Protocol> from) {
        return sortOrders.stream()
                .map(s -> s.isAscending() ? cb().asc(from.get(s.getProperty())) : cb().desc(from.get(s.getProperty())))
                .collect(toList());
    }

    @Override
    public long count(String filterText) {
        CriteriaQuery<Long> query = cb().createQuery(Long.class);
        Root<Protocol> from = query.from(Protocol.class);
        query.select(cb().countDistinct(from)).where(restrictions(filterText, from));

        return entityManager.createQuery(query).getSingleResult();
    }

    private Predicate restrictions(String filterText, Root<Protocol> from) {
        if (isEmpty(filterText)) return cb().and();

        return cb().or(
                noteLikePredicate(filterText, from),
                customerLikePredicate(filterText, from),
                codeLikePredicate(filterText, from)
        );
    }

    private Predicate noteLikePredicate(String filterText, Root<Protocol> from) {
        return cb().like(cb().upper(from.get(note)), "%" + filterText.toUpperCase() + "%");
    }

    private Predicate customerLikePredicate(String filterText, Root<Protocol> from) {
        return cb().or(
                cb().like(cb().upper(from.get(customer).get(name)), "%" + filterText.toUpperCase() + "%"),
                cb().like(cb().upper(from.get(customer).get(Customer_.code)), "%" + filterText.toUpperCase() + "%"));
    }

    private Predicate codeLikePredicate(String filterText, Root<Protocol> from) {
        return cb().like(cb().upper(from.get(code)), "%" + filterText.toUpperCase() + "%");
    }

}
