package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.data.UserStatus;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findWithCategories(String filterText, Pageable pageable) {
        String where = buildWhere(filterText);

        String select = "SELECT distinct u FROM User u JOIN FETCH u.category c where " + where;
        List<UserStatus> statuses = getUserStatuses(filterText);

        return entityManager.createQuery(select, User.class)
                .setParameter("filterText", "%" + filterText + "%")
                .setParameter("status", statuses.isEmpty() ? "1" : statuses)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public long count(String filterText) {
        String where = buildWhere(filterText);
        String query = "SELECT count(distinct u) FROM User u, IN (u.category) c where " + where;
        List<UserStatus> statuses = getUserStatuses(filterText);
        return entityManager.createQuery(query, Long.class)
                .setParameter("filterText", "%" + filterText + "%")
                .setParameter("status", statuses.isEmpty() ? "1" : statuses)
                .getSingleResult();
    }

    private List<UserStatus> getUserStatuses(String filterText) {
        return stream(UserStatus.values())
                .filter(s -> s.toString().contains(filterText))
                .collect(toList());
    }

    private String buildWhere(String filterText) {
        String statusCondition = getUserStatuses(filterText).isEmpty() ? " OR :status != :status " : " OR u.status in :status";

        return (filterText.isEmpty() ? " 1 = 1 " : " 1 != 1 ")
                + " OR u.userName like :filterText"
                + " OR c.name like :filterText"
                + statusCondition;
    }

}
