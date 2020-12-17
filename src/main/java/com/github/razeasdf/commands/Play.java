package com.github.razeasdf.commands;

import com.github.razeasdf.LavaplayerAudioSource;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.message.MessageCreateEvent;

@Commando(name = "Play the music!", aliases = "play")
public class Play extends JavacordCommand {
    public Play() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent event) {
        String message = event.getMessageContent();
        String[] messageTwo = message.split(" ");
        ServerVoiceChannel channel = event.getMessageAuthor().getConnectedVoiceChannel().get();
        channel.connect().thenAccept(audioConnection -> {
            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            playerManager.registerSourceManager(new YoutubeAudioSourceManager());
            AudioPlayer player = playerManager.createPlayer();
            AudioSource source = new LavaplayerAudioSource(event.getApi(), player);
            audioConnection.setAudioSource(source);
            String sourceLink = messageTwo[1];
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
        return true;
    }

}
