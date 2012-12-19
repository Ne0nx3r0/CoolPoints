package com.gmail.ne0nx3r0.coolpoints.points;

import com.gmail.ne0nx3r0.coolpoints.CoolPoints;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log.Level;

public class PlayerProfileManager
{
    private HashMap<String,CoolPointsPlayer> playerProfiles;
    private final File PROFILE_DIRECTORY;
    
    public PlayerProfileManager()
    {
        playerProfiles = new HashMap<>();
        
        PROFILE_DIRECTORY = new File(CoolPoints.self.getDataFolder(),"players");
        
        if(!PROFILE_DIRECTORY.exists())
        {
            PROFILE_DIRECTORY.mkdirs();
        }
    }

    public void loadProfile(String playerName)
    {
        String sPlayerNameLower = playerName.toLowerCase();
        
        if(!playerProfiles.containsKey(sPlayerNameLower))
        {
            File ymlFile = new File(PROFILE_DIRECTORY,sPlayerNameLower+".yml");

            if(!ymlFile.exists())
            {
                playerProfiles.put(sPlayerNameLower, new CoolPointsPlayer(
                        playerName,
                        0,
                        System.currentTimeMillis()
                ));
            }
            else
            {
                FileConfiguration yml = YamlConfiguration.loadConfiguration(ymlFile);

                String sDisplayName = yml.getString("name");
                int points = yml.getInt("cp");
                long firstJoined = yml.getLong("firstJoined");

                ///////
            }
        }
    }
    
    public void saveProfile(String sPlayerName)
    {
        String sPlayerNameLower = sPlayerName.toLowerCase();
        
        if(!playerProfiles.containsKey(sPlayerNameLower))
        {
            File ymlFile = new File(PROFILE_DIRECTORY,sPlayerNameLower+".yml");

            if(!ymlFile.exists())
            {
                try
                {
                    ymlFile.createNewFile();
                }
                catch(IOException ex)
                {
                    Logger.getLogger("CoolPoints").log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
            
            FileConfiguration yml = YamlConfiguration.loadConfiguration(ymlFile);

            CoolPointsPlayer cpp = playerProfiles.get(sPlayerNameLower);
            
            yml.set("name", cpp.getDisplayName());
            yml.set("points", cpp.getPoints());
            yml.set("firstJoined", cpp.getFirstJoined());
            
            
        }
    }
}
