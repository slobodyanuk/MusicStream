
package com.musicstream.rest.model;

import android.text.TextUtils;


import com.musicstream.utils.Constants;

import org.parceler.Parcel;

@Parcel(Parcel.Serialization.BEAN)
public class Track {

    private String artwork_url;
    private String full_artwork_url;
    private String created_at;
    private String description;
    private int duration;
    private int full_duration;
    private String genre;
    private int id;
    private String kind;
    private String permalink;
    private String permalink_url;
    private boolean _public;
    private String purchase_title;
    private String purchase_url;
    private String state;
    private boolean streamable;
    private String tag_list;
    private String title;
    private String uri;
    private String streamUri;
    private String policy;

    /**
     * @return The artwork_url
     */
    public String getArtworkUrl() {
        return artwork_url;
    }

    /**
     * @param artwork_url The artwork_url
     */
    public void setArtworkUrl(String artwork_url) {
        this.artwork_url = artwork_url;
    }

    public  String getFullArtworkUrl(){
        if (!TextUtils.isEmpty(artwork_url)) {
            return full_artwork_url = artwork_url.replace("large", "t500x500");
        }else{
            return "null";
        }
    }

    /**
     * @return The created_at
     */
    public String getCreatedAt() {
        return created_at;
    }

    /**
     * @param created_at The created_at
     */
    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration The duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return The full_duration
     */
    public int getFullDuration() {
        return full_duration;
    }

    /**
     * @param full_duration The full_duration
     */
    public void setFullDuration(int full_duration) {
        this.full_duration = full_duration;
    }

    /**
     * @return The genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre The genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind The kind
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return The permalink
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * @param permalink The permalink
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    /**
     * @return The permalink_url
     */
    public String getPermalinkUrl() {
        return permalink_url;
    }

    /**
     * @param permalink_url The permalink_url
     */
    public void setPermalinkUrl(String permalink_url) {
        this.permalink_url = permalink_url;
    }

    /**
     * @return The _public
     */
    public boolean isPublic() {
        return _public;
    }

    /**
     * @param _public The public
     */
    public void setPublic(boolean _public) {
        this._public = _public;
    }

    /**
     * @return The purchase_title
     */
    public String getPurchaseTitle() {
        return purchase_title;
    }

    /**
     * @param purchase_title The purchase_title
     */
    public void setPurchaseTitle(String purchase_title) {
        this.purchase_title = purchase_title;
    }

    /**
     * @return The purchase_url
     */
    public String getPurchaseUrl() {
        return purchase_url;
    }

    /**
     * @param purchase_url The purchase_url
     */
    public void setPurchaseUrl(String purchase_url) {
        this.purchase_url = purchase_url;
    }

    /**
     * @return The state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return The streamable
     */
    public boolean isStreamable() {
        return streamable;
    }

    /**
     * @param streamable The streamable
     */
    public void setStreamable(boolean streamable) {
        this.streamable = streamable;
    }

    /**
     * @return The tag_list
     */
    public String getTagList() {
        return tag_list;
    }

    /**
     * @param tag_list The tag_list
     */
    public void setTagList(String tag_list) {
        this.tag_list = tag_list;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri The uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return The policy
     */
    public String getPolicy() {
        return policy;
    }

    /**
     * @param policy The policy
     */
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getStreamUri() {
        return streamUri = getUri() + "/stream?client_id=" + Constants.CLIENT_ID;
    }

}
