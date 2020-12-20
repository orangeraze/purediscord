package com.github.razeasdf.commands;

import com.github.razeasdf.LavaplayerAudioSource;
import com.github.razeasdf.music.MusicManager;
import com.github.razeasdf.music.PlayerManager;
import com.github.razeasdf.utils.BotUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;

@Commando(name = "Play the music!", aliases = "play")
public class Play extends JavacordCommand {
    // We retrieve the AudioPlayerManager from the PlayerManager.
    private static final AudioPlayerManager manager = PlayerManager.getManager();

    public Play() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> {
            event.getMessage().addReaction("⛔");
        });
    }

    private static boolean isUrl(String argument) {
        return argument.startsWith("https://") || argument.startsWith("http://");
    }

    public static void playMusic(MessageCreateEvent event, String query) {
        AudioConnection audioConnection = event.getServer().orElseThrow().getAudioConnection().orElseThrow();
        MusicManager m = MusicManager.getByEvent(event);
        AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);

        audioConnection.setAudioSource(audio);
        audioConnection.setSelfDeafened(true);
        manager.loadItemOrdered(m,
                isUrl(query) ? query : "ytsearch: " + query.replace("!play ", ""),
                new AudioLoadResultHandler() {

                    @Override
                    public void trackLoaded(AudioTrack audioTrack) {
                        m.scheduler.queue(audioTrack);
                        EmbedBuilder embed = new EmbedBuilder()
                                .setAuthor("Meeme?")
                                .setTitle(String.format("Added to queue: [%s](%s)",
                                        audioTrack.getInfo().title,
                                        audioTrack.getInfo().uri));
                        event.getChannel().sendMessage(embed);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist audioPlaylist) {
                        if (audioPlaylist.isSearchResult()) {
                            m.scheduler.queue(audioPlaylist.getTracks().get(0));
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setAuthor("Meeme?")
                                    .setColor(Color.RED)
                                    .setDescription(String.format("Added to queue: __**[%s](%s)**__ by <@%s>",
                                            audioPlaylist.getTracks().get(0).getInfo().title,
                                            audioPlaylist.getTracks().get(0).getInfo().uri,
                                            event.getMessageAuthor().getIdAsString()));
                            event.getChannel().sendMessage(embed);
                        } else {
                            audioPlaylist.getTracks().forEach(m.scheduler::queue);
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setAuthor("Meeme?")
                                    .setColor(Color.RED)
                                    .setDescription(String.format("Queued __**%s**__ tracks by <@%s>",
                                            audioPlaylist.getTracks().size(),
                                            event.getMessageAuthor().getIdAsString()));
                            event.getChannel().sendMessage(embed);
                        }
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

    @Override
    public boolean handle(MessageCreateEvent event) {
        String query = event.getMessageContent();

        event.getMessageAuthor().getConnectedVoiceChannel().ifPresentOrElse(voiceChannel -> {
                    if (BotUtil.canSeeAndConnect(event)) {
                        Server server = event.getServer().orElseThrow();
                        if (BotUtil.isNotConnected(event)) {
                            BotUtil.connectAndPlay(event, query);
                        } else {
                            if (BotUtil.hasAudioConnection(event)) {
                                server.getAudioConnection().ifPresent(audioConnection -> {
                                    if (BotUtil.isConnectedToUsersChannel(voiceChannel)) {
                                        playMusic(event, query);
                                    } else {
                                        event.getChannel().sendMessage("You are not connected with the same channel as the bot.");
                                    }
                                });
                            }
                        }
                    } else {
                        event.getChannel().sendMessage("Either I cannot connect, cannot see, or do not have the permission to speak on the channel.");
                    }
                },
                () -> event.getChannel().sendMessage("You are not connected in any voice channel."));
        return true;

    }

}