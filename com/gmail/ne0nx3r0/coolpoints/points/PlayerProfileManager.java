package com.gmail.ne0nx3r0.coolpoints.points;

import com.gmail.ne0nx3r0.coolpoints.CoolPoints;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        System.out.println("CP: Giving "+sPlayerName +" "+iAmount);
        
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
            
                result.close(); 
                
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
                int giftedToday = result.getInt("giftedToday");
                
                result.close();
            
                return giftedToday > 0;
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
        System.out.println("CP: "+takeFrom+" gifted "+giveTo);
        
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
                result.close();
                
                PreparedStatement insertUser = CoolPoints.sqlite.prepare(""
                    + "INSERT INTO player(username,points,giftedToday,receivedAllowanceToday,firstJoined)"
                    + "VALUES(?,1,0,1,strftime('%s', 'now'))");
                
                insertUser.setString(1, sPlayerName.toLowerCase());
                
                insertUser.execute();
            }
            else
            {
                result.close();
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean giveDailyWage(String sPlayerName)
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
                if(result.getInt("receivedAllowanceToday") == 0)
                {
                    System.out.println("CP: Giving "+sPlayerName +" their daily point");
                    PreparedStatement statement2 = CoolPoints.sqlite.prepare(""
                        + "UPDATE player "
                        + "SET points = points+1,receivedAllowanceToday=1 "
                        + "WHERE username=?");

                    statement2.setString(1, sPlayerName.toLowerCase());

                    statement2.execute();
                
                    return true;
                }
                
                result.close();
                
                return false;
            }
            else
            {
                result.close();
                
                createProfile(sPlayerName);
                
                return true;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    public ArrayList<String> getTopPlayers(int iTopAmount)
    {
        try
        {
            PreparedStatement statement = CoolPoints.sqlite.prepare(""
                + "SELECT username,points "
                + "FROM player "
                + "ORDER BY points DESC,username ASC LIMIT "+iTopAmount+";");
            
            ResultSet result = statement.executeQuery();
            
            ArrayList<String> topPlayers = new ArrayList<>();
            
            while(result.next())
            {
                topPlayers.add(result.getString("username")+","+
                        result.getInt("points"));
            }
            
            result.close();
            
            return topPlayers;
        }
        catch (Exception ex)
        {
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public void dailyReset()
    {
        CoolPoints.self.getLogger().log(Level.INFO, "Resetting cool points!");
                    
        try
        {
            PreparedStatement updatePoints = CoolPoints.sqlite.prepare("UPDATE player SET giftedToday = 0,receivedAllowanceToday = 0;");  
            updatePoints.execute();
        }
        catch(SQLException ex)
        {
            Bukkit.broadcastMessage("[SERVER] I need an adult!!! I need an admin! Something Happened!");
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getRank(String sPlayerName)
    {
        try
        {
            PreparedStatement statement = CoolPoints.sqlite.prepare(""
                + "SELECT COUNT(username) as rank "
                + "FROM player "
                + "WHERE points > " + this.getCoolPoints(sPlayerName)+";");
            
            ResultSet result = statement.executeQuery();

            if(result.next())
            {
                int iRank = result.getInt("rank")+1;
                
                result.close();
                
                return iRank;
            }
            
            result.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(PlayerProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }
}
