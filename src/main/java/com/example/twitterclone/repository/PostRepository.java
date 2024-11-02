package com.example.twitterclone.repository;

import com.example.twitterclone.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 特定のユーザーIDに関連する投稿を取得するメソッド
    List<Post> findByUserId(Long userId);

    // 内容に特定の文字列を含む投稿を検索するメソッド
    List<Post> findByContentsContaining(String keyword);

    // 最新の投稿から指定した数だけ取得するメソッド
    List<Post> findTop10ByOrderByCreatedAtDesc();

    // ユーザーIDで投稿の新着順で取得するメソッド
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Fetch Joinを使用してユーザー情報と一緒に投稿を取得するメソッド（投稿日時の降順）
    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
    List<Post> findByUserIdInWithUser(List<Long> userIds);
}
