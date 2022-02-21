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
    private int followers;
    private int following;
    private int posts;
    private String profilePhoto;
    @Indexed(unique = true)
    private String username;
    private String website;
    private long dateCreated;

    public UserAccountSetting() {
        super();
    }

    public UserAccountSetting(String id, String displayName, String description, int followers, int following, int posts, String profilePhoto, String username, String website, long dateCreated) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
        this.profilePhoto = profilePhoto;
        this.username = username.replace(" ", "_");
        this.website = website;
        this.dateCreated = dateCreated;
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

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
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

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
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
