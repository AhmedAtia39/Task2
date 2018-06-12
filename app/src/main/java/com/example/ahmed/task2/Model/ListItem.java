package com.example.ahmed.task2.Model;

/**
 * Created by AHMED on 11/06/2018.
 */

public class ListItem {
    public String repoName;
    public String description;
    public String userName;
    public boolean fork;

    public ListItem(String repoName, String description, String userName, boolean fork) {
        this.repoName = repoName;
        this.description = description;
        this.userName = userName;
        this.fork = fork;
    }
}
