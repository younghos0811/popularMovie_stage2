package com.younghos0811.github.popular_movies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    /* movie Img url */
    @Ignore
    final String MOVIE_BASE_IMG_PATH = "https://image.tmdb.org/t/p/";
    @Ignore
    final String IMG_SIZE = "w185";

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String path;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String overView;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    private String rate;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDay;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @Ignore
    public Movie newMovie() {
        return new Movie(this.id ,this.title, this.path, this.overView, this.rate, this.releaseDay , new Date());
    }

    public Movie(String id, String title, String path, String overView, String rate, String releaseDay, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.overView = overView;
        this.rate = rate;
        this.releaseDay = releaseDay;
        this.updatedAt = updatedAt;
    }

    //    public Movie(int id, String description, int priority, Date updatedAt) {
//        this.id = id;
//        this.description = description;
//        this.priority = priority;
//        this.updatedAt = updatedAt;
//    }

    @Ignore
    protected Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        path = in.readString();
        overView = in.readString();
        rate = in.readString();
        releaseDay = in.readString();
    }
    @Ignore
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(path);
        dest.writeString(overView);
        dest.writeString(rate);
        dest.writeString(releaseDay);
    }


    /** get & set **/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public String getFullPath() {
        return Uri.parse(MOVIE_BASE_IMG_PATH).buildUpon()
                .appendPath(IMG_SIZE)
                .appendEncodedPath(path)
                .build().toString();
    }

    public void setPath(String path) { this.path = path; }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(String releaseDay) {
        this.releaseDay = releaseDay;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
