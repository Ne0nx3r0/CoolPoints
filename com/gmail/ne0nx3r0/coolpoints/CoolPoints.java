package com.gmail.ne0nx3r0.coolpoints;

import com.gmail.ne0nx3r0.coolpoints.commands.CoolPointsCommandExecutor;
import com.gmail.ne0nx3r0.coolpoints.listeners.CoolPointsPlayerListener;
import com.gmail.ne0nx3r0.coolpoints.points.PlayerProfileManager;
import java.util.Calendar;
import java.util.logging.Level;
import lib.PatPeter.SQLibrary.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CoolPoints extends JavaPlugin
{
    public static Plugin self;
    public static PlayerProfileManager ppm;
    public static SQLite sqlite;

    @Override
    public void onEnable()
    {        
        CoolPoints.self = this;

// DB Setup
        sqlite = new SQLite(
            getLogger(),
            "CoolPoints",
            "points",
            this.getDataFolder().getAbsolutePath()
        );
        
        try 
        {
            sqlite.open();
        }
        catch(Exception e)
        {
            getLogger().info(e.getMessage());
            getPluginLoader().disablePlugin(this);
        }
        
        if(!sqlite.checkTable("player"))
        {
            sqlite.query("CREATE TABLE player("
                + "username VARCHAR(16) PRIMARY KEY,"
                + "points INT,"
                + "giftedToday INT,"
                + "receivedAllowanceToday INT,"
                + "firstJoined INT"
            + ");");
            
            sqlite.query("CREATE TABLE points_given("
                + "created INT PRIMARY KEY,"
                + "fromName VARCHAR(16) PRIMARY KEY,"
                + "toName VARCHAR(16) PRIMARY KEY,"
                + "amount INT"
            + ");");

            getLogger().info("Cool Points tables created.");
        }
        
        ppm = new PlayerProfileManager();
        
        //register events
        getServer().getPluginManager().registerEvents(new CoolPointsPlayerListener(), this);
        
        //register commands
        getCommand("cp").setExecutor(new CoolPointsCommandExecutor());
        
        //Setup daily point reset thingy
        this.getServer().getScheduler().scheduleSyncRepeatingTask(self,new Runnable()
        {
            @Override 
            public void run()
            {      
                if(Calendar.HOUR_OF_DAY == 0)
                {
                    Bukkit.broadcastMessage("[SERVER] Resetting CoolPoint daily stats!");

                    CoolPoints.ppm.dailyReset();
                }
            }
        }, 
        20*60*60,20*60*60);
    }

    @Override
    public void onDisable()
    {
        sqlite.close();
    }
}