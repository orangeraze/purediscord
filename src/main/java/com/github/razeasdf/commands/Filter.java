package com.github.razeasdf.commands;

import java.util.Collections;

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.github.razeasdf.music.AudioManager;
import com.github.razeasdf.music.MusicManager;
import com.github.razeasdf.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.message.MessageCreateEvent;

import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;

@Commando(name = "Filter", aliases = "filter")
public class Filter extends JavacordCommand {
    private final AudioPlayerManager manager = PlayerManager.getManager();

    public Filter() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent event) {
        MusicManager m = AudioManager.get(event.getChannel().getId());
        ServerVoiceChannel channel = event.getMessageAuthor().getConnectedVoiceChannel().get();
        String[] message = event.getMessageContent().split(" ");
        m.player.setFilterFactory((track, format, output) -> {
            TimescalePcmAudioFilter timescale = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
            System.out.println(message[1]);
            timescale.setRate(Double.parseDouble(message[1])); //1.5x normal speed
            return Collections.singletonList(timescale);
        });

//                    audioConnection -> {
//                AudioConnection connection = audioConnection;
//                try {
//                    channel.connect().get();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
        event.getChannel().sendMessage("applied!");
//            });
        return true;
    }
}

//        m.player.setFilterFactory(null);
