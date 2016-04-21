package com.musicstream.events;

import com.musicstream.rest.model.Tracks;

import retrofit2.Response;

/**
 * Created by Serhii Slobodyanuk on 14.04.2016.
 */

public class RestResponseEvent {
    private Response<Tracks> response;

    public RestResponseEvent(Response<Tracks> response) {
        this.response = response;
    }

    public Response<Tracks> getResponse() {
        return response;
    }
}
