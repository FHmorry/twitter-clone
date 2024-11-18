package com.example.twitterclone.service;

import com.example.twitterclone.model.Post;
import com.example.twitterclone.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Post> getPostsByUserIdsWithUser(List<Long> userIds) {
        return postRepository.findByUserIdInOrderByCreatedAtDesc(userIds);
    }

    public int countPostsByUserId(Long userId) {
        return postRepository.countByUserId(userId).intValue();
    }
}
