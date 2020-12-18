package com.github.razeasdf.commands;

import com.github.razeasdf.music.AudioManager;
import com.github.razeasdf.music.MusicManager;
import com.github.razeasdf.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

@Commando(name = "Loop", aliases = "loop")
public class Loop extends JavacordCommand {

    private final AudioPlayerManager manager = PlayerManager.getManager();

    public Loop() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent event, String[] args) {

        event.getMessageAuthor().getConnectedVoiceChannel().ifPresentOrElse(voiceChannel -> {
                    if (voiceChannel.canYouConnect() && voiceChannel.canYouSee() && voiceChannel.hasPermission(event.getApi().getYourself(), PermissionType.SPEAK)) {
                        MusicManager m = AudioManager.get(event.getChannel().getId());
                        Server server = event.getServer().get();

                        if (server.getAudioConnection().isPresent()) {
                            server.getAudioConnection().ifPresent(audioConnection -> {
                                if (audioConnection.getChannel().getId() == voiceChannel.getId()) {

                                    if (m.scheduler.isRepeating()) {
                                        m.scheduler.setRepeating(false);
                                        event.getChannel().sendMessage("Loop disabled");
                                    } else {
                                        m.scheduler.setRepeating(true);
                                        event.getChannel().sendMessage("Loop enabled");
                                    }
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
}