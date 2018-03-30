package akankshab.me.popularmovies.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import akankshab.me.popularmovies.Movie;

/**
 * Created by akanksha on 28/2/18.
 * using Gson tutorial -> https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
 */

public class MovieResponse {
    @SerializedName("results")
    List<Movie> movies;

    // public constructor
    public MovieResponse() {
        movies = new ArrayList<Movie>();
    }

    public static List<Movie> parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        MovieResponse movieResponse = gson.fromJson(response, MovieResponse.class);
        return movieResponse.movies;
    }
}
