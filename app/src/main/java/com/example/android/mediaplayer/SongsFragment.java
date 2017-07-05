package com.example.android.mediaplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.id;
import static android.media.CamcorderProfile.get;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Admin on 6/28/2017.
 */

public class SongsFragment extends Fragment {

    private MediaPlayer sMediaPlayer;
    private AudioManager sAudioManager;
    private ImageButton pauseButton;
    private ImageButton playButton;
    boolean paused = true;
    final ArrayList<Song> songs = new ArrayList<Song>();

    private AudioManager.OnAudioFocusChangeListener sOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                sMediaPlayer.pause();
                sMediaPlayer.seekTo(0);
            }else if (focusChange == AudioManager.AUDIOFOCUS_GAIN){
                sMediaPlayer.start();
            }else if (focusChange == AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            }       }
    };
    private MediaPlayer.OnCompletionListener sOnCompletionListener = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp) {
           releaseMediaPlayer();
    }};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.song_list,container,false);

        sAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Song> songs = new ArrayList<Song>();
        songs.add(new Song("Mask Off (Remix)","Future ft Kendrick Lamar",R.raw.audio,R.drawable.ic_play_arrow_black));
        songs.add(new Song("Colors of the wind", "Vannessa Williams",R.raw.colors_of_the_wind,R.drawable.ic_play_arrow_black));
        songs.add(new Song("Don't dream it's over", "Crowded House",R.raw.dont_dream_its_over,R.drawable.ic_play_arrow_black));
        songs.add(new Song("Let's get retarded ", "The Black Eyed Peas",R.raw.lets_get_retarded,R.drawable.ic_play_arrow_black));
        songs.add(new Song("Hit me baby one more time", "Britney Spears",R.raw.hit_me_baby_one_more_time,R.drawable.ic_play_arrow_black));
        songs.add(new Song("Niggas in Paris", "Jay-Z & Kanye West",R.raw.niggas_in_paris,R.drawable.ic_play_arrow_black));
        songs.add(new Song("See you Again", "Miley Cyrus",R.raw.see_you_again,R.drawable.ic_play_arrow_black));
        songs.add(new Song("Work it", "Missy Elliot",R.raw.missy_workit,R.drawable.ic_play_arrow_black));
        songs.add(new Song("Bang Bang Bang", "Mark Ronson",R.raw.bang_bang_bang,R.drawable.ic_play_arrow_black));
        songs.add(new Song("Unbreak my Heart", "Toni braxton",R.raw.unbreak_my_heart,R.drawable.ic_play_arrow_black));
        SongAdapter adapter = new SongAdapter(getActivity(),R.color.category_albums,songs);
        final ListView listView = (ListView) rootView.findViewById(R.id.list);
       sMediaPlayer =  MediaPlayer.create(getActivity(),R.raw.audio);
        pauseButton = (ImageButton) rootView.findViewById(R.id.bt_start_pause);
        playButton = (ImageButton) rootView.findViewById(R.id.bt_start_play);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                // Get the {@link Word} object at the given position the user clicked on
                Song song = songs.get(position);

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = sAudioManager.requestAudioFocus(sOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    sMediaPlayer = MediaPlayer.create(getActivity(), song.getAudioResourceId());

                    // Start the audio file
                    sMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    sMediaPlayer.setOnCompletionListener(sOnCompletionListener);
                }}
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (paused){
                    sMediaPlayer.pause();
                    playButton.setVisibility(View.VISIBLE);
                }



            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseButton.setVisibility(View.VISIBLE);
                sMediaPlayer.start();
            }
        });







        return rootView;
    }



    public SongsFragment() {

    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (sMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            sMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            sMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            sAudioManager.abandonAudioFocus(sOnAudioFocusChangeListener);
        }
    }

}
