package com.example.twitterclone.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserFollowId implements Serializable {

    @Column(name = "following_user_id")
    private Long followingUserId;

    @Column(name = "followed_user_id")
    private Long followedUserId;

    public UserFollowId() {}

    public UserFollowId(Long followingUserId, Long followedUserId) {
        this.followingUserId = followingUserId;
        this.followedUserId = followedUserId;
    }

    // getterとsetterメソッド

    public Long getFollowingUserId() {
        return followingUserId;
    }

    public Long getFollowedUserId() {
        return followedUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFollowId that = (UserFollowId) o;
        return Objects.equals(followingUserId, that.followingUserId) &&
               Objects.equals(followedUserId, that.followedUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followingUserId, followedUserId);
    }
}
