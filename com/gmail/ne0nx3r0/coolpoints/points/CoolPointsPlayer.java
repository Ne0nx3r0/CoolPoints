package com.gmail.ne0nx3r0.coolpoints.points;

import java.util.HashMap;

public class CoolPointsPlayer
{
    private final String playerName;
    
    private final long firstJoined;
    
    private int points;
    
    //author, note
    private HashMap<String,String> notes;
    
    public CoolPointsPlayer(String player,int points,long firstJoined,HashMap<String,String> notes)
    {
        this.playerName = player;
        this.points = points;
        this.firstJoined = firstJoined;
        this.notes = notes;
    }
}
