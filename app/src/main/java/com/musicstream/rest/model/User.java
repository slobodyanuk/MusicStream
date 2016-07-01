package com.musicstream.rest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by slobodyanuk on 23.06.16.
 */

public class User {

    public String id;
    public String kind;
    public String permalink;
    public String username;
    public String lastModified;
    public String uri;
    public String permalinkUrl;
    public String avatarUrl;
    public String country;
    public String firstName;
    public String lastName;
    public String fullName;
    public String description;
    public String city;
    public String discogsName;
    public String myspaceName;
    public String website;
    public String websiteTitle;
    public Boolean online;
    public String trackCount;
    public String playlistCount;
    public String plan;
    public Integer publicFavoritesCount;
    public List<String> subscriptions = new ArrayList<String>();
    public Integer uploadSecondsLeft;
    public Quota quota;
    public String privateTracksCount;
    public String privatePlaylistsCount;
    public Boolean primaryEmailConfirmed;
    public String locale;
    public String followersCount;
    public String followingsCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDiscogsName() {
        return discogsName;
    }

    public void setDiscogsName(String discogsName) {
        this.discogsName = discogsName;
    }

    public String getMyspaceName() {
        return myspaceName;
    }

    public void setMyspaceName(String myspaceName) {
        this.myspaceName = myspaceName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public void setWebsiteTitle(String websiteTitle) {
        this.websiteTitle = websiteTitle;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(String trackCount) {
        this.trackCount = trackCount;
    }

    public String getPlaylistCount() {
        return playlistCount;
    }

    public void setPlaylistCount(String playlistCount) {
        this.playlistCount = playlistCount;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Integer getPublicFavoritesCount() {
        return publicFavoritesCount;
    }

    public void setPublicFavoritesCount(Integer publicFavoritesCount) {
        this.publicFavoritesCount = publicFavoritesCount;
    }

    public List<String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Integer getUploadSecondsLeft() {
        return uploadSecondsLeft;
    }

    public void setUploadSecondsLeft(Integer uploadSecondsLeft) {
        this.uploadSecondsLeft = uploadSecondsLeft;
    }

    public Quota getQuota() {
        return quota;
    }

    public void setQuota(Quota quota) {
        this.quota = quota;
    }

    public String getPrivateTracksCount() {
        return privateTracksCount;
    }

    public void setPrivateTracksCount(String privateTracksCount) {
        this.privateTracksCount = privateTracksCount;
    }

    public String getPrivatePlaylistsCount() {
        return privatePlaylistsCount;
    }

    public void setPrivatePlaylistsCount(String privatePlaylistsCount) {
        this.privatePlaylistsCount = privatePlaylistsCount;
    }

    public Boolean getPrimaryEmailConfirmed() {
        return primaryEmailConfirmed;
    }

    public void setPrimaryEmailConfirmed(Boolean primaryEmailConfirmed) {
        this.primaryEmailConfirmed = primaryEmailConfirmed;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getFollowingsCount() {
        return followingsCount;
    }

    public void setFollowingsCount(String followingsCount) {
        this.followingsCount = followingsCount;
    }
}
