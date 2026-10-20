
package com.blogmanagement.repository;

import com.blogmanagement.config.HibernateUtil;
import com.blogmanagement.entity.Post;

import java.util.List;
import java.util.Optional;

public class PostRepository {

    public Post save(Post post) {
        return HibernateUtil.executeInTransaction(em -> {
            em.persist(post);
            return post;
        });
    }

    public Optional<Post> findById(Long id) {
        return HibernateUtil.read(em ->
                Optional.ofNullable(em.find(Post.class, id))
        );
    }

    public List<Post> findAll() {
        return HibernateUtil.read(em ->
                em.createQuery(
                                "SELECT p FROM Post p ORDER BY p.createdAt DESC",
                                Post.class
                        )
                        .getResultList()
        );
    }

    public Post update(Post post) {
        return HibernateUtil.executeInTransaction(em ->
                em.merge(post)
        );
    }

    public boolean deleteById(Long id) {
        return HibernateUtil.executeInTransaction(em -> {
            Post post = em.find(Post.class, id);

            if (post == null) {
                return false;
            }

            em.remove(post);
            return true;
        });
    }
}
