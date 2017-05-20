package com.example.android.miwok;

/**
 * {@Link Word} represents a vocabulary word that the user wants to learn.
 * It contains a default translation and a Miwok translation for that word.
 */
public class Word {


    // Holds default translation
    private String mDefaultTranslation;
    // Holds miwok translation
    private String mMiwokTranslation;
    // Holds image resource ID
    private int mImgResourceID;

    private int mAudioResourceID;


    private boolean mHasImage;

    /*
     * Constructor with both translations
     */
    public Word(String defaultTranslation, String miwokTranslation) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
    }

    /*
     * Constructor with both translations and an image resource ID
     */
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceID) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImgResourceID = imageResourceID;
    }


    /*
     * Constructor with both translations, an image resource ID, and an audio resource ID
     */
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceID, int audioResourceID) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImgResourceID = imageResourceID;
        mAudioResourceID = audioResourceID;
    }



    // Get default translation for the word
    public String getDefaultTranslation() {return mDefaultTranslation;}

    // Get miwok translation for the word
    public String getMiWokTranslation() {return mMiwokTranslation;}

    // Get image resource ID
    public int getImgResourceID() {return mImgResourceID;}

    public int getAudioResourceID() {return mAudioResourceID;}


    // Method that tells if the imageResourceID has been set or not
    public boolean hasImage() {
        if (mImgResourceID > 0) {
            mHasImage = true;
        } else {
            mHasImage = false;
        }
        return mHasImage;
    }

}



