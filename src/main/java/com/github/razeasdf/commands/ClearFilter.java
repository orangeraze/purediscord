package com.github.razeasdf.commands;

import com.github.razeasdf.music.AudioManager;
import com.github.razeasdf.music.MusicManager;
import com.github.razeasdf.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.message.MessageCreateEvent;

import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;

@Commando(name = "Clear all filters", aliases = "clearfilters")
public class ClearFilter extends JavacordCommand {
    private final AudioPlayerManager manager = PlayerManager.getManager();

    public ClearFilter() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent event) {
        MusicManager m = AudioManager.get(event.getChannel().getId());
        ServerVoiceChannel channel = event.getMessageAuthor().getConnectedVoiceChannel().get();
        m.player.setFilterFactory(null);
        return true;
    }
}
