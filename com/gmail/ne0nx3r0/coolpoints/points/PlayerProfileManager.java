package com.gmail.ne0nx3r0.coolpoints.points;

import com.gmail.ne0nx3r0.coolpoints.CoolPoints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerProfileManager
{
    private final File PROFILE_DIRECTORY;
    
    public PlayerProfileManager()
    {
        PROFILE_DIRECTORY = new File(CoolPoints.self.getDataFolder(),"players");
        
        if(!PROFILE_DIRECTORY.exists())
        {
            PROFILE_DIRECTORY.mkdirs();
        }
    }

    public void unloadProfile(String sPlayerName)
    {
        String sPlayerNameLower = sPlayerName.toLowerCase();
        if(playerProfiles.containsKey(sPlayerNameLower))
        {
            saveProfile(sPlayerNameLower);
            
            playerProfiles.remove(sPlayerNameLower);
        }
    }

    public void saveAllPlayerProfiles()
    {
        for(String sPlayerNameLower : playerProfiles.keySet())
        {
            saveProfile(sPlayerNameLower);
        }
    }

    public int getCoolPoints(String sPlayerName)
    {
        String sPlayerNameToLower = sPlayerName.toLowerCase();
        
        if(playerProfiles.containsKey(sPlayerNameToLower))
        {
            return playerProfiles.get(sPlayerNameToLower).getPoints();
        }
        
        return -1;
    }

    public int giveCoolPoints(String sPlayerName, int iAmount)
    {
        return playerProfiles.get(sPlayerName.toLowerCase()).addPoints(iAmount);
    }

    public boolean hasGiftedToday(String sPlayerName)
    {
        return playerProfiles.get(sPlayerName.toLowerCase()).hasGiftedToday();
    }

    public void playerGiftPlayer(String takeFrom, String giveTo)
    {
        playerProfiles.get(takeFrom.toLowerCase()).addPoints(-1);
        playerProfiles.get(giveTo.toLowerCase()).addPoints(1);
    }
}
