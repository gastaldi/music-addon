package com.github.forge.addon.music;

import com.github.forge.addon.music.model.Playlist;
import com.github.forge.addon.music.model.Song;
import com.github.forge.addon.music.player.Player;
import com.github.forge.addon.music.playlist.PlaylistManager;
import com.github.forge.addon.music.util.AudioControl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Created by pestano on 15/10/15.
 */
@RunWith(Arquillian.class)
public class PlayerTest extends BaseTest {


    @Inject
    Player player;

    Song song;

    private static float masterVolume;

    private static boolean masterMute;

    @Deployment
    @AddonDependencies
    public static AddonArchive getDeployment() {

        return ShrinkWrap.create(AddonArchive.class).addClass(BaseTest.class).addBeansXML();
    }

    @BeforeClass
    public static void setUp() {
        if (AudioControl.isAudioEnabled()) {
            masterVolume = AudioControl.getMasterOutputVolume();
            masterMute = AudioControl.getMasterOutputMute();
            AudioControl.setMasterOutputMute(false);
            AudioControl.setMasterOutputVolume(0.30f);
        } else {
            Logger.getLogger(PlayerTest.class.getName()).warning("Audio device is no enabled, tests will be skipped");
        }
    }


    @AfterClass
    public static void tearDown() {
        if (AudioControl.isAudioEnabled()) {
             //AudioControl.setMasterOutputVolume(masterVolume);
            // AudioControl.setMasterOutputMute(masterMute);
        }
    }

    @Test
    public void shouldPlaySong() {
        if (AudioControl.isAudioEnabled()) {
            saveSong();
            player.play();
        }
    }

    @Test
    public void shouldPlayAndResume() {
        if (AudioControl.isAudioEnabled()) {
            saveSong();
            player.play();
            player.pause();
            player.play();
        }
    }

    private void saveSong() {
        Song song = getSampleMp3();
        Playlist playlist = playlistManager.getPlaylist(PlaylistManager.DEFAULT_PLAYLIST);
        playlist.addSong(song);
        playlistManager.savePlaylist(playlist);
    }

    private Song getSampleMp3() {
        if (song == null) {
            song = new Song(Paths.get("target/test-classes").toAbsolutePath() + "/axe.mp3");
        }
        return song;
    }
}
