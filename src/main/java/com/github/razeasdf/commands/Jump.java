package com.github.razeasdf.commands;

import com.github.razeasdf.music.MusicManager;
import com.github.razeasdf.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import me.koply.kcommando.integration.impl.javacord.JavacordCommand;
import me.koply.kcommando.internal.Commando;
import me.koply.kcommando.internal.KRunnable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;

@Commando(name = "Jump", aliases = "jump")
public class Jump extends JavacordCommand {

    private static final AudioPlayerManager manager = PlayerManager.getManager();
    private static MusicManager m;

    public Jump() {
        getInfo().setOnFalseCallback((KRunnable<MessageCreateEvent>) event -> event.getMessage().addReaction("⛔"));
    }

    @Override
    public boolean handle(MessageCreateEvent e, String[] args) {
        m = MusicManager.getByEvent(e);
        if (!m.player.getPlayingTrack().isSeekable()) {
            e.getChannel().sendMessage("Can't jump, track isn't seekable");
            return false;
        }
        if (!(args[1] == null)) {
            long seconds = Long.parseLong(args[1]);
            jumpByMilliseconds(e, seconds * 1000);
        } else {
            e.getChannel().sendMessage("Incorrect arguments.");
        }
        return true;
    }


    public void jumpByMilliseconds(MessageCreateEvent e, Long ms) {
        var newPosition = ensureRange(m.player.getPlayingTrack().getPosition() + ms, 0, m.player.getPlayingTrack().getDuration());
        m.player.getPlayingTrack().setPosition(newPosition);
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Meeme?")
                .setColor(Color.RED)
                .setDescription("Jumped to " + getTimestamp(newPosition));
        e.getChannel().sendMessage(embed);
    }

    public long ensureRange(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

    public String getTimestamp(long ms) {
        long seconds = ms / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
        } else {
            return String.format("%02d:%02d", minutes, seconds % 60);
        }
    }

}
