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
                List<Map<String,Object>> tempNotes = (List<Map<String,Object>>) yml.get("notes");
                
                if(tempNotes.isEmpty())
                {
                    playerProfiles.put(sPlayerNameLower, new CoolPointsPlayer(
                        sDisplayName,
                        points,
                        firstJoined
                    ));
                }
                else
                {
                    ArrayList<PlayerNote> notes = new ArrayList<>();
                    for(Map<String,Object> tempNote: tempNotes)
                    {
                        notes.add(new PlayerNote(
                            (Long) tempNote.get("created"),
                            (String) tempNote.get("author"),
                            (String) tempNote.get("text")
                        ));
                    }
                    
                    playerProfiles.put(sPlayerNameLower, new CoolPointsPlayer(
                        sDisplayName,
                        points,
                        firstJoined,
                        notes
                    ));
                }
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

            ArrayList<HashMap<String,Object>> notes = new ArrayList<>();
            
            for(PlayerNote pn : cpp.getNotes())
            {
                HashMap<String, Object> tempNote = new HashMap<>();
                
                tempNote.put("created", pn.getCreated());
                tempNote.put("author", pn.getAuthor());
                tempNote.put("text", pn.getText());
                
                notes.add(tempNote);
            }
            
            yml.set("name", cpp.getDisplayName());
            yml.set("points", cpp.getPoints());
            yml.set("firstJoined", cpp.getFirstJoined());
            yml.set("notes", notes);
            
            try
            {
                yml.save(ymlFile);
            }
            catch (IOException ex)
            {
                Logger.getLogger(PlayerProfileManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
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

    public CoolPointsPlayer getCoolPointsPlayer(String sPlayerName)
    {
        return playerProfiles.get(sPlayerName.toLowerCase());
    }

    public boolean playerGiftPlayer(String gifterName, String giftedName)
    {
        CoolPointsPlayer cpGifter = playerProfiles.get(this);
        CoolPointsPlayer cpGifted = playerProfiles.get(this);
        
        if()
        {
            
        }
    }
}
