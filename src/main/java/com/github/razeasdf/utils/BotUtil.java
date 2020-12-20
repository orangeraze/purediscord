package com.github.razeasdf.utils;

import com.github.razeasdf.commands.Play;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.message.MessageCreateEvent;

public class BotUtil {

    public static boolean canSeeAndConnect(MessageCreateEvent event) {
        ServerVoiceChannel voiceChannel = event.getServer().orElseThrow().getConnectedVoiceChannel(event.getMessageAuthor().getId()).orElseThrow();
        return voiceChannel.canYouConnect() && voiceChannel.canYouSee() && voiceChannel.hasPermission(event.getApi().getYourself(), PermissionType.SPEAK);
    }

    public static boolean isNotConnected(MessageCreateEvent event) {
        ServerVoiceChannel voiceChannel = event.getServer().orElseThrow().getConnectedVoiceChannel(event.getMessageAuthor().getId()).orElseThrow();
        return !(voiceChannel.isConnected(event.getApi().getYourself()) && event.getServer().get().getAudioConnection().isPresent());
    }

    public static boolean hasAudioConnection(MessageCreateEvent event) {
        return event.getServer().orElseThrow().getAudioConnection().isPresent();
    }

    public static boolean isConnectedToUsersChannel(ServerVoiceChannel voiceChannel) {
        AudioConnection audioConnection = voiceChannel.getServer().getAudioConnection().orElseThrow();
        return audioConnection.getChannel().getId() == voiceChannel.getId();
    }

    public static void connectAndPlay(MessageCreateEvent event, String query) {
        ServerVoiceChannel voiceChannel = event.getServer().orElseThrow().getConnectedVoiceChannel(event.getMessageAuthor().getId()).orElseThrow();
        voiceChannel.connect().thenAccept(audioConnection -> {
            Play.playMusic(event, query);
        });
    }
}
