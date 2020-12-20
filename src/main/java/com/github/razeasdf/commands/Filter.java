package com.github.razeasdf.commands;

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.github.razeasdf.music.AudioManager;
import com.github.razeasdf.music.MusicManager;
import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Collections;

@Commando(name = "Filter", aliases = "filter")

public class Filter extends JavacordCommand {
    public Filter() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent event) {
        MusicManager m = AudioManager.get(event.getChannel().getId());
        ServerVoiceChannel channel = event.getMessageAuthor().getConnectedVoiceChannel().orElseThrow();
        String[] message = event.getMessageContent().split(" ");
        m.player.setFilterFactory((track, format, output)->{
            TimescalePcmAudioFilter timescale = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
            timescale.setRate(Double.parseDouble(message[1])); //1.5x normal speed
            return Collections.singletonList(timescale);
        });

        event.getChannel().sendMessage("applied!");
        return true;
    }
}

