package com.gmail.ne0nx3r0.coolpoints.commands;

import com.gmail.ne0nx3r0.coolpoints.CoolPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoolPointsCommandExecutor implements CommandExecutor 
{
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args)
    {
        if(args.length == 0)
        {
            cs.sendMessage("Usage:");
            
            cs.sendMessage("---------------------");
            cs.sendMessage(ChatColor.AQUA+"You have "+CoolPoints.ppm.getCoolPoints(cs.getName())+"CP");
            cs.sendMessage("---------------------");
            
            return true;
        }
        else if(args.length == 3)
        {
            if(args[0].equalsIgnoreCase("send"))
            {
                if(cs.hasPermission("coolpoints.send"))
                {
                    int iSenderCP = CoolPoints.ppm.getCoolPoints(cs.getName());
                    int iSendAmount = 0;
                    
                    try
                    {
                        iSendAmount = Integer.parseInt(args[2]);
                    }
                    catch(Exception e)
                    {
                        cs.sendMessage(ChatColor.RED+"Invalid CP amount.");
                        cs.sendMessage("Usage: /cp send <username> <amount>");
                        return true;
                    }
                    
                    if(iSenderCP >= iSendAmount)
                    {
                        get CP receiver,
                        take sender points
                        give receiver points
                        confirmation message on both ends
                    }
                    else
                    {
                        cs.sendMessage(ChatColor.RED+"You only have "+iSenderCP+"CP.");
                        return true;
                    }
                }
                else
                {
                    cs.sendMessage(ChatColor.RED+"You don't have permission to send CP.");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("give"))
            {

            }
            else if(args[0].equalsIgnoreCase("take"))
            {
                
            }
        }
        else if(args.length == 1)
        {
            for(Player p : Bukkit.getOnlinePlayers())
            {
                if(p.getName().toLowerCase().contains(args[1].toLowerCase()))
                {
                    cs.sendMessage("---------------------");
                    cs.sendMessage(ChatColor.AQUA+p.getName()+" has "+CoolPoints.ppm.getCoolPoints(cs.getName())+"CP");
                    cs.sendMessage("---------------------");
                    
                    return true;
                }
            }
            
            cs.sendMessage(ChatColor.RED+"\""+args[1]+"\" not found.");
            
            return true;
        }
        
        return false;
    }//End onCommand
}
