package akankshab.me.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akanksha on 3/11/17.
 */

public class Movie implements Parcelable {

    @SerializedName("title")
    private String title;

    @SerializedName("vote_average")
    private double rating;

    //@SerializedName("poster_path")
    private int thumbnail;

    @SerializedName("overview")
    private String synopsis;

    @SerializedName("release_date")
    private String  releaseDate;

    @SerializedName("poster_path")
    private String imagePath;
// http://image.tmdb.org/t/p/w185/
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185";
    private static final String  POSTER_SIZE = "w342";

    public Movie() {
    }
    public Movie(String path) {
        this.imagePath = path;
    }

    public Movie(String name, double rating, int thumbnail, String synopsis, String releaseDate, String path) {
        this.title = name;
        this.rating = rating;
        this.thumbnail = thumbnail;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.imagePath = path;
    }
    private Movie(Parcel source){
        title = source.readString();
        rating = source.readDouble();
        synopsis = source.readString();
        releaseDate = source.readString();
        imagePath = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(imagePath);
        dest.writeString(synopsis);
        dest.writeDouble(rating);
        dest.writeString(releaseDate);

    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel movie) {
            return new Movie(movie);
        }
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String date) {
        this.releaseDate = date;
    }

    public void setSynopsis (String synopsis) {
        this.synopsis = synopsis;
    }
    public String getSynopsis() {
        return synopsis;
    }
    public String getPosterPath(){
        String fullPath = "";
        if (!imagePath.isEmpty()){
            fullPath += BASE_URL + POSTER_SIZE + imagePath;
            return fullPath;
        }
        return null ;                   // return default value
    }

    public String getPath() {
        String fullPath = "";
        if (!imagePath.isEmpty()){
            fullPath += BASE_URL + IMAGE_SIZE + imagePath;
            return fullPath;
        }
        return null ;                   // return default value

    }

}