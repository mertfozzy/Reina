package com.example.reina.model;

public class contacts {

    private String mName;
    private String mAbout;

    public contacts() {}

    public contacts(String about, String name) {
        mAbout = about;
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public String getAbout() {
        return mAbout;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAbout(String about) {
        mAbout = about;
    }


}
