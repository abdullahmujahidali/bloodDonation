package com.example.bloodbank;

public class User {
    String firstname;
    String lastname;
    String email;
    String address;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    double latitude;
    double longitude;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }


    String pincode;
    String blood;
    public String getAddress() {
        return address;
    }

    public String getPincode() {
        return pincode;
    }




    public User(){

    }

    public User(String firstname, String lastname, String email,String address,String pincode,String blood,double lat,double lang) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address=address;
        this.pincode=pincode;
        this.blood=blood;
        this.latitude = lat;
        this.longitude = lang;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

}
