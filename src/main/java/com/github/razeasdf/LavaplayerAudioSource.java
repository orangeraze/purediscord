package com.github.razeasdf;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.audio.AudioSourceBase;

public class LavaplayerAudioSource extends AudioSourceBase {
    private final AudioPlayer player;
    private AudioFrame lastFrame;

    /**
     * Creates a new lavaplayer audio source.
     *
     * @param discordApi         A discord api instance.
     * @param player An audio player from Lavaplayer.
     */
    public LavaplayerAudioSource(DiscordApi discordApi, AudioPlayer player) {
        super(discordApi);
        this.player = player;
    }

    @Override
    public byte[] getNextFrame() {
        if (lastFrame == null) {
            return null;
        }
        return applyTransformers(lastFrame.getData());
    }

    @Override
    public boolean hasFinished() {
        return false;
    }

    @Override
    public boolean hasNextFrame() {
        lastFrame = player.provide();
        return lastFrame != null;
    }

    @Override
    public AudioSource copy() {
        return new LavaplayerAudioSource(getApi(), player);
    }

}