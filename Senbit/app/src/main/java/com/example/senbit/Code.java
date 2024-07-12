package com.example.senbit;

import java.io.Serializable;

public class Code implements Serializable {
    private String title;
    private String Owner;
    private String description;
    private String Language;
    private String CodeContent;

    public Code(String title,String Owner, String description,String Language, String CodeContent) {
        this.title = title;
        this.Owner=Owner;
        this.description = description;
        this.Language=Language;
        this.CodeContent = CodeContent;
    }

    public String getTitle() {
        return title;
    }
    public String getOwner() {
        return Owner;
    }

    public String getDescription() {
        return description;
    }
    public String getLanguage() {
        return Language;
    }

    public String getCodeContent() {
        return CodeContent;
    }

}
