
package com.musicstream.rest.model;

import java.util.ArrayList;
import java.util.List;

public class Tracks {

    private String genre;
    private String kind;
    private List<TrackCollection> collection = new ArrayList<TrackCollection>();

    /**
     * 
     * @return
     *     The genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * 
     * @param genre
     *     The genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * 
     * @return
     *     The kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * 
     * @param kind
     *     The kind
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * 
     * @return
     *     The collection
     */
    public List<TrackCollection> getCollection() {
        return collection;
    }

    /**
     * 
     * @param collection
     *     The collection
     */
    public void setCollection(List<TrackCollection> collection) {
        this.collection = collection;
    }

}
