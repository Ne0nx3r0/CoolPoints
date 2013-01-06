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
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(args.length == 0)
        {
            return this.usage(cs,args);
        }

        switch(args[0])
        {
            case "createProfileWith":
                return this.createProfileWith(cs, args);
            case "reset":
                return this.resetPoints(cs, args);
            case "gift":
                return this.gift(cs, args);
            case "give":
                return this.give(cs, args);
            case "points":
            case "p":
                return this.points(cs, args);
            case "take":
                return this.take(cs, args);
            case "top":
                return this.top(cs, args);
            case "rank":
                return this.rank(cs, args);
        }
        
        if(args.length == 1)
        {
            return this.points(cs, args);
        }
        
        return false;
    }
    
    public boolean usage(CommandSender cs,String[] args)
    {
        cs.sendMessage("Usage:");
        cs.sendMessage("/cp <player> - Get player CP");
        cs.sendMessage("/cp gift <player> - give a player one of your CP");
        cs.sendMessage("/cp top [amount] - Show top players");
        cs.sendMessage("/cp rank <player> - Show player rank");
        
        if(cs.hasPermission("coolpoints.manage"))
        {
            cs.sendMessage("/cp give <player> <amount> - Give CP");
            cs.sendMessage("/cp take <player> <amount> - Take away CP");
            cs.sendMessage("/cp reset - Reset daily points");
            cs.sendMessage("/cp createProfileWith <player> <amount> - Create a profile artificially");
        }
        
        cs.sendMessage(ChatColor.GOLD+"You have "+CoolPoints.ppm.getCoolPoints(cs.getName())+"CP");
            
        return true;
    }

    private boolean points(CommandSender cs,String[] args)
    {
        int iCP = CoolPoints.ppm.getCoolPoints(args[0]);
        if(iCP > -1)
        {
            cs.sendMessage(ChatColor.GOLD+args[0] + " has "+iCP+"CP");
        }
        else
        {
            cs.sendMessage(ChatColor.RED+"\""+args[0]+"\" not found.");
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
        
        if(!(cs instanceof Player))
        {
            cs.sendMessage("This command not allowed from the console.");
            return true;
        }
        
        if(args.length == 1 || args[1].equals("?"))
        {
            cs.sendMessage("Usage:");
            cs.sendMessage("/cp gift <player>");
            
            return true;
        }
        
        if(CoolPoints.ppm.getCoolPoints(args[1]) > -1)
        {
            if(args[1].equalsIgnoreCase(cs.getName()))
            {
                cs.sendMessage(ChatColor.RED+"You can't gift yourself!");
                
                return true;
            }
            
            if(cs instanceof Player && CoolPoints.ppm.getCoolPoints(cs.getName()) < 1)
            {
                cs.sendMessage(ChatColor.RED+"You don't have enough cool points! "
                        + "("+CoolPoints.ppm.getCoolPoints(cs.getName())+"CP left)");
                
                return true;
            }
            
            if(!CoolPoints.ppm.hasGiftedToday(cs.getName()))
            {
                if(CoolPoints.ppm.playerGiftPlayer(cs.getName(),args[1]))
                {
                    cs.sendMessage(ChatColor.RED+"You gifted "+args[1]+" a cool point!");

                    if(Bukkit.getPlayer(args[1]) != null)
                    {
                        Bukkit.getPlayer(args[1]).sendMessage(cs.getName()+" gifted you a cool point! ("+CoolPoints.ppm.getCoolPoints(args[1]) +"CP total)");
                    }
                    
                    Bukkit.broadcast(ChatColor.GOLD+cs.getName() + " gifted a CP to "+args[1], "coolpoints.alerts");
                }
                else
                {
                    cs.sendMessage("Something odd occurred.");
                }
            }
            else
            {
                cs.sendMessage(ChatColor.RED+"You've already gifted someone today!");
            }
        }
        else
        {
            cs.sendMessage(ChatColor.RED+args[1]+" not found.");
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
        
        if(CoolPoints.ppm.getCoolPoints(args[1]) > -1)
        {
            int iAmount = 0;
            try
            {
                iAmount = Integer.parseInt(args[2]);
            }
            catch(Exception e)
            {
                cs.sendMessage(ChatColor.RED+"Invalid amount! ("+args[2]+")");
                return true;
            }
            
            int iGiveToTotal = CoolPoints.ppm.giveCoolPoints(args[1],iAmount);
            
            cs.sendMessage(ChatColor.GOLD+"You gave "+args[1]+" "+iAmount+"CP! "
                    +"("+iGiveToTotal+" total CP)");

            if(Bukkit.getPlayer(args[1]) != null)
            {
                Bukkit.getPlayer(args[1]).sendMessage(cs.getName()+" gave you "+iAmount+"CP! "
                        + "("+iGiveToTotal+" total CP)");
            }
            
            Bukkit.broadcast(ChatColor.GOLD+cs.getName() + " gave "+iAmount+"CP to "+args[1], "coolpoints.alerts");
        }
        else
        {
            cs.sendMessage(ChatColor.RED+args[1]+" not found.");
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
        
        if(CoolPoints.ppm.getCoolPoints(args[1]) > -1)
        {
            int iAmount = 0;
            try
            {
                iAmount = Integer.parseInt(args[2]);
            }
            catch(Exception e)
            {
                cs.sendMessage(ChatColor.RED+"Invalid amount! ("+args[2]+")");
                return true;
            }
            
            int iTakeFromTotal = CoolPoints.ppm.giveCoolPoints(args[1],iAmount*-1);
            
            cs.sendMessage(ChatColor.RED+"You took "+iAmount+"CP from "+args[1]+"! "
                    +"("+iTakeFromTotal+" total CP)");

            if(Bukkit.getPlayer(args[1]) != null)
            {
                Bukkit.getPlayer(args[1]).sendMessage(ChatColor.RED+cs.getName()+" took "+iAmount+"CP! "
                        + " from you! ("+iTakeFromTotal+" total CP)");
            }
            
            Bukkit.broadcast(ChatColor.GOLD+cs.getName() + " took "+iAmount+"CP from "+args[1], "coolpoints.alerts");
        }
        else
        {
            cs.sendMessage(ChatColor.RED+args[1]+" not found.");
        }
        
        return true;
    }

    private boolean top(CommandSender cs, String[] args)
    {
        if(!cs.hasPermission("coolpoints.top"))
        {
            cs.sendMessage(ChatColor.RED+"You do not have permission to see the top users.");
            
            return true;
        }
        
        int iTopAmount = 10;
        
        if(args.length == 2)
        {
            try
            {
                iTopAmount = Integer.parseInt(args[1]);
            }
            catch(Exception e)
            {
                cs.sendMessage(ChatColor.RED+args[1]+" is not a valid amount!");
                
                return true;
            }
        }
        
        if(iTopAmount > 50)
        {
            iTopAmount = 50;
        }

        cs.sendMessage("--------------------------");
        
        cs.sendMessage(ChatColor.GOLD+"    Top "+iTopAmount+" players");
        
        cs.sendMessage("--------------------------");
        
        int iRank = 0;
        for(String sPacked : CoolPoints.ppm.getTopPlayers(iTopAmount))
        {
            iRank++;
            
            String[] sValues = sPacked.split(",");
            
            cs.sendMessage("    #"+iRank+" "+ChatColor.BLUE+sValues[0] + ChatColor.WHITE+" : " + ChatColor.GREEN+sValues[1]);
        }
        
        cs.sendMessage("--------------------------");
        
        return true;
    }

    private boolean rank(CommandSender cs, String[] args)
    {
        if(!cs.hasPermission("coolpoints.rank"))
        {
            cs.sendMessage(ChatColor.RED+"You do not have permission to use this command.");
        }
        else if(args.length == 1)
        {
            cs.sendMessage(ChatColor.GOLD+"You are ranked #"+CoolPoints.ppm.getRank(cs.getName()));
        }
        else if(CoolPoints.ppm.getCoolPoints(args[1]) > -1)
        {
            cs.sendMessage(ChatColor.GOLD+args[1]+ " is ranked #"+CoolPoints.ppm.getRank(args[1]));
        }
        else
        {
            cs.sendMessage(ChatColor.RED+args[1]+" not found");
        }
        
        return true;
    }

    private boolean createProfileWith(CommandSender cs, String[] args)
    {
        if(cs instanceof Player)
        {
            cs.sendMessage(ChatColor.RED+"Console-only command.");
        }
        else if(args.length == 3 && !args[2].equals("0"))
        {
            System.out.println("Creating profile for "+args[1]+" with "+args[2]+" points.");
            
            CoolPoints.ppm.createProfile(args[1]);
            
            CoolPoints.ppm.giveCoolPoints(args[1], Integer.parseInt(args[2]));
        }
        
        return true;
    }

    private boolean resetPoints(CommandSender cs, String[] args)
    {
        if(cs instanceof Player)
        {
            cs.sendMessage(ChatColor.RED+"Console-only command.");
        }
        else
        {
            CoolPoints.ppm.dailyReset();
        }
        
        return true;
    }
}
