package com.example.android.mediaplayer;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.android.mediaplayer.R.id.play;

/**
 * Created by Admin on 6/28/2017.
 */

public class SongsFragment extends Fragment {

    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 4000;

    private MediaPlayer sMediaPlayer;
    private AudioManager sAudioManager;
    private ImageButton playButton;
    private  ImageButton rewind;
    private TextView selectedFile = null;
    private boolean isStarted = true;
    private String currentFile = "";
    private final Handler handler = new Handler();
    private LinearLayout playView;


    private final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };


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
            stopPlay();
    }};




    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {


        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            return false;
        }
    };




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.song_list,container,false);

        sAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        playButton = (ImageButton) rootView.findViewById(R.id.play);
        ImageButton fast_forward = (ImageButton) rootView.findViewById(R.id.next);
        rewind = (ImageButton) rootView.findViewById(R.id.prev);
        selectedFile = (TextView) rootView.findViewById(R.id.selectedfile);
        playButton = (ImageButton) rootView.findViewById(play);
        selectedFile = (TextView) rootView.findViewById(R.id.selectedfile);
        playView = (LinearLayout) rootView.findViewById(R.id.play_view);
        playView.setVisibility(View.INVISIBLE);



        final ArrayList<Song> songs = new ArrayList<Song>();
        songs.add(new Song("Mask Off (Remix)","Future ft Kendrick Lamar",R.raw.audio,android.R.drawable.ic_media_play));
        songs.add(new Song("Colors of the wind", "Vannessa Williams",R.raw.colors_of_the_wind,android.R.drawable.ic_media_play));
        songs.add(new Song("Don't dream it's over", "Crowded House",R.raw.dont_dream_its_over,android.R.drawable.ic_media_play));
        songs.add(new Song("Let's get retarded ", "The Black Eyed Peas",R.raw.lets_get_retarded,android.R.drawable.ic_media_play));
        songs.add(new Song("Hit me baby one more time", "Britney Spears",R.raw.hit_me_baby_one_more_time,android.R.drawable.ic_media_play));
        songs.add(new Song("Niggas in Paris", "Jay-Z & Kanye West",R.raw.niggas_in_paris,android.R.drawable.ic_media_play));
        songs.add(new Song("See you Again", "Miley Cyrus",R.raw.see_you_again,android.R.drawable.ic_media_play));
        songs.add(new Song("Work it", "Missy Elliot",R.raw.missy_workit,android.R.drawable.ic_media_play));
        songs.add(new Song("Bang Bang Bang", "Mark Ronson",R.raw.bang_bang_bang,android.R.drawable.ic_media_play));
        songs.add(new Song("Unbreak my Heart", "Toni braxton",R.raw.unbreak_my_heart,android.R.drawable.ic_media_play));
        SongAdapter adapter = new SongAdapter(getActivity(),R.color.category_albums,songs);
        final ListView listView = (ListView) rootView.findViewById(R.id.list);


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
                    playView.setVisibility(View.VISIBLE);
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                    selectedFile.setText(song.getArtistName() + " - "+ song.getSongTitle());

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    sMediaPlayer.setOnCompletionListener(sOnCompletionListener);
                    sMediaPlayer.setOnErrorListener(onError);
                }}
        });
        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {switch (v.getId()) {
                case play: {
                    if (sMediaPlayer.isPlaying()) {
                        handler.removeCallbacks(updatePositionRunnable);
                        sMediaPlayer.pause();
                        playButton.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        if (isStarted) {
                            sMediaPlayer.start();
                            playButton.setImageResource(android.R.drawable.ic_media_pause);

                            updatePosition();
                        } else {
                            startPlay(currentFile);
                        }
                    }

                    break;
                }
                case R.id.next: {
                    int seekto = sMediaPlayer.getCurrentPosition() + STEP_VALUE;

                    if (seekto > sMediaPlayer.getDuration())
                        seekto = sMediaPlayer.getDuration();

                    sMediaPlayer.pause();
                    sMediaPlayer.seekTo(seekto);
                    sMediaPlayer.start();

                    break;
                }
                case R.id.prev: {
                    int seekto = sMediaPlayer.getCurrentPosition() - STEP_VALUE;

                    if (seekto < 0)
                        seekto = 0;

                    sMediaPlayer.pause();
                    sMediaPlayer.seekTo(seekto);
                    sMediaPlayer.start();

                    break;
                }
            }
            }



        };



       playButton.setOnClickListener(onClickListener);
        fast_forward.setOnClickListener(onClickListener);
        rewind.setOnClickListener(onClickListener);
















        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updatePositionRunnable);
        sMediaPlayer.stop();
        sMediaPlayer.reset();
        sMediaPlayer.release();

        sMediaPlayer = null;

    }



    public SongsFragment() {}

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
    private void updatePosition() {
        handler.removeCallbacks(updatePositionRunnable);

        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
    }


    private void stopPlay() {
        sMediaPlayer.stop();
        sMediaPlayer.reset();
        playButton.setImageResource(android.R.drawable.ic_media_play);
        handler.removeCallbacks(updatePositionRunnable);

        isStarted = false;
    }

    private void startPlay(String file) {
        Log.i("Selected: ", file);

        selectedFile.setText(file);

        sMediaPlayer.stop();
        sMediaPlayer.reset();

        try {
            sMediaPlayer.setDataSource(file);
            sMediaPlayer.prepare();
            sMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playButton.setImageResource(android.R.drawable.ic_media_pause);

        updatePosition();

        isStarted = true;
    }








}
