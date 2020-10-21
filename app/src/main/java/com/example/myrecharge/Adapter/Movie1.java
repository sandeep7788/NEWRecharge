package com.example.myrecharge.Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class Movie1 {

    private String title;

    public Movie1() {
    }

    public Movie1(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Creating 10 dummy content for list.
     *
     * @param itemCount
     * @return
     */
    public static List<Movie1> createMovies(int itemCount) {
        List<Movie1> Movie1s = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Movie1 Movie1 = new Movie1("Movie1 " + (itemCount == 0 ?
                    (itemCount + 1 + i) : (itemCount + i)));
            Movie1s.add(Movie1);
        }
        return Movie1s;
    }
}