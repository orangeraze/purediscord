package com.github.razeasdf;

import com.github.razeasdf.music.PlayerManager;
import me.koply.kcommando.KCommando;
import me.koply.kcommando.integration.impl.javacord.JavacordIntegration;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.json.JSONObject;

public class Main extends JavacordIntegration {

    private static JSONObject config;
    private static String prefix = "!";
    private static Main instance;

    public Main(DiscordApi discordApi) {
        super(discordApi);
    }

    public static JSONObject getConfig() {
        return config;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        String token = System.getenv("BOT_TOKEN");

        DiscordApi discordApi = new DiscordApiBuilder()
                .setToken(token)
                .login().join();

        instance = new Main(discordApi);

        KCommando kcommando = new KCommando(new Main(discordApi))
                .setPackage("com.github.razeasdf.commands") // command classes package path
                .setPrefix(prefix)
                .build();

        PlayerManager.init();

    }
}