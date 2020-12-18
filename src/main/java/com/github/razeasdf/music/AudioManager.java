package com.github.razeasdf.music;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    /*
     * A custom class used to store
     * all the MusicManagers to unify them all.
     */

    private static final Map<Long, MusicManager> managers = new HashMap<>();

    /**
     * Retrieves the server music manager dedicated for the server.
     * @param server the server's identification number.
     * @return a MusicManager.
     */
    public static MusicManager get(long server) {
        // If it doesn't exist then we create one.
        if (!managers.containsKey(server)) {
            managers.put(server, new MusicManager(PlayerManager.getManager()));
        }

        return managers.get(server);
    }

}