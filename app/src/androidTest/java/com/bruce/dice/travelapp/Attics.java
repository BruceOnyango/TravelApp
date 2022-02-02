package com.bruce.dice.travelapp;

public class Attics {
    //declare the variable
    private String comment, displayName, profilePhoto, time, date;
    //create a constructor
    public Attics(String comment, String displayName,
                 String profilePhoto, String time, String date) {
        this.comment=comment;
        this.displayName = displayName;
        this.profilePhoto=profilePhoto;
        this.time=time;
        this.date=date;


    }






    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
   public void setComment(String comment){this.comment=comment;}
    public void setTime(String time){
        this.time=time;
    }
    public void setDate(String date){
        this.date=date;
    }
    //public void setPost_comment(String post_comment){this.post_comment=post_comment;}

    //getters
    public String getDisplayName() {
        return displayName;
    }
    public String getProfilePhoto()
    {
        return profilePhoto;
    }
    public String getTime(){
        return time;
    }
    public String getDate(){
        return date;
    }
    public String getComment(){return comment;}

}
