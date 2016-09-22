package com.example.bradleycooper.politicslive;

/**
 * Created by Bradley Cooper on 4/29/2016.
 */
public class ExitPoll {
    private int exitPollID;
    private String state;
    private int totalResponders;
    private String candidate;
    private String voterGroup;
    private String voterGroupLabel;
    private int percentageOfVote;

    public ExitPoll(){
        exitPollID = -1;
    }

    public int getExitPollID() {
        return exitPollID;
    }
    public void setExitPollID(int exitPollID) {
        this.exitPollID = exitPollID;
    }
    public String getState() {return state;    }
    public void setState(String state) {
        this.state = state;
    }
    public int getTotalResponders() {return totalResponders;}
    public void setTotalResponders(int totalResponders) {this.totalResponders = totalResponders;}
    public String getCandidate() {
        return candidate;
    }
    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }
    public String getVoterGroup() {
        return voterGroup;
    }
    public void setVoterGroup(String voterGroup) {
        this.voterGroup = voterGroup;
    }
    public int getPercentageOfVote() {
        return percentageOfVote;
    }
    public void setPercentageOfVote(int percentageOfVote) {this.percentageOfVote = percentageOfVote;    }
    public String getVoterGroupLabel() {        return voterGroupLabel;    }

    public void setVoterGroupLabel(String voterGroupLabel) {this.voterGroupLabel = voterGroupLabel;    }

}
