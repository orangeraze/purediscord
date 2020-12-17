package com.github.razeasdf;

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.github.natanbc.lavadsp.volume.VolumePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Insert your bot's token here
        String token = System.getenv("BOT_TOKEN");

        DiscordApi api = new DiscordApiBuilder()
                .setToken(token)
                .login().join();

        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        AudioPlayer player = playerManager.createPlayer();

        api.addMessageCreateListener(event -> {
            String message = event.getMessageContent();

            String[] messageTwo = message.split(" ");
            if (messageTwo[0].equals("!music") && event.getMessageAuthor().getConnectedVoiceChannel().isPresent()) {
                ServerVoiceChannel channel = event.getMessageAuthor().getConnectedVoiceChannel().get();
                channel.connect().thenAccept(audioConnection -> {
// Create a player manager

// Create an audio source and add it to the audio connection's queue
                    AudioSource source = new LavaplayerAudioSource(api, player);
                    audioConnection.setAudioSource(source);

                    String sourceLink = messageTwo[1];
// You can now use the AudioPlayer like you would normally do with Lavaplayer, e.g.,
                    playerManager.loadItem(sourceLink, new AudioLoadResultHandler() {
                        @Override
                        public void trackLoaded(AudioTrack track) {
                            event.getChannel().sendMessage("Pong!");
                            player.playTrack(track);
                        }

                        @Override
                        public void playlistLoaded(AudioPlaylist playlist) {
                            for (AudioTrack track : playlist.getTracks()) {
                                player.playTrack(track);
                            }
                        }

                        @Override
                        public void noMatches() {
                            event.getChannel().sendMessage("nomatches!");
                        }

                        @Override
                        public void loadFailed(FriendlyException throwable) {
                            event.getChannel().sendMessage("exception loadfailed!");
                        }
                    });
                });
            }
        });
        api.addMessageCreateListener(event -> {
            String message = event.getMessageContent();
            String[] messageTwo = message.split(" ");
            if (messageTwo[0].equals("!filter") && event.getMessageAuthor().getConnectedVoiceChannel().isPresent()) {
                player.setFilterFactory((track, format, output) -> {
                    TimescalePcmAudioFilter timescalePcmAudioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                    timescalePcmAudioFilter.setRate(Double.parseDouble(messageTwo[1]));
                        VolumePcmAudioFilter volumePcmAudioFilter = new VolumePcmAudioFilter(output, format.channelCount, format.sampleRate);
                        volumePcmAudioFilter.setVolume(Float.parseFloat(messageTwo[2]));
                    return Arrays.asList(timescalePcmAudioFilter, volumePcmAudioFilter);
                });
                event.getChannel().sendMessage("filter applied!");
            }
        });
    }
}