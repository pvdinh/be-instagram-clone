package com.example.demo.models.group;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Group {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private int privacy;
    private String description;
    private String imageCover;
    private long numberMembership;
    private long dateCreated;

    public Group() {
        super();
    }

    public Group(String name, int privacy, String description, String imageCover, long numberMembership, long dateCreated) {
        this.name = name;
        this.privacy = privacy;
        this.description = description;
        this.imageCover = imageCover;
        this.numberMembership = numberMembership;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public long getNumberMembership() {
        return numberMembership;
    }

    public void setNumberMembership(long numberMembership) {
        this.numberMembership = numberMembership;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }
}
