package com.example.bradleycooper.politicslive.dataobjects;

/**
 * Created by Bradley Cooper on 4/28/2016.
 */
public class Poll {
    private int pollID;
    private String name;
    private String description;
    private String type;
    private String location;
    private String totalResponders;
    private String whoPollIsAbout;
    private String whatPollIsAbout;
    private String result;
    private String source;
    private String sourceURL;


    public int getPollID() {
        return pollID;
    }
    public void setPollID(int pollID) {
        this.pollID = pollID;
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
    public void setDescription(String description) {this.description = description;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) {this.location = location;}
    public String getTotalResponders() {return totalResponders;}
    public void setTotalResponders(String totalResponders) {this.totalResponders = totalResponders;}
    public String getWhoPollIsAbout() {return whoPollIsAbout;    }
    public void setWhoPollIsAbout(String whoPollIsAbout) {this.whoPollIsAbout = whoPollIsAbout; }
    public String getWhatPollIsAbout() {return whatPollIsAbout;}
    public void setWhatPollIsAbout(String whatPollIsAbout) {this.whatPollIsAbout = whatPollIsAbout; }
    public String getResult() {return result;}
    public void setResult(String result) { this.result = result; }
    public String getSource() {   return source;}
    public void setSource(String source) { this.source = source;    }
    public String getSourceURL() { return sourceURL;   }
    public void setSourceURL(String sourceURL) { this.sourceURL = sourceURL; }
}
