package com.example.twitterclone.service;

import com.example.twitterclone.model.UserFollow;
import com.example.twitterclone.repository.UserFollowRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFollowService {

    private final UserFollowRepository userFollowRepository;

    // コンストラクタ: UserFollowRepositoryを依存性注入
    public UserFollowService(UserFollowRepository userFollowRepository) {
        this.userFollowRepository = userFollowRepository;
    }

    /**
     * ユーザーをフォローするメソッド
     * @param followingUserId フォローするユーザーのID
     * @param followedUserId フォローされるユーザーのID
     */
    public void followUser(Long followingUserId, Long followedUserId) {
        // 既にフォローしているか確認
        if (existsByFollowingAndFollowed(followingUserId, followedUserId)) {
            throw new IllegalArgumentException("既にフォローしています。");
        }
        // フォロー関係を作成し、保存
        UserFollow userFollow = new UserFollow(followingUserId, followedUserId);
        userFollowRepository.save(userFollow);
    }

    /**
     * ユーザーのフォローを解除するメソッド
     * @param followingUserId フォローを解除するユーザーのID
     * @param followedUserId フォロー解除されるユーザーのID
     */
    public void unfollowUser(Long followingUserId, Long followedUserId) {
        // フォロー関係が存在するか確認
        if (!existsByFollowingAndFollowed(followingUserId, followedUserId)) {
            throw new IllegalArgumentException("フォロー関係が存在しません。");
        }
        // フォロー関係を削除
        userFollowRepository.deleteById_FollowingUserIdAndId_FollowedUserId(followingUserId, followedUserId);
    }

    /**
     * フォロー関係が存在するか確認するメソッド
     * @param followingUserId フォローするユーザーのID
     * @param followedUserId フォローされるユーザーのID
     * @return フォロー関係が存在する場合はtrue、存在しない場合はfalse
     */
    public boolean existsByFollowingAndFollowed(Long followingUserId, Long followedUserId) {
        return userFollowRepository.existsById_FollowingUserIdAndId_FollowedUserId(followingUserId, followedUserId);
    }

    /**
     * 現在のユーザーがフォローしているユーザーのリストを取得するメソッド
     * @param userId ユーザーのID
     * @return フォローしているユーザーのリスト
     */
    public List<UserFollow> getFollowing(Long userId) {
        return userFollowRepository.findById_FollowingUserId(userId);
    }

    /**
     * 現在のユーザーをフォローしているユーザーのリストを取得するメソッド
     * @param userId ユーザーのID
     * @return フォロワーのリスト
     */
    public List<UserFollow> getFollowers(Long userId) {
        return userFollowRepository.findById_FollowedUserId(userId);
    }

    /**
     * フォローしているユーザーのIDリストを取得するメソッド
     * @param userId ユーザーのID
     * @return フォローしているユーザーのIDリスト
     */
    public List<Long> getFollowingUserIds(Long userId) {
        return userFollowRepository.findById_FollowingUserId(userId)
            .stream()
            .map(UserFollow::getFollowedUserId)
            .collect(Collectors.toList());
    }

    /**
     * 相互フォロー状態を確認するメソッド
     * @param userId 現在のユーザーのID
     * @param targetUserId 確認するターゲットユーザーのID
     * @return 相互フォローしている場合はtrue、そうでない場合はfalse
     */
    public boolean isFollowingBack(Long userId, Long targetUserId) {
        return existsByFollowingAndFollowed(targetUserId, userId);
    }

    /**
     * 特定のユーザーが他のユーザーをフォローしているか確認するメソッド
     * @param followingUserId フォローしているユーザーのID
     * @param followedUserId フォローされているユーザーのID
     * @return フォローしている場合はtrue、そうでない場合はfalse
     */
    public boolean isFollowing(Long followingUserId, Long followedUserId) {
        return userFollowRepository.existsById_FollowingUserIdAndId_FollowedUserId(
            followingUserId, 
            followedUserId
        );
    }
}
