package com.example.sifat.Domain;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by sifat on 10/31/2015.
 */
public class FacebookInfo implements Serializable{
    String lastName,firstName,email,address,bday,gender;

    public FacebookInfo(String email,String lastName,String firstName,String address,String bday,String gender)
    {
        this.email=email;
        this.address=address;
        this.bday=bday;
        this.lastName=lastName;
        this.firstName=firstName;
        this.gender=gender;
    }

    public String getGender()
    {
        return gender;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getBday() {
        return bday;
    }
}