package tnguyen.hcmute.myspotifyapp;

import java.io.Serializable;

public class Song implements Serializable {
    private int id;
    private String title;
    private String single;
    private int image;
    private int resource;
    private String duration;

    public Song() {}

    public Song(String title, String single, int image, int resource) {
        this.title = title;
        this.single = single;
        this.image = image;
        this.resource = resource;
    }

    public Song(int id, String title, String single, int resource, String duration, int image) {
        this.id = id;
        this.title = title;
        this.single = single;
        this.image = image;
        this.resource = resource;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}

