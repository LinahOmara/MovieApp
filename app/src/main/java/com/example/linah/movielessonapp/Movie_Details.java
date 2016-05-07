package com.example.linah.movielessonapp;

/**
 * Created by Linah on 5/2/2016.
 */
public class Movie_Details {

    private String Key ;
    private String Name ;
    private String Author ;
    private String Content ;
    private String ID;

    public Movie_Details(String Name, String Key, String Author, String Content) {
        this.Key     = Key;
        this.Name    = Name;
        this.Author  = Author;
        this.Content = Content;
    }

    public Movie_Details() {

    }

    public String getID () {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
