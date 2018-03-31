package ru.pussy_penetrator.randomcounter;

/**
 * Created by Sex_predator on 11.12.2016.
 */
public class MusicPlayerLab {

    private static MusicPlayer sMusicPlayer;

    private MusicPlayerLab() {
        //it's a singleton
    }

    public static MusicPlayer getPlayer() {
        if (sMusicPlayer == null)
            sMusicPlayer = new MusicPlayer();
        return sMusicPlayer;
    }

}
