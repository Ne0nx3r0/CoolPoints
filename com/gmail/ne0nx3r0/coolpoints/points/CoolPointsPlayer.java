package com.gmail.ne0nx3r0.coolpoints.points;

import java.util.ArrayList;

public class CoolPointsPlayer
{
    private final String displayName;
    
    private final long firstJoined;
    
    private int points;
    
    //author, note
    private ArrayList<PlayerNote> notes;
    
    CoolPointsPlayer(String player, int points, long firstJoined)
    {
        this.displayName = player;
        this.points = points;
        this.firstJoined = firstJoined;
        this.notes = new ArrayList<>();
    }
    
    public CoolPointsPlayer(String player,int points,long firstJoined,ArrayList<PlayerNote> notes)
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

    ArrayList<PlayerNote> getNotes()
    {
        return this.notes;
    }
}
