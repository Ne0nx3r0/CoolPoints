package com.gmail.ne0nx3r0.coolpoints.points;

import com.gmail.ne0nx3r0.coolpoints.CoolPoints;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

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
                + "FROM player "
                + "WHERE username=? LIMIT 1;");
            
            statement.setString(1, sPlayerName.toLowerCase());
            
            ResultSet result = statement.executeQuery();
            
            if(result.next())
            {
                int iPoints = result.getInt("points");
                
                result.close();
                
                return iPoints;
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
        try
        {
            PreparedStatement statement = CoolPoints.sqlite.prepare(""
                + "SELECT points "
                + "FROM player "
                + "WHERE username = ? LIMIT 1;");
            
            statement.setString(1, sPlayerName.toLowerCase());
            
            ResultSet result = statement.executeQuery();
            
            if(result.next())
            {
                int newPoints = result.getInt("points") + iAmount;
                
                if(newPoints < 0)
                {
                    newPoints = 0;
                }
                
                PreparedStatement updatePoints = CoolPoints.sqlite.prepare(""
                    + "UPDATE player SET points = "+newPoints+" "
                    + "WHERE username = ?");

                updatePoints.setString(1, sPlayerName.toLowerCase());

                updatePoints.execute();   
                
                return newPoints;
            }
            if(result.next())
            {
                return result.getInt("points");
            }
        }
        catch (SQLException ex)
        {
            Bukkit.getPlayer(sPlayerName).sendMessage("A wacky sort of error occurred!");
            
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    public boolean hasGiftedToday(String sPlayerName)
    {
        try
        {
            PreparedStatement statement = CoolPoints.sqlite.prepare(""
                + "SELECT giftedToday "
                + "FROM player "
                + "WHERE username=? LIMIT 1;");
            
            statement.setString(1, sPlayerName.toLowerCase());
            
            ResultSet result = statement.executeQuery();
            
            if(result.next())
            {
                return result.getInt("giftedToday") > 0;
            }
            else
            {
                return false;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;//Should never reach this point
    }

    public boolean playerGiftPlayer(String takeFrom, String giveTo)
    {
        if(this.getCoolPoints(takeFrom) > 0)
        {
            try
            {            
                PreparedStatement insertUser = CoolPoints.sqlite.prepare(
                        "UPDATE player "
                        + "SET points = points - 1,giftedToday = giftedToday + 1 "
                        + "WHERE username = ?");

                insertUser.setString(1, takeFrom.toLowerCase());

                insertUser.execute();
                
                this.giveCoolPoints(giveTo, 1);
                
                return true;
            }
            catch (SQLException ex)
            {
                Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return false;
    }
    
    public void createProfile(String sPlayerName)
    {
        try
        {
            PreparedStatement statement = CoolPoints.sqlite.prepare(""
                + "SELECT username "
                + "FROM player "
                + "WHERE username=? LIMIT 1;");
            
            statement.setString(1, sPlayerName.toLowerCase());
            
            ResultSet result = statement.executeQuery();
            
            if(!result.next())
            {
                PreparedStatement insertUser = CoolPoints.sqlite.prepare(""
                    + "INSERT INTO player(username,points,giftedToday,receivedAllowanceToday,firstJoined)"
                    + "VALUES(?,1,0,1,strftime('%s', 'now'))");
                
                insertUser.setString(1, sPlayerName.toLowerCase());
                
                insertUser.execute();
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void giveDailyWage(String sPlayerName)
    {
        try
        {
            PreparedStatement statement = CoolPoints.sqlite.prepare(""
                + "SELECT receivedAllowanceToday "
                + "FROM player "
                + "WHERE username=? LIMIT 1;");
            
            statement.setString(1, sPlayerName.toLowerCase());
            
            ResultSet result = statement.executeQuery();
            
            if(result.next())
            {
                System.out.println("did exist");
                if(result.getInt("receivedAllowanceToday") == 0)
                {
                System.out.println("doesnt have todays allowance");
                    PreparedStatement statement2 = CoolPoints.sqlite.prepare(""
                        + "UPDATE player "
                        + "SET points = points+1 "
                        + "WHERE username=? LIMIT 1;");

                    statement2.setString(1, sPlayerName.toLowerCase());

                    statement2.executeQuery();
                }
            }
            else
            {
                createProfile(sPlayerName);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<String, Integer> getTopPlayers(int iTopAmount)
    {
        if(iTopAmount > 50)
        {
            iTopAmount = 50;
        }
        
        try
        {
            PreparedStatement statement = CoolPoints.sqlite.prepare(""
                + "SELECT username,points "
                + "FROM player "
                + "ORDER BY points DESC LIMIT "+iTopAmount+";");
            
            ResultSet result = statement.executeQuery();
            
            HashMap<String,Integer> topPlayers = new HashMap<>();
            
            while(result.next())
            {
                topPlayers.put(result.getString("username"),
                        result.getInt("points"));
            }
            
            return topPlayers;
        }
        catch (Exception ex)
        {
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
