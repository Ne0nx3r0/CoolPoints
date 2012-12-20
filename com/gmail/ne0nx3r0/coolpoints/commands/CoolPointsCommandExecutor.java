package com.gmail.ne0nx3r0.coolpoints.commands;

import com.gmail.ne0nx3r0.coolpoints.CoolPoints;
import com.gmail.ne0nx3r0.coolpoints.points.CoolPointsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CoolPointsCommandExecutor implements CommandExecutor 
{
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args)
    {
        if(args.length == 0)
        {
            return this.usage(cs,args);
        }
        else
        {
            try
            {
                return (Boolean) Class.forName("CoolPointsCommandExecutor")
                        .getMethod(args[0].toLowerCase()).invoke(this,cs,args);
            }
            catch(Exception e)
            {
                if(args.length == 1)
                {
                    return this.points(cs,args);
                }
            }
        }
        
        return false;
    }
    
    public boolean usage(CommandSender cs,String[] args)
    {
        cs.sendMessage("Usage:");
        cs.sendMessage("/cp <player> - Get player CP");
        cs.sendMessage("/cp gift <player> - give a player a CP");
        cs.sendMessage("You have "+CoolPoints.ppm.getCoolPoints(cs.getName())+"CP");
            
        return true;
    }

    private boolean points(CommandSender cs,String[] args)
    {
        CoolPointsPlayer cpp = CoolPoints.ppm.getCoolPointsPlayer(cs.getName());
        
        if(cpp != null)
        {
            cs.sendMessage(cpp.getDisplayName() + " has "+cpp.getPoints()+"CP");
        }
        else
        {
            cs.sendMessage(ChatColor.RED+"\""+args[1]+"\" not found.");
        }
        
        return true;
    }
    
    private boolean gift(CommandSender cs,String[] args)
    {
        if(args.length == 1)
        {
            cs.sendMessage("Usage:");
            cs.sendMessage("/cp gift <player>");
            
            return true;
        }
        
        if(CoolPoints.ppm.getCoolPointsPlayer(args[2]) != null)
        {
            if(CoolPoints.ppm.playerGiftPlayer(cs.getName(),args[2]))
            {
                cs.sendMessage(ChatColor.RED+"You gifted "+args[2]+" a cool point!");
            
                if(Bukkit.getPlayer(args[2]) != null)
                {
                    Bukkit.getPlayer(args[2]).sendMessage(cs.getName()+" gifted you a cool point!");
                }
            }
            else
            {
                cs.sendMessage(ChatColor.RED+"You've already gifted someone today!");
            }
        }
        else
        {
            cs.sendMessage(ChatColor.RED+args[2]+" not found.");
        }
        
        return true;
    }
}
