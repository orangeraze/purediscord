package com.github.razeasdf.commands;

import com.github.razeasdf.music.MusicManager;
import com.github.razeasdf.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;
import org.javacord.api.event.message.MessageCreateEvent;

@Commando(name = "Queue", aliases = "queue")
public class Queue extends JavacordCommand {
    private static final AudioPlayerManager manager = PlayerManager.getManager();


    public Queue() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent event) {
        MusicManager m = MusicManager.getByEvent(event);
        m.scheduler.getQueueInfo(event);
        return true;
    }
}
