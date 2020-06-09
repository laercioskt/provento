package br.com.laercioskt.backend.repository;

import br.com.laercioskt.backend.data.User;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findByFilter(String filterText, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        Predicate userName = getUserNamePredicate(filterText, cb, user);
        query.select(user).where(userName);
        return entityManager.createQuery(query).getResultList();
    }

    private Predicate getUserNamePredicate(String filterText, CriteriaBuilder cb, Root<User> user) {
        if (isNullOrEmpty(filterText)){
            return cb.isTrue(cb.literal(true));
        } else {
            return cb.like(user.get("userName"), filterText);
        }
    }

    @Override
    public long count(String filterText) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> user = query.from(User.class);
        query.select(cb.count(user)).where(getUserNamePredicate(filterText, cb, user));
        return entityManager.createQuery(query).getSingleResult();
    }

}
