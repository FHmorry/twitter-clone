package com.example.twitterclone.repository;

import com.example.twitterclone.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds);
    Long countByUserId(Long userId);
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
}
