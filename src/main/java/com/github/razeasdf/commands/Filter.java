package com.github.razeasdf.commands;

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.github.natanbc.lavadsp.volume.VolumePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;

@Commando(name = "Filter", aliases = "filter")
public class Filter extends JavacordCommand {
    public Filter() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent event) {
        ServerVoiceChannel channel = event.getMessageAuthor().getConnectedVoiceChannel().get();
        channel.connect().thenAccept(audioConnection -> {
            AudioConnection connection = audioConnection;
            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            playerManager.registerSourceManager(new YoutubeAudioSourceManager());
            AudioPlayer player = playerManager.createPlayer();
            player.setFilterFactory((track, format, output)->{
                TimescalePcmAudioFilter timescalePcmAudioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                timescalePcmAudioFilter.setSpeed(0.9);
                VolumePcmAudioFilter volumePcmAudioFilter = new VolumePcmAudioFilter(output, format.channelCount, format.sampleRate);
                volumePcmAudioFilter.setVolume(4.0f);
                return Arrays.asList(volumePcmAudioFilter, timescalePcmAudioFilter);
            });
            });
        }

    }
