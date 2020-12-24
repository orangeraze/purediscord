package com.github.razeasdf.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.yamusic.YandexMusicAudioSourceManager;


public class PlayerManager {

    // This is created because there is no need to have multiple AudioPlayerManager
    // as a single one can handle everything.

    private static final AudioPlayerManager manager = new DefaultAudioPlayerManager();

    /**
     * This is only here since we want to initialize the thing
     * from the start, from the YouTube Source, etc.
     */
    public static void init(){
        // Registers YouTubeAudioSearch with allow search.
        manager.registerSourceManager(new YoutubeAudioSourceManager(true));
        manager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
        manager.registerSourceManager(new TwitchStreamAudioSourceManager());
        manager.registerSourceManager(new YandexMusicAudioSourceManager());
        AudioSourceManagers.registerRemoteSources(manager);
    }

    /**
     * Retrieves the AudioPlayerManager.
     * @return AudioPlayerManager.
     */
    public static AudioPlayerManager getManager(){
        return manager;
    }
}

