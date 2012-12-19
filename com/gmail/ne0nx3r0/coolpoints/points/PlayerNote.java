package com.gmail.ne0nx3r0.coolpoints.points;

class PlayerNote
{
    private final long created;
    private final String author;
    private final String text;
    
    PlayerNote(long created,String author,String text)
    {
        this.created = created;
        this.author = author;
        this.text = text;
    }

    long getCreated()
    {
        return created;
    }

    String getAuthor()
    {
        return author;
    }

    String getText()
    {
        return text;
    }
}
