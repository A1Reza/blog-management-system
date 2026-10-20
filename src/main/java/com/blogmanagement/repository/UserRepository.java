package com.blogmanagement.repository;

import com.blogmanagement.config.HibernateUtil;
import com.blogmanagement.entity.User;

import java.util.Optional;

public class UserRepository {

    public User save(User user) {
        return HibernateUtil.executeInTransaction(em -> {
            em.persist(user);
            return user;
        });
    }

    public Optional<User> findById(Long id) {
        return HibernateUtil.read(em ->
                Optional.ofNullable(em.find(User.class, id))
        );
    }

    public Optional<User> findByUsername(String username) {
        return HibernateUtil.read(em ->
                em.createQuery(
                                "SELECT u FROM User u WHERE u.username = :username",
                                User.class
                        )
                        .setParameter("username", username)
                        .getResultStream()
                        .findFirst()
        );
    }

    public Optional<User> findByEmail(String email) {
        return HibernateUtil.read(em ->
                em.createQuery(
                                "SELECT u FROM User u WHERE u.email = :email",
                                User.class
                        )
                        .setParameter("email", email)
                        .getResultStream()
                        .findFirst()
        );
    }
}
