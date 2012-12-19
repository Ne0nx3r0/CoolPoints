package com.gmail.ne0nx3r0.coolpoints.points;

import java.util.HashMap;

public class CoolPointsPlayer
{
    private final String displayName;
    
    private final long firstJoined;
    
    private int points;
    
    //author, note
    private HashMap<String,String> notes;
    
    CoolPointsPlayer(String player, int points, long firstJoined)
    {
        this.displayName = player;
        this.points = points;
        this.firstJoined = firstJoined;
        this.notes = new HashMap<>();
    }
    
    public CoolPointsPlayer(String player,int points,long firstJoined,HashMap<String,String> notes)
    {
        this.displayName = player;
        this.points = points;
        this.firstJoined = firstJoined;
        this.notes = notes;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public int getPoints()
    {
        return this.points;
    }

    public long getFirstJoined()
    {
        return this.firstJoined;
    }
}
