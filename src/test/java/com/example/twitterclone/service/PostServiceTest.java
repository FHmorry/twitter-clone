package com.example.twitterclone.service;

import com.example.twitterclone.model.Post;
import com.example.twitterclone.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    private Post post;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        post = new Post();
        post.setId(1L);
        post.setUserId(1L);
        post.setContents("テスト投稿");
        // post.setCreatedAt(LocalDateTime.now());
        // post.setUpdatedAt(LocalDateTime.now());

    }

    @Test
    public void testCreatePost() {
        when(postRepository.save(post)).thenReturn(post);
        Post createdPost = postService.createPost(post);
        assertEquals(post.getContents(), createdPost.getContents());
    }

    @Test
    public void testGetPostById() {
        when(postRepository.findById(1L)).thenReturn(java.util.Optional.of(post));
        Post foundPost = postService.getPostById(1L);
        assertEquals(post.getId(), foundPost.getId());
    }
}
