package com.gmail.ne0nx3r0.coolpoints.points;

import java.util.HashMap;

public class PlayerProfileManager
{
    private HashMap<String,CoolPointsPlayer> playerProfiles;

    public PlayerProfileManager()
    {
        playerProfiles = new HashMap<>();
    }

    public void loadProile(String playerName)
    {
        String sPlayerNameLower = playerName.toLowerCase();
        
        if(!this.playerProfiles.containsKey(sPlayerNameLower))
        {
            
        }
    }
}
