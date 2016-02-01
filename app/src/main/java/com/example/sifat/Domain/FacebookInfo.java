package com.example.sifat.Domain;

import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by sifat on 10/31/2015.
 */
public class FacebookInfo implements Serializable{
    String lastName, firstName, address, bday, gender;

    public FacebookInfo(String lastName, String firstName, String address, String bday, String gender)
    {
        this.address=address;
        this.bday=bday;
        this.lastName=lastName;
        this.firstName=firstName;
        this.gender=gender;
    }

    public int getGender()
    {
        if(gender.equalsIgnoreCase("male"))
            return 1;
        else if(gender.equalsIgnoreCase("female"))
            return 2;
        else if(gender.equalsIgnoreCase("others"))
            return 3;
        else
            return 0;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAddress() {
        return address;
    }

    public String getBday() {
        return getFormatedBday(bday);
    }

    private String getFormatedBday(String bday) {
        String birthday;
        String[] separated = bday.split("/");
        birthday=separated[2]+"/"+separated[1]+"/"+separated[0];
        return birthday;
    }
}