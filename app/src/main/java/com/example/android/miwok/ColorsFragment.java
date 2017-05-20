package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.i("ColorsActivity", "AUDIO FOCUS LOSS");
                 /*
                  * Stop playback, because you lost the Audio Focus.
                  * i.e. the user started some other playback app
                  * Remember to unregister your controls/buttons here.
                  * And release the Audio Focus!
                  * Your done.
                  */
                    mMediaPlayer.release();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.i("ColorsActivity", "AUDIO FOCUS LOSS TRANSIENT");
                 /* Pause playback because your Audio Focus was
                 *  temporarily stolen, but will be back soon.
                 * i.e. for a phone call
                 */
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.i("ColorsActivity", "AUDIO FOCUS LOSS TRANSIENT CAN DUCK");
                 /* Lower the volume, because something else is also
                  * playing audio over you.
                  * i.e. for notifications or navigation directions
                  * Depending on your audio playback, you may prefer to
                  * pause playback here instead. You do you.
                  */
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                    break;

                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.i("ColorsActivity", "AUDIO FOCUS GAIN");
                 /*
                 * Resume playback, because you hold the Audio Focus
                 * again!
                 * i.e. the phone call ended or the nav directions
                 * are finished
                 * If you implement ducking and lower the volume, be
                 * sure to return it to normal here, as well.
                 */
                    mMediaPlayer.start();
                    break;

            }
        }
    };


    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Create and setup the {@link AudioManager} to request audio focus;
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create an ArrayList of Word objects
        final ArrayList<Word> colorWords = new ArrayList<Word>();
        colorWords.add(new Word("red", "wetetti", R.drawable.color_red, R.raw.color_red));
        colorWords.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        colorWords.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        colorWords.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        colorWords.add(new Word("black", "kulilli", R.drawable.color_black, R.raw.color_black));
        colorWords.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));
        colorWords.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorWords.add(new Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        // Create a {@link WordAdapter}, whose data source is a list of
        // {@link Word}s. The adapter knows how to create list item views for each item
        // in the list.
        WordAdapter colorWordsAdapter = new WordAdapter(getActivity(), colorWords, R.color.category_colors);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(colorWordsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Release the media player if it currently exists because we are about to play a different file
                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_RING, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.


                    // Create a {@link MediaPlayer} for the audio resource associated with the current word
                    mMediaPlayer = MediaPlayer.create(getActivity(), colorWords.get(position).getAudioResourceID());
                    // Play the audio file
                    mMediaPlayer.start();

                    mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                }

                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releaseMediaPlayer();
                    }
                });
            }
        });

        return rootView;
    }


    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(afChangeListener);
        }
    }

}
