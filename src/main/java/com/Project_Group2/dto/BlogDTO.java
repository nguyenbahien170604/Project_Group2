package com.Project_Group2.dto;

import java.util.Date;

public class BlogDTO {
    private int id;
    private String blogTitle;
    private String blogUserName;
    private String blogShortDescription;
    private String blogContent;
    private String blogImage;
    private Date createdAt;

    public BlogDTO() {
    }

    public BlogDTO(int id, String blogTitle, String blogUserName, String blogShortDescription, String blogContent, String blogImage, Date createdAt) {
        this.id = id;
        this.blogTitle = blogTitle;
        this.blogUserName = blogUserName;
        this.blogShortDescription = blogShortDescription;
        this.blogContent = blogContent;
        this.blogImage = blogImage;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogUserName() {
        return blogUserName;
    }

    public void setBlogUserName(String blogUserName) {
        this.blogUserName = blogUserName;
    }

    public String getBlogShortDescription() {
        return blogShortDescription;
    }

    public void setBlogShortDescription(String blogShortDescription) {
        this.blogShortDescription = blogShortDescription;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String getBlogImage() {
        return blogImage;
    }

    public void setBlogImage(String blogImage) {
        this.blogImage = blogImage;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "BlogDTO{" +
                "blogTitle='" + blogTitle + '\'' +
                ", blogUserName='" + blogUserName + '\'' +
                ", blogShortDescription='" + blogShortDescription + '\'' +
                ", blogContent='" + blogContent + '\'' +
                ", blogImage='" + blogImage + '\'' +
                '}';
    }
}
