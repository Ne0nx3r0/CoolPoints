package com.gmail.ne0nx3r0.coolpoints.points;

import com.gmail.ne0nx3r0.coolpoints.CoolPoints;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerProfileManager
{
    public PlayerProfileManager()
    {
        File PLUGIN_DIR = new File(CoolPoints.self.getDataFolder().getPath());
        
        if(!PLUGIN_DIR.exists())
        {
            PLUGIN_DIR.mkdirs();
        }
    }

    public int getCoolPoints(String sPlayerName)
    {
        try
        {
            PreparedStatement statement = CoolPoints.sqlite.prepare(""
                + "SELECT points "
                + "FROM players "
                + "WHERE username=? LIMIT 1;");
            
            statement.setString(1, sPlayerName.toLowerCase());
            
            ResultSet result = statement.executeQuery();
            
            if(result != null)
            {
                return result.getInt("points");
            }
            else
            {
                return -1;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
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
