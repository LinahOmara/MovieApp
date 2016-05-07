package com.example.linah.movielessonapp;

public class Movie {
    private String title;
    private String ID;
    private String Overview;
    private String Avg_Vote;
    private String Release_date;
    private String Img_Path;


    public Movie() {
    }

    public Movie(String ID , String title,String Overview , String Avg_Vote, String Release_Date , String Img_Path) {
        this.title = title;
        this.ID = ID;
        this.Overview = Overview;
        this.Avg_Vote = Avg_Vote;
        this.Release_date = Release_Date;
        this.Img_Path = Img_Path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getAvg_Vote() {
        return Avg_Vote;
    }

    public void setAvg_Vote(String avg_Vote) {
        Avg_Vote = avg_Vote;
    }

    public String getRelease_date() {
        return Release_date;
    }

    public void setRelease_date(String release_date) {
        Release_date = release_date;
    }

    public String getImg_Path() {
        return Img_Path;
    }

    public void setImg_Path(String img_Path) {
        Img_Path = img_Path;
    }



}