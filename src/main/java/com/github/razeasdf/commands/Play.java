package com.github.razeasdf.commands;

import com.github.razeasdf.LavaplayerAudioSource;
import com.github.razeasdf.music.AudioManager;
import com.github.razeasdf.music.MusicManager;
import com.github.razeasdf.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

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
        MusicManager m = AudioManager.get(event.getChannel().getId());
        Server server = event.getServer().get();
        event.getMessageAuthor().getConnectedVoiceChannel().ifPresentOrElse(voiceChannel -> {
            String query = event.getMessageContent().replace("!" + args[0] + " ", "");
            System.out.println(query);
            if (!voiceChannel.isConnected(event.getApi().getYourself()) && !(server.getAudioConnection().isPresent()))
                voiceChannel.connect().thenAccept(audioConnection -> {
                    AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
                    audioConnection.setAudioSource(audio);
                    play(query, event.getServerTextChannel().get(), m);
                });
            else if (server.getAudioConnection().isPresent()) {
                server.getAudioConnection().ifPresent(audioConnection -> {
                    if (audioConnection.getChannel().getId() == voiceChannel.getId()) {
                        // Create an audio source and add to audio connection queue, this is where we use the ServerMusicManager as well.
                        AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
                        audioConnection.setAudioSource(audio);
                    } else {
                        // Tell the user that we cannot connect, or see, or speak in the channel.
                        event.getChannel().sendMessage("Either I cannot connect, cannot see, or do not have the permission to speak on the channel.");
                    }
                });
            }
        }, () -> event.getChannel().sendMessage("You are not connected in any voice channel."));

        return true;
    }


//
//    private void play (String query, ServerTextChannel channel, MusicManager m) {
//        manager.loadItem(query, new AudioLoadResultHandler() {
//            @Override
//            public void trackLoaded(AudioTrack audioTrack) {
//                event.getChannel().sendMessage("Pong!");
//                player.playTrack(track);
//
//            }
//
//            @Override
//            public void playlistLoaded(AudioPlaylist playlist) {
//                for (AudioTrack track : playlist.getTracks()) {
//                    player.playTrack(track);
//                }
//            }
//
//            @Override
//            public void noMatches() {
//                event.getChannel().sendMessage("nomatches!");
//            }
//
//            @Override
//            public void loadFailed(FriendlyException throwable) {
//                event.getChannel().sendMessage("exception loadfailed!");
//            }
//        });
//    }

    /**
     * Plays the music and notifies the user that we have successfully played the music.
     *
     * @param query   the query to search for.
     * @param channel the channel where the command was sent.
     * @param m       the server music manager.
     */
    private void play(String query, ServerTextChannel channel, MusicManager m) {
        // Load the track, we use isUrl to see if the argument is a URL, otherwise if it is not then we use YouTube Search to search the query.
        manager.loadItemOrdered(m, isUrl(query) ? query : "ytsearch: " + query, new FunctionalResultHandler(
                audioTrack -> {
                    // This is for track loaded.
                    channel.sendMessage("We have added the track: " + audioTrack.getInfo().title);
                    m.scheduler.queue(audioTrack);

                },
                audioPlaylist -> {
                    // If the playlist is a search result, then we only need to get the first one.
                    if (audioPlaylist.isSearchResult()) {
                        // YOU HAVE TO ADD THIS, UNLESS YOU WANT YOUR BOT TO GO SPAM MODE.
                        m.scheduler.queue(audioPlaylist.getTracks().get(0));
                        channel.sendMessage("We have added the track: " + audioPlaylist.getTracks().get(0).getInfo().title);
                    } else {
                        // If it isn't then simply queue every track.
                        audioPlaylist.getTracks().forEach(audioTrack -> {
                            m.scheduler.queue(audioTrack);
                            channel.sendMessage("We have queued the track: " + audioTrack.getInfo().title);
                        });
                    }
                },
                () -> {
                    // If there are no matches, then we tell the user that we couldn't find any track.
                    channel.sendMessage("We couldn't find the track.");
                },
                e -> {
                    // In the case of when an exception occurs, we inform the user about it.
                    channel.sendMessage("We couldn't play the track: " + e.getMessage());
                }));
    }

    /**
     * Checks if the string is a URL.
     *
     * @param argument the string to validate.
     * @return boolean.
     */
    private boolean isUrl(String argument) {
        return argument.startsWith("https://") || argument.startsWith("http://");
    }
}