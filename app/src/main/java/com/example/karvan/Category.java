package com.example.karvan;

import java.util.ArrayList;

public class Category {

    public static final ArrayList<Category> nodes = new ArrayList<>();

    ArrayList<String> services;
    String name;
    boolean[] idk;
    ArrayList<String> selected;
    String searchText;
    int num;

    public Category(String name) {
        num = 0;
        this.name = name;
        services = new ArrayList<>();
        selected = new ArrayList<>();
        idk = new boolean[64];
    }

    public String getSearchText(){
        searchText = "";
        for(String service:services){
            searchText += service;
        }

        return searchText;
    }

    @Override
    public String toString() {
        return name;
    }
}