package com.example.android.mediaplayer;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 6/29/2017.
 */

public class SongAdapter extends ArrayAdapter<Song> {
    private  int mColorResourceId;

    public SongAdapter(Activity context, int mColorResourceId, List<Song> songs) {
        super(context,0, songs);
        this.mColorResourceId = mColorResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.music_list_item,parent,false);
        }

        Song currentSong = getItem(position);

        TextView songTitleTextView = (TextView) listItemView.findViewById(R.id.song_title_text_view);

        songTitleTextView.setText(currentSong.getSongTitle());

        TextView artistNameTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        artistNameTextView.setText(currentSong.getArtistName());

        ImageView iconView = (ImageView) listItemView.findViewById(R.id.image_view);

        ImageView playView = (ImageView) listItemView.findViewById(R.id.play_arrow_black);
        playView.setImageResource(currentSong.getPlayArrowResourceId());

        if (currentSong.hasImage()){ iconView.setImageResource(currentSong.getImageResourceId());}
        else{
            iconView.setVisibility(View.GONE);
        }

        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(),mColorResourceId);
        textContainer.setBackgroundColor(color);


        return listItemView;
    }
}
