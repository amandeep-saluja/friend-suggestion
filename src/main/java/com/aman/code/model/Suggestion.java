package com.aman.code.model;

public class Suggestion {

    private String id;

    private int score;

    private String name;

    public Suggestion(String id, int score, String name) {
        this.id = id;
        this.score = score;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
