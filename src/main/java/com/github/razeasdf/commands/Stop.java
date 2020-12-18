package com.github.razeasdf.commands;

import com.github.razeasdf.music.AudioManager;
import com.github.razeasdf.music.MusicManager;
import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;
import org.javacord.api.event.message.MessageCreateEvent;

@Commando(name = "Stop", aliases = "stop")
public class Stop extends JavacordCommand {

    public Stop() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent e, String[] args) {

        e.getServer().get().getAudioConnection().ifPresentOrElse(audioConnection -> {
                    MusicManager m = AudioManager.get(e.getChannel().getId());
                    m.player.stopTrack();
                    audioConnection.close();
                    e.getChannel().sendMessage("Player is stopped");
                },
                () -> e.getChannel().sendMessage("Music is not playing"));

        return true;
    }

}