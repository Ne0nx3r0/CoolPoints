package com.gmail.ne0nx3r0.coolpoints;

import com.gmail.ne0nx3r0.coolpoints.listeners.CoolPointsPlayerListener;
import com.gmail.ne0nx3r0.coolpoints.points.PlayerProfileManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CoolPoints extends JavaPlugin{
    public static Plugin self;
    public static PlayerProfileManager ppm;

    @Override
    public void onEnable()
    {        
        CoolPoints.self = this;
        
        ppm = new PlayerProfileManager();
        
        //register events
        getServer().getPluginManager().registerEvents(new CoolPointsPlayerListener(), this);
    }
}