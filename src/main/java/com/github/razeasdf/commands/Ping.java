package com.github.razeasdf.commands;

import org.javacord.api.event.message.MessageCreateEvent;

import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;

@Commando(name = "Ping!", aliases = "ping")
public class Ping extends JavacordCommand {

    public Ping() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) e -> e.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent e) {
        e.getChannel().sendMessage("Pong!");
        return true;
    }

}
