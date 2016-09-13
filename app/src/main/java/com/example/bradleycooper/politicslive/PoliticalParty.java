package com.example.bradleycooper.politicslive;

/**
 * Created by Bradley Cooper on 9/12/2016.
 */
public class PoliticalParty {
    private int politicalPartyID;
    private String name;
    private String description;
    private byte[] squarePicture;
    private byte[] widePicture;
    private int numberOfHouseSeats;
    private int numberOfSenateSeats;
    private float huffFavorableRating;
    private float huffUnfavorableRating;
    private String site;
    private String email;
    private String twitter;
    private float percentageOfHouseVote;


    public PoliticalParty(){
        politicalPartyID = -1;
        percentageOfHouseVote = 0;
    }

    public int getPoliticalPartyID() {
        return politicalPartyID;
    }

    public void setPoliticalPartyID(int politicalPartyID) {
        this.politicalPartyID = politicalPartyID;
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

    public byte[] getSquarePicture() {
        return squarePicture;
    }

    public void setSquarePicture(byte[] squarePicture) {
        this.squarePicture = squarePicture;
    }

    public byte[] getWidePicture() {
        return widePicture;
    }

    public void setWidePicture(byte[] widePicture) {
        this.widePicture = widePicture;
    }

    public int getNumberOfHouseSeats() {
        return numberOfHouseSeats;
    }

    public void setNumberOfHouseSeats(int numberOfHouseSeats) {
        this.numberOfHouseSeats = numberOfHouseSeats;
    }

    public int getNumberOfSenateSeats() {
        return numberOfSenateSeats;
    }

    public void setNumberOfSenateSeats(int numberOfSenateSeats) {
        this.numberOfSenateSeats = numberOfSenateSeats;
    }

    public float getHuffFavorableRating() {
        return huffFavorableRating;
    }

    public void setHuffFavorableRating(float huffFavorableRating) {
        this.huffFavorableRating = huffFavorableRating;
    }

    public float getHuffUnfavorableRating() {
        return huffUnfavorableRating;
    }

    public void setHuffUnfavorableRating(float huffUnfavorableRating) {
        this.huffUnfavorableRating = huffUnfavorableRating;
    }

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

    public float getPercentageOfHouseVote() {
        return percentageOfHouseVote;
    }

    public void setPercentageOfHouseVote(float percentageOfHouseVote) {
        this.percentageOfHouseVote = percentageOfHouseVote;
    }


}
