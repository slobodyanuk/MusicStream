package com.musicstream.rest.service;


import com.musicstream.rest.model.Tracks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Serhii Slobodyanuk on 30.03.2016.
 */
public interface ApiService {
    @GET("/charts")
    Call<Tracks> getTracks(@Query("kind") String kind,
                           @Query("genre") String genres,
                           @Query("client_id") String client_id,
                           @Query("limit") int limit);

    @GET("/charts")
    Call<Tracks> getHotTracks(@Query("kind") String kind,
                              @Query("genre") String genres,
                              @Query("client_id") String client_id,
                              @Query("limit") int limit);

    @GET("/users/{id}/playlists")
    Call<Tracks> getPlaylists(@Path("id") String id,
                              @Query("client_id") String client_id);
}
