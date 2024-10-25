    // Start of Selection
    package com.example.twitterclone.service;
    
    import com.example.twitterclone.model.UserFollow;
    import com.example.twitterclone.repository.UserFollowRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    
    import java.util.List;
    
    @Service
    public class UserFollowService {
    
        @Autowired
        private UserFollowRepository userFollowRepository;
    
        // ユーザーをフォローするメソッド
        // フォローしていない場合に新しいフォロー関係を作成し、保存する
        @Transactional
        public void followUser(Long followerId, Long followeeId) {
            // フォロー済みのユーザーを確認
            if (isFollowing(followerId, followeeId)) {
                throw new IllegalArgumentException("このユーザーはすでにフォローしています。");
            }
            UserFollow userFollow = new UserFollow(followerId, followeeId);
            userFollowRepository.save(userFollow);
        }
    
        // ユーザーのフォローを解除するメソッド
        // 指定されたユーザーのフォロー関係を削除する
        @Transactional
        public void unfollowUser(Long followingUserId, Long followedUserId) {
            userFollowRepository.deleteById_FollowingUserIdAndId_FollowedUserId(followingUserId, followedUserId);
        }
    
        // フォローしているユーザーのリストを取得するメソッド
        // 指定されたユーザーがフォローしているユーザーを返す
        public List<UserFollow> getFollowing(Long userId) {
            return userFollowRepository.findById_FollowingUserId(userId);
        }
    
        // フォロワーのリストを取得するメソッド
        // 指定されたユーザーをフォローしているユーザーを返す
        public List<UserFollow> getFollowers(Long userId) {
            return userFollowRepository.findById_FollowedUserId(userId);
        }
    
        // 特定のユーザーをフォローしているか確認するメソッド
        // フォロー関係が存在するかどうかを返す
        public boolean isFollowing(Long followingUserId, Long followedUserId) {
            return userFollowRepository.existsById_FollowingUserIdAndId_FollowedUserId(followingUserId, followedUserId);
        }
    }
