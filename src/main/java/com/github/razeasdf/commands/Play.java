package com.github.razeasdf.commands;

import com.github.razeasdf.LavaplayerAudioSource;
import com.github.razeasdf.music.AudioManager;
import com.github.razeasdf.music.MusicManager;
import com.github.razeasdf.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;

@Commando(name = "Play the music!", aliases = "play")
public class Play extends JavacordCommand {
    //    String message = event.getMessageContent();
    // We retrieve the AudioPlayerManager from the PlayerManager.
    private final AudioPlayerManager manager = PlayerManager.getManager();

    public Play() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent event, String[] args) {

        event.getMessageAuthor().getConnectedVoiceChannel().ifPresentOrElse(voiceChannel -> {
                    if (voiceChannel.canYouConnect() && voiceChannel.canYouSee() && voiceChannel.hasPermission(event.getApi().getYourself(), PermissionType.SPEAK)) {

                        MusicManager m = AudioManager.get(event.getChannel().getId());
                        String[] query = event.getMessageContent().split(" ");
                        Server server = event.getServer().get();

                        if (!voiceChannel.isConnected(event.getApi().getYourself()) && server.getAudioConnection().isEmpty()) {

                            voiceChannel.connect().thenAccept(audioConnection -> {
                                AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
                                audioConnection.setAudioSource(audio);
                                playMusic(event, m, query, audioConnection);
                            });

                            System.out.println(query[1]);

                        } else if (server.getAudioConnection().isPresent()) {
                            server.getAudioConnection().ifPresent(audioConnection -> {
                                if (audioConnection.getChannel().getId() == voiceChannel.getId()) {
                                    AudioSource audio = audioConnection.getAudioSource().get();
                                    playMusic(event, m, query, audioConnection);
                                } else
                                    event.getChannel().sendMessage("You are not connected with the same channel as the bot.");
                            });
                        }
                    } else
                        event.getChannel().sendMessage("Either I cannot connect, cannot see, or do not have the permission to speak on the channel.");
                },
                () -> event.getChannel().sendMessage("You are not connected in any voice channel."));
        return true;

    }

    private void playMusic(MessageCreateEvent event, MusicManager m, String[] query, AudioConnection audioConnection) {
        audioConnection.setSelfDeafened(true);
        manager.loadItem(query[1], new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                event.getChannel().sendMessage("Added to queue!");
                m.scheduler.queue(audioTrack);

            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                event.getChannel().sendMessage("Playlist loaded!");
            }

            @Override
            public void noMatches() {
                event.getChannel().sendMessage("Error: No matches!");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.getChannel().sendMessage("Error: load failed!");
            }
        });
    }
}

//            String query = event.getMessageContent().replace("!" + args[0] + " ", "");
//            System.out.println(query);
//            if (!voiceChannel.isConnected(event.getApi().getYourself()) && !(server.getAudioConnection().isPresent()))
//                voiceChannel.connect().thenAccept(audioConnection -> {
//                    AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
//                    audioConnection.setAudioSource(audio);
//                    play(query, event.getServerTextChannel().get(), m);
//                });
//            else if (server.getAudioConnection().isPresent()) {
//                server.getAudioConnection().ifPresent(audioConnection -> {
//                    if (audioConnection.getChannel().getId() == voiceChannel.getId()) {
//                        // Create an audio source and add to audio connection queue, this is where we use the ServerMusicManager as well.
//                        AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
//                        audioConnection.setAudioSource(audio);
//                    } else {
//                        // Tell the user that we cannot connect, or see, or speak in the channel.
//                        event.getChannel().sendMessage("Either I cannot connect, cannot see, or do not have the permission to speak on the channel.");
//                    }
//                });
//            }
//        }, () -> event.getChannel().sendMessage("You are not connected in any voice channel."));
