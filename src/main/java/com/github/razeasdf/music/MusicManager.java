package com.github.razeasdf.music;

import com.github.razeasdf.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.javacord.api.event.message.MessageCreateEvent;

public class MusicManager {

    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;

    /**
     * Creates a player and a track scheduler.
     * @param manager Audio player manager to use for creating the player.
     */
    public MusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    public static MusicManager getByEvent(MessageCreateEvent event) {
        return AudioManager.get(event.getChannel().getId());
    }

}

//import com.github.razeasdf.TrackScheduler;
//import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
//import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
//
//public final class MusicManager {
//    public final AudioPlayer audioPlayer;
//    public final TrackScheduler scheduler;
//
//    public MusicManager(AudioPlayerManager manager) {
//        audioPlayer = manager.createPlayer();
//        scheduler = new TrackScheduler(audioPlayer);
//        audioPlayer.addListener(scheduler);
//    }
//}
