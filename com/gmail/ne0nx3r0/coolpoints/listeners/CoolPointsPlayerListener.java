package com.gmail.ne0nx3r0.coolpoints.listeners;

import com.gmail.ne0nx3r0.coolpoints.CoolPoints;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class CoolPointsPlayerListener implements Listener
{
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        if(CoolPoints.ppm.giveDailyWage(e.getPlayer().getName()))
        {
            e.getPlayer().sendMessage("You have earned a cool point for logging in today!");
        }
    }
}