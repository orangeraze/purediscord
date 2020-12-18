package com.github.razeasdf.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public class PlayerManager {

    // This is created because there is no need to have multiple AudioPlayerManager
    // as a single one can handle everything.

    private static final AudioPlayerManager manager = new DefaultAudioPlayerManager();

    /**
     * This is only here since we want to initialize the thing
     * from the start, from the YouTube Source, etc.
     */
    public static void init() {
        // Registers YouTubeAudioSearch with allow search.
        manager.registerSourceManager(new YoutubeAudioSourceManager(true));
    }

    /**
     * Retrieves the AudioPlayerManager.
     *
     * @return AudioPlayerManager.
     */
    public static AudioPlayerManager getManager() {
        return manager;
    }
}

//import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
//import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
//import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
//import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
//import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
//import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
//import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
//import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
//
//public class PlayerManager {
//    public final MusicManager getMusicManager(Guild guild) {
//        return musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
//            final GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);
//            guild.getAudioManager().setSendingHandler(guildMusicManager.getHandler());
//            return guildMusicManager;
//        });
//    }
//
//
//}
