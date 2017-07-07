package com.example.android.mediaplayer;

/**
 * Created by Admin on 6/29/2017.
 */

public class Song {
    private String mSongTitle;
    private String mArtistName;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mAudioResourceId;
    private int mPlayArrowResourceId;

    public Song() {
    }

    public Song(String songTitle, String artistName, int imageResourceId, int audioResourceId, int playArrowResourceId) {
        this.mSongTitle = songTitle;
        this.mArtistName = artistName;
        this.mImageResourceId = imageResourceId;
        this.mAudioResourceId = audioResourceId;
        this.mPlayArrowResourceId = playArrowResourceId;


    }

    public Song(String songTitle, String artistName, int audioResourceId, int playArrowResourceId) {
        this.mSongTitle = songTitle;
        this.mArtistName = artistName;
        this.mAudioResourceId = audioResourceId;
        this.mPlayArrowResourceId = playArrowResourceId;
    }

    public String getSongTitle() {
        return mSongTitle;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public int getAudioResourceId() {
        return mAudioResourceId;
    }

    public int getPlayArrowResourceId() {
        return mPlayArrowResourceId;
    }
    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    @Override
    public String toString() {
        return "Song{" +
                "mSongTitle='" + mSongTitle + '\'' +
                ", mArtistName='" + mArtistName + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                ", mPlayArrowResourceId=" + mPlayArrowResourceId +
                '}';
    }
}
