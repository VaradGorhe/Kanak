package com.example.eyickanak.model;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : Leaderboard.java
        Global Variables:None
        Functions   :
        */


//class Leaderboard is created which has name and points as variables.

public class Leaderboard {
    String name, points;

    public Leaderboard() {
    }

    public Leaderboard(String points, String name) {
        this.points = points;
        this.name = name;
    }

    //The following functions return the values of points and name of the particular user.
    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
