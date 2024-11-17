package com.example.twitterclone.controller;

import com.example.twitterclone.dto.UserFollowResponseDTO;
import com.example.twitterclone.model.User;
import com.example.twitterclone.model.UserFollow;
import com.example.twitterclone.service.UserFollowService;
import com.example.twitterclone.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserFollowControllerTest {

    @InjectMocks
    private UserFollowController userFollowController;

    @Mock
    private UserFollowService userFollowService;

    @Mock
    private UserService userService;

    public UserFollowControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFollowing_shouldReturnListOfUserFollowResponseDTO() {
        Authentication authentication = mock(Authentication.class);
        User currentUser = new User();
        currentUser.setId(1L);
        when(userService.getCurrentUser(authentication)).thenReturn(currentUser);

        UserFollow follow1 = new UserFollow(1L, 2L);
        UserFollow follow2 = new UserFollow(1L, 3L);
        List<UserFollow> following = Arrays.asList(follow1, follow2);
        when(userFollowService.getFollowing(currentUser.getId())).thenReturn(following);

        User followedUser1 = new User();
        followedUser1.setId(2L);
        followedUser1.setUsername("user2");
        User followedUser2 = new User();
        followedUser2.setId(3L);
        followedUser2.setUsername("user3");
        when(userService.findById(2L)).thenReturn(followedUser1);
        when(userService.findById(3L)).thenReturn(followedUser2);

        when(userFollowService.isFollowingBack(1L, 2L)).thenReturn(true);
        when(userFollowService.isFollowingBack(1L, 3L)).thenReturn(false);
        when(userService.getPostCount(2L)).thenReturn(10);
        when(userService.getPostCount(3L)).thenReturn(5);

        ResponseEntity<List<UserFollowResponseDTO>> response = userFollowController.getFollowing(authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

        UserFollowResponseDTO dto1 = response.getBody().get(0);
        assertEquals(2L, dto1.getUser().getId());
        assertEquals("user2", dto1.getUser().getUsername());
        assertTrue(dto1.isFollowingBack());
        assertEquals(10, dto1.getPostCount());

        UserFollowResponseDTO dto2 = response.getBody().get(1);
        assertEquals(3L, dto2.getUser().getId());
        assertEquals("user3", dto2.getUser().getUsername());
        assertFalse(dto2.isFollowingBack());
        assertEquals(5, dto2.getPostCount());
    }

    // 他のテストケースも同様に修正
} 