package com.example.bradleycooper.politicslive;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class Candidate {
    private int candidateID;
    private String candidateName;
    private String candidateDescription;
    private int numberOfVotes;
    private byte[] squarePicture;
    private byte[] widePicture;
    private String party;

    public Candidate(){
        candidateID = -1;
        numberOfVotes = 0;
    }
    public int getCandidateID() {
        return candidateID;
    }
    public void setCandidateID(int candidateID) {
        this.candidateID = candidateID;
    }
    public String getCandidateName() {
        return candidateName;
    }
    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }
    public String getCandidateDescription() {
        return candidateDescription;
    }
    public void setCandidateDescription(String candidateDescription) {
        this.candidateDescription = candidateDescription;
    }
    public int getNumberOfVotes() {
        return numberOfVotes;
    }
    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }
    public byte[] getSquarePicture(){
        return squarePicture;
    }
    public void setSquarePicture(byte[] array){
        squarePicture = array;
    }
    public byte[] getWidePicture() {
        return widePicture;
    }
    public void setWidePicture(byte[] widePicture) {
        this.widePicture = widePicture;
    }
    public String getParty() {
        return party;
    }
    public void setParty(String party) {
        this.party = party;
    }
}
