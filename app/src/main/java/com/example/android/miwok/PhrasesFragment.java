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
public class PhrasesFragment extends Fragment {


    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.i("PhrasesActivity", "AUDIO FOCUS LOSS");
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
                    Log.i("PhrasesActivity", "AUDIO FOCUS LOSS TRANSIENT");
                 /* Pause playback because your Audio Focus was
                 *  temporarily stolen, but will be back soon.
                 * i.e. for a phone call
                 */
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.i("PhrasesActivity", "AUDIO FOCUS LOSS TRANSIENT CAN DUCK");
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
                    Log.i("PhrasesActivity", "AUDIO FOCUS GAIN");
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


    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Create and setup the {@link AudioManager} to request audio focus;
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create an ArrayList of Word objects
        final ArrayList<Word> phrases = new ArrayList<Word>();
        phrases.add(new Word("Where are you going?", "minto wuksus", 0, R.raw.phrase_where_are_you_going));
        phrases.add(new Word("What is your name?", "tinnә oyaase'nә", 0, R.raw.phrase_what_is_your_name));
        phrases.add(new Word("My name is...", "oyaaset...", 0, R.raw.phrase_my_name_is));
        phrases.add(new Word("How are you feeling?", "michәksәs?", 0, R.raw.phrase_how_are_you_feeling));
        phrases.add(new Word("I'm feeling good.", "kuchi achit", 0, R.raw.phrase_im_feeling_good));
        phrases.add(new Word("Are you coming?", "әәnәs'aa?", 0, R.raw.phrase_are_you_coming));
        phrases.add(new Word("Yes, I'm coming.", "hәә’ әәnәm", 0, R.raw.phrase_yes_im_coming));
        phrases.add(new Word("I'm coming.", "әәnәm", 0, R.raw.phrase_im_coming));
        phrases.add(new Word("Let's go.", "yoowutis", 0, R.raw.phrase_lets_go));
        phrases.add(new Word("Come here.", "әnni'nem", 0, R.raw.phrase_come_here));


        // Create a {@link WordAdapter}, whose data source is a list of
        // {@link Word}s. The adapter knows how to create list item views for each item
        // in the list.
        WordAdapter phrasesAdapter = new WordAdapter(getActivity(), phrases, R.color.category_phrases);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(phrasesAdapter);


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
                    mMediaPlayer = MediaPlayer.create(getActivity(), phrases.get(position).getAudioResourceID());
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
