package ru.pussy_penetrator.randomcounter;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Sex_predator on 11.12.2016.
 */
public class MusicPlayer {

    private MediaPlayer mLastPlayer;

    public void play(Context context, int soundId) {
        stop();
        mLastPlayer = MediaPlayer.create(context, soundId);
        mLastPlayer.start();
    }

    public int getDuration() {
        if (mLastPlayer == null)
            return 0;
        return mLastPlayer.getDuration();
    }

    public void stop() {
        if (mLastPlayer != null) {
            mLastPlayer.stop();
            mLastPlayer.release();
            mLastPlayer = null;
        }
    }

    public void release() {
        stop();
    }

}
