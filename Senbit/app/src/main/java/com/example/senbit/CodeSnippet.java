package com.example.senbit;

public class CodeSnippet {
    private String title;
    private int id;
    private String owner;
    private String description;
    private String programmingLanguage = "Not Specified";
    private String tag = "Not Specified";
    private String content = "";
    private int likedBy = 0;
    private int dislikedBy = 0;


    public CodeSnippet(String title, String owner, String description) {
        this.title = title;
        this.owner = owner;
        this.description = description;
    }
    public void setId(int id) {

        this.id = id;
    }
    public void setLikedBy(int likedBy) {
        this.likedBy = likedBy;
    }
    public void setDisLikedBy(int disLikedBy) {

        this.dislikedBy = disLikedBy;
    }
    public void setTitle(String title) {

        this.title = title;
    }
    public String getTag() {

        return tag;
    }
    public void setTag(String tag) {

        this.tag = tag;
    }
    public void setCodeOwner(String owner) {

        this.owner = owner;
    }
    public void setCodeDescription(String description) {

        this.description = description;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }
    public String getProgrammingLanguage() {
        return programmingLanguage;
    }
    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }
    public String getTitle() {
        return title;
    }
    public String getcodeOwner() {
        return owner;
    }
    public String getContent() {
        return content;
    }
    public String getcodeDescription() {
        return description;
    }
    public int getLikedBy() {
        return likedBy;
    }
    public int getDislikedBy() {
        return dislikedBy;
    }
}
