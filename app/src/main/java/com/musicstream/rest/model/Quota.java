package com.musicstream.rest.model;

/**
 * Created by slobodyanuk on 23.06.16.
 */
public class Quota {

    public Boolean unlimitedUploadQuota;
    public Integer uploadSecondsUsed;
    public Integer uploadSecondsLeft;

    public Boolean getUnlimitedUploadQuota() {
        return unlimitedUploadQuota;
    }

    public void setUnlimitedUploadQuota(Boolean unlimitedUploadQuota) {
        this.unlimitedUploadQuota = unlimitedUploadQuota;
    }

    public Integer getUploadSecondsUsed() {
        return uploadSecondsUsed;
    }

    public void setUploadSecondsUsed(Integer uploadSecondsUsed) {
        this.uploadSecondsUsed = uploadSecondsUsed;
    }

    public Integer getUploadSecondsLeft() {
        return uploadSecondsLeft;
    }

    public void setUploadSecondsLeft(Integer uploadSecondsLeft) {
        this.uploadSecondsLeft = uploadSecondsLeft;
    }
}
