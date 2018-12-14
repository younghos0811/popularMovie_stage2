package com.younghos0811.github.popular_movies.data;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.net.URL;
import java.security.Key;

public class Video {
    /* movie Img url */
    final private String UTUBE_BASE_IMG_URL = "https://img.youtube.com/vi/";
    final private String UTUBE_M_IMAGE_PATH = "mqdefault.jpg";
    final private String UTUBE_BASE_WATCH_URL = "https://www.youtube.com/watch/";


    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private String size;

    @SerializedName("type")
    private String type;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /** get Image URL **/
    public String getImageUrl() {
        Uri builtUri = Uri.parse(UTUBE_BASE_IMG_URL).buildUpon()
                .appendPath(key)
                .appendPath(UTUBE_M_IMAGE_PATH)
                .build();
        return builtUri.toString();
    }

    /** get Video URL **/
    public String getWatchUrl() {
        Uri builtUri = Uri.parse(UTUBE_BASE_WATCH_URL).buildUpon()
                .appendQueryParameter("v" , key)
                .build();
        return builtUri.toString();
    }
}
