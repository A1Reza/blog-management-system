
package com.blogmanagement.repository;

import com.blogmanagement.command.PostToPostResponseDto;
import com.blogmanagement.config.HibernateUtil;
import com.blogmanagement.dto.response.PostResponseDto;
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
        return HibernateUtil.read(entityManager ->
                entityManager.createQuery(
                                "SELECT p FROM Post p " +
                                        "JOIN FETCH p.author " +
                                        "WHERE p.id = :id",
                                Post.class
                        )
                        .setParameter("id", id)
                        .getResultStream()
                        .findFirst()
        );
    }

    public List<Post> findAll() {
        return HibernateUtil.read(entityManager ->
                entityManager.createQuery(
                        "SELECT p FROM Post p " +
                                "JOIN FETCH p.author " +
                                "ORDER BY p.createdAt DESC",
                        Post.class
                ).getResultList()
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

    public Post updateAndFetchAuthor(Post post) {
        return HibernateUtil.executeInTransaction(em -> {
            Post updatedPost = em.merge(post);

            return em.createQuery(
                            "SELECT p FROM Post p " +
                                    "JOIN FETCH p.author " +
                                    "WHERE p.id = :id",
                            Post.class
                    )
                    .setParameter("id", updatedPost.getId())
                    .getSingleResult();
        });
    }

    public PostResponseDto updateAndConvert(Post post) {
        return HibernateUtil.executeInTransaction(em -> {
            Post updatedPost = em.merge(post);

            Post fetchedPost = em.createQuery(
                            "SELECT p FROM Post p " +
                                    "JOIN FETCH p.author " +
                                    "WHERE p.id = :id",
                            Post.class
                    )
                    .setParameter("id", updatedPost.getId())
                    .getSingleResult();

            return PostToPostResponseDto.convert(fetchedPost);
        });
    }
}
