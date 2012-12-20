package com.gmail.ne0nx3r0.coolpoints.commands;

import com.gmail.ne0nx3r0.coolpoints.CoolPoints;
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
        int iCP = CoolPoints.ppm.getCoolPoints(args[1]);
        if(iCP > -1)
        {
            cs.sendMessage(args[1] + " has "+iCP+"CP");
        }
        else
        {
            cs.sendMessage(ChatColor.RED+"\""+args[1]+"\" not found.");
        }
        
        return true;
    }
    
    private boolean gift(CommandSender cs,String[] args)
    {
        if(!cs.hasPermission("coolpoints.gift"))
        {
            cs.sendMessage("You don't have permisson to use /cp gift");
            return true;
        }
        
        if(args.length == 1 || args[1].equals("?"))
        {
            cs.sendMessage("Usage:");
            cs.sendMessage("/cp gift <player>");
            
            return true;
        }
        
        if(CoolPoints.ppm.getCoolPoints(args[2]) > -1)
        {
            if(!CoolPoints.ppm.hasGiftedToday(cs.getName()))
            {
                CoolPoints.ppm.playerGiftPlayer(cs.getName(),args[2]);
                
                cs.sendMessage(ChatColor.RED+"You gifted "+args[2]+" a cool point!");
            
                if(Bukkit.getPlayer(args[2]) != null)
                {
                    Bukkit.getPlayer(args[2]).sendMessage(cs.getName()+" gifted you a cool point! ("+CoolPoints.ppm.getCoolPoints(args[2]) +"CP total)");
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

    private boolean give(CommandSender cs,String[] args)
    {
        if(!cs.hasPermission("coolpoints.manage"))
        {
            cs.sendMessage("You don't have permisson to use /cp give");
            
            return true;
        }
        
        if(args.length < 3)
        {
            cs.sendMessage("Usage:");
            cs.sendMessage("/cp give <player> <amount>");
            
            return true;
        }
        
        if(CoolPoints.ppm.getCoolPoints(args[2]) > -1)
        {
            int iAmount = 0;
            try
            {
                iAmount = Integer.parseInt(args[3]);
            }
            catch(Exception e)
            {
                cs.sendMessage(ChatColor.RED+"Invalid amount! ("+args[3]+")");
                return true;
            }
            
            int iGiveToTotal = CoolPoints.ppm.giveCoolPoints(args[2],iAmount);
            
            cs.sendMessage(ChatColor.RED+"You gave "+args[2]+" "+iAmount+"CP! "
                    +"("+iGiveToTotal+" total CP)");

            if(Bukkit.getPlayer(args[2]) != null)
            {
                Bukkit.getPlayer(args[2]).sendMessage(cs.getName()+" gave you "+iAmount+"CP! "
                        + "("+iGiveToTotal+" total CP)");
            }
        }
        else
        {
            cs.sendMessage(ChatColor.RED+args[2]+" not found.");
        }
        
        return true;
    }
    
    private boolean take(CommandSender cs,String[] args)
    {
        if(!cs.hasPermission("coolpoints.manage"))
        {
            cs.sendMessage("You don't have permisson to use /cp take");
            
            return true;
        }
        
        if(args.length < 3)
        {
            cs.sendMessage("Usage:");
            cs.sendMessage("/cp take <player> <amount>");
            
            return true;
        }
        
        if(CoolPoints.ppm.getCoolPoints(args[2]) > -1)
        {
            int iAmount = 0;
            try
            {
                iAmount = Integer.parseInt(args[3]);
            }
            catch(Exception e)
            {
                cs.sendMessage(ChatColor.RED+"Invalid amount! ("+args[3]+")");
                return true;
            }
            
            int iTakeFromTotal = CoolPoints.ppm.giveCoolPoints(args[2],iAmount*-1);
            
            cs.sendMessage(ChatColor.RED+"You took "+iAmount+"CP from "+args[2]+"! "
                    +"("+iTakeFromTotal+" total CP)");

            if(Bukkit.getPlayer(args[2]) != null)
            {
                Bukkit.getPlayer(args[2]).sendMessage(cs.getName()+" gave you "+iAmount+"CP! "
                        + "("+iTakeFromTotal+" total CP)");
            }
        }
        else
        {
            cs.sendMessage(ChatColor.RED+args[2]+" not found.");
        }
        
        return true;
    }
}
