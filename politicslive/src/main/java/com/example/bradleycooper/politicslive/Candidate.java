package com.example.bradleycooper.politicslive;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class Candidate {
    private int candidateID;
    private String candidateName;
    private String candidateDescription;
    private int numberOfVotes;
    private String squarePicture;
    private String widePicture;
    private String party;
    private int delegateCount;
    private float huffPercentageOfVote;
    private float huffPercentageOfVoteGeneral;
    private float huffFavorableRating;
    private float huffUnfavorableRating;
    private String site;
    private String email;
    private String twitter;
    private String electionType;
    private String typeOfVoter;



    public Candidate(){
        candidateID = -1;
        numberOfVotes = 0;
        typeOfVoter = "";
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
    public String getSquarePicture(){
        return squarePicture;
    }
    public void setSquarePicture(String array){
        squarePicture = array;
    }
    public String getWidePicture() {
        return widePicture;
    }
    public void setWidePicture(String widePicture) {
        this.widePicture = widePicture;
    }
    public String getParty() {
        return party;
    }
    public void setParty(String party) {
        this.party = party;
    }
    public int getDelegateCount() {return delegateCount;}
    public void setDelegateCount(int delegateCount) {this.delegateCount = delegateCount;}
    public float getHuffPercentageOfVote() {return huffPercentageOfVote;}
    public void setHuffPercentageOfVote(float huffPercentageOfVote) {this.huffPercentageOfVote = huffPercentageOfVote;}
    public float getHuffPercentageOfVoteGeneral() {return huffPercentageOfVoteGeneral;}
    public void setHuffPercentageOfVoteGeneral(float huffPercentageOfVoteGeneralNew) {
        this.huffPercentageOfVoteGeneral = huffPercentageOfVoteGeneralNew;
    }

    public float getHuffFavorableRating() {return huffFavorableRating;   }
    public void setHuffFavorableRating(float huffFavorableRating) {this.huffFavorableRating = huffFavorableRating;}
    public float getHuffUnfavorableRating() {return huffUnfavorableRating;}
    public void setHuffUnfavorableRating(float huffUnfavorableRating) {this.huffUnfavorableRating = huffUnfavorableRating;}

    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTwitter() {
        return twitter;
    }
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
        this.electionType = electionType;
    }

    public String getTypeOfVoter() {
        return typeOfVoter;
    }

    public void setTypeOfVoter(String typeOfVoter) {
        this.typeOfVoter = typeOfVoter;
    }

}
