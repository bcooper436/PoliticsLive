package com.example.bradleycooper.politicslive;

/**
 * Created by Bradley Cooper on 4/4/2016.
 */
public class User {
    private int userID;
    private String displayName;
    private String userName;
    private String password;
    private String partyAffiliation;
    private int age;
    private String gender;
    private String chosenDemocrat;
    private String chosenRepublican;
    private byte[] profilePicture;

    public User(){
        userID = -1;
        chosenDemocrat = "";
        chosenRepublican = "";
    }

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPartyAffiliation() {
        return partyAffiliation;
    }
    public void setPartyAffiliation(String partyAffiliation) {
        this.partyAffiliation = partyAffiliation;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getChosenDemocrat() {
        return chosenDemocrat;
    }
    public void setChosenDemocrat(String chosenDemocrat) {
        this.chosenDemocrat = chosenDemocrat;
    }
    public String getChosenRepublican() {
        return chosenRepublican;
    }
    public void setChosenRepublican(String chosenRepublican) {
        this.chosenRepublican = chosenRepublican;
    }
    public byte[] getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

}
