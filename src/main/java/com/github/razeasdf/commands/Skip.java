package com.github.razeasdf.commands;

import com.github.razeasdf.music.AudioManager;
import com.github.razeasdf.music.MusicManager;

import org.javacord.api.event.message.MessageCreateEvent;

import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;

@Commando(name = "Skip", aliases = "skip")
public class Skip extends JavacordCommand {

    public Skip() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent e, String[] args) {

        e.getServer().get().getAudioConnection().ifPresentOrElse(audioConnection -> {
                    MusicManager m = AudioManager.get(e.getChannel().getId());
                    m.scheduler.nextTrack();
                    e.getChannel().sendMessage("Skipped current track");
                },
                () -> e.getChannel().sendMessage("Music is not playing"));

        return true;
    }

}
