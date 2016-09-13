package com.example.bradleycooper.politicslive;

/**
 * Created by Bradley Cooper on 9/12/2016.
 */
public class Event {
    private int eventID;
    private String name;
    private String description;
    private String date;        //  MMDDYYY
    private String time;        //  HHMM
    private String channel;
    private byte[] channelPic;
    private String moderator;
    private byte[] moderatorPic;
    private String site;
    private String location;

    public Event(){
        eventID = -1;
    }




    /*Getters and Setters */
    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public byte[] getChannelPic() {
        return channelPic;
    }

    public void setChannelPic(byte[] channelPic) {
        this.channelPic = channelPic;
    }

    public String getModerator() {
        return moderator;
    }

    public void setModerator(String moderator) {
        this.moderator = moderator;
    }

    public byte[] getModeratorPic() {
        return moderatorPic;
    }

    public void setModeratorPic(byte[] moderatorPic) {
        this.moderatorPic = moderatorPic;
    }
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }






}
