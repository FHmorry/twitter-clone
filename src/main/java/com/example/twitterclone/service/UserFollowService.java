    // Start of Selection
    package com.example.twitterclone.service;
    
    import com.example.twitterclone.model.UserFollow;
    import com.example.twitterclone.repository.UserFollowRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import com.example.twitterclone.exception.UserFollowException;
    
    import java.util.List;
    import java.util.stream.Collectors;
    
    @Service
    public class UserFollowService {
    
        private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserFollowService.class);
    
        @Autowired
        private UserFollowRepository userFollowRepository;
    
        // ユーザーをフォローするメソッド
        // フォローしていない場合に新しいフォロー関係を作成し、保存する
        @Transactional
        public void followUser(Long followerId, Long followeeId) {
            try {
                if (isFollowing(followerId, followeeId)) {
                    throw new UserFollowException(
                        String.format("フォロー失敗: ユーザーID %d は既にユーザーID %d をフォローしています", followerId, followeeId),
                        "ALREADY_FOLLOWING"
                    );
                }
                userFollowRepository.save(new UserFollow(followerId, followeeId));
                logger.info("フォロー成功: ユーザーID {} がユーザーID {} をフォローしました", followerId, followeeId);
            } catch (UserFollowException e) {
                logger.info(e.getMessage());
                throw e;
            } catch (Exception e) {
                String errorMessage = String.format("予期せぬエラーが発生しました: %s", e.getMessage());
                logger.error(errorMessage);
                throw new UserFollowException(errorMessage, "SYSTEM_ERROR");
            }
        }
    
        // ユーザーのフォローを解除するメソッド
        // 指定されたユーザーのフォロー関係を削除する
        @Transactional
        public void unfollowUser(Long followingUserId, Long followedUserId) {
            if (!isFollowing(followingUserId, followedUserId)) {
                logger.info("アンフォロー失敗: ユーザーID {} はユーザーID {} をフォローしていません", followingUserId, followedUserId);
                return;
            }
            userFollowRepository.deleteById_FollowingUserIdAndId_FollowedUserId(followingUserId, followedUserId);
            logger.info("アンフォロー成功: ユーザーID {} がユーザーID {} のフォローを解除しました", followingUserId, followedUserId);
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
    
        // フォローしているユーザーのIDリストを取得するメソッド
        public List<Long> getFollowingUserIds(Long userId) {
            return userFollowRepository.findById_FollowingUserId(userId)
                .stream()
                .map(UserFollow::getFollowedUserId)
                .collect(Collectors.toList());
        }
    }
