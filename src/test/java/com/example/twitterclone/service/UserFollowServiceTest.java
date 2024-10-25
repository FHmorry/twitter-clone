    // Start of Selection
    package com.example.twitterclone.service;
    
    import com.example.twitterclone.model.UserFollow;
    import com.example.twitterclone.repository.UserFollowRepository;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;
    
    import java.util.Arrays;
    import java.util.List;
    
    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;
    
    class UserFollowServiceTest {
    
        @InjectMocks
        private UserFollowService userFollowService; // UserFollowServiceのインスタンスを注入
    
        @Mock
        private UserFollowRepository userFollowRepository; // UserFollowRepositoryのモックを作成
    
        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this); // テストの前にモックを初期化
        }
    
        @Test
        void followUser() {
            Long followingUserId = 1L; // フォローするユーザーのID
            Long followedUserId = 2L; // フォローされるユーザーのID
    
            // フォロー関係が存在しないことをモックする
            when(userFollowRepository.existsById_FollowingUserIdAndId_FollowedUserId(followingUserId, followedUserId)).thenReturn(false);
    
            // ユーザーをフォローするメソッドを呼び出す
            userFollowService.followUser(followingUserId, followedUserId);
    
            // saveメソッドが1回呼ばれたことを確認
            verify(userFollowRepository, times(1)).save(any(UserFollow.class));
        }
    
        @Test
        void unfollowUser() {
            Long followingUserId = 1L; // フォローを解除するユーザーのID
            Long followedUserId = 2L; // フォロー解除されるユーザーのID
    
            // ユーザーのフォローを解除するメソッドを呼び出す
            userFollowService.unfollowUser(followingUserId, followedUserId);
    
            // deleteメソッドが1回呼ばれたことを確認
            verify(userFollowRepository, times(1)).deleteById_FollowingUserIdAndId_FollowedUserId(followingUserId, followedUserId);
        }
    
        @Test
        void getFollowing() {
            Long userId = 1L; // フォローしているユーザーのID
            List<UserFollow> expectedFollowing = Arrays.asList(new UserFollow(), new UserFollow()); // 期待されるフォローリスト
    
            // フォローしているユーザーのリストをモックする
            when(userFollowRepository.findById_FollowingUserId(userId)).thenReturn(expectedFollowing);
    
            // フォローしているユーザーのリストを取得するメソッドを呼び出す
            List<UserFollow> actualFollowing = userFollowService.getFollowing(userId);
    
            // 期待されるリストと実際のリストが一致することを確認
            assertEquals(expectedFollowing, actualFollowing);
        }
    
        @Test
        void getFollowers() {
            Long userId = 1L; // フォロワーを取得するユーザーのID
            List<UserFollow> expectedFollowers = Arrays.asList(new UserFollow(), new UserFollow()); // 期待されるフォロワーリスト
    
            // フォロワーのリストをモックする
            when(userFollowRepository.findById_FollowedUserId(userId)).thenReturn(expectedFollowers);
    
            // フォロワーのリストを取得するメソッドを呼び出す
            List<UserFollow> actualFollowers = userFollowService.getFollowers(userId);
    
            // 期待されるリストと実際のリストが一致することを確認
            assertEquals(expectedFollowers, actualFollowers);
        }
    
        @Test
        void isFollowing() {
            Long followingUserId = 1L; // フォローしているユーザーのID
            Long followedUserId = 2L; // フォローされているユーザーのID
    
            // フォロー関係が存在することをモックする
            when(userFollowRepository.existsById_FollowingUserIdAndId_FollowedUserId(followingUserId, followedUserId)).thenReturn(true);
    
            // フォローしているか確認するメソッドを呼び出す
            boolean isFollowing = userFollowService.isFollowing(followingUserId, followedUserId);
    
            // フォローしていることを確認
            assertTrue(isFollowing);
        }
    }
