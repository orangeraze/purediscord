package com.github.razeasdf;

import com.github.razeasdf.music.PlayerManager;
import me.koply.kcommando.KCommando;
import me.koply.kcommando.integration.impl.javacord.JavacordIntegration;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main extends JavacordIntegration {

    public Main(DiscordApi discordApi) {
        super(discordApi);
    }

    public static void main(String[] args) {
        // Insert your bot's token here
        String token = System.getenv("BOT_TOKEN");

        DiscordApi discordApi = new DiscordApiBuilder()
                .setToken(token)
                .login().join();

        KCommando kcommando = new KCommando(new Main(discordApi))
                .setPackage("com.github.razeasdf.commands") // command classes package path
                .setPrefix("!")
                .build();

        PlayerManager.init();


//        discordApi.addMessageCreateListener(event -> {
//            String message = event.getMessageContent();
//            String[] messageTwo = message.split(" ");
//            if (messageTwo[0].equals("!filter") && event.getMessageAuthor().getConnectedVoiceChannel().isPresent()) {
//                player.setFilterFactory((track, format, output) -> {
//                    TimescalePcmAudioFilter timescalePcmAudioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
//                    timescalePcmAudioFilter.setRate(Double.parseDouble(messageTwo[1]));
//                        VolumePcmAudioFilter volumePcmAudioFilter = new VolumePcmAudioFilter(output, format.channelCount, format.sampleRate);
//                        volumePcmAudioFilter.setVolume(Float.parseFloat(messageTwo[2]));
//                    return Arrays.asList(timescalePcmAudioFilter, volumePcmAudioFilter);
//                });
//                event.getChannel().sendMessage("filter applied!");
//            }
    }
}