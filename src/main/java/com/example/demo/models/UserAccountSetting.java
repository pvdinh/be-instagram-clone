package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserAccountSetting {
    @Id
    private String id;
    @Indexed(unique = true)
    private String displayName;
    private String description;
    private String followers;
    private String following;
    private String posts;
    private String profilePhoto;
    @Indexed(unique = true)
    private String username;
    private String website;

    public UserAccountSetting() {
        super();
    }

    public UserAccountSetting(String id, String displayName, String description, String followers, String following, String posts, String profilePhoto, String username, String website) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
        this.profilePhoto = profilePhoto;
        this.username = username.replace(" ", "_");
        this.website = website;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getUsername() {
        return username.replace(" ", "_");
    }

    public void setUsername(String username) {
        this.username = username.replace(" ", "_");
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    //để thực hiện các thao tác trên List cần so sánh như removeAll
    //VD: trong FollowingService.suggestionsToFollow
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(!(obj instanceof UserAccountSetting)){
            return false;
        }
        UserAccountSetting userAccountSetting = (UserAccountSetting) obj;
        return this.id.equalsIgnoreCase(userAccountSetting.getId());
    }
}
