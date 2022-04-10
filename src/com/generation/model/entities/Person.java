package com.generation.model.entities;

import com.generation.common.*;
import java.util.Calendar;

public abstract class Person implements Validable
{
    SSN ssn;
    Email email;
    String name, surname, dob, gender;
    

    public Person(String ssn, String name, String surname, String email, String dob, String gender)
    {
        this.ssn =new SSN(ssn);
        this.name = name;
        this.surname = surname;
        this.email = new Email(email);
        this.dob = dob;
        this.gender = gender;
    }

    public SSN getSsn() {
        return ssn;
    }

    public void setSsn(SSN ssn) {
        this.ssn = ssn;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets an instance of Calendar, gets the current year, the year the person was born and returns the age
     * @return int age of the person
     */
    public int getAge()
    {
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(dob.substring(6,10));
        int currentYear = calendar.get(Calendar.YEAR);
        return currentYear - year;
    }

    @Override
    public String getErrors() 
    {
        String res = "";
        res += ssn.getErrors();
        res += email.getErrors();
        if(name==null || name.isBlank())
            res += "Invalid name\n";

        if(surname==null || surname.isBlank())
            res += "Invalid surname\n";

        if(name==null || name.isBlank())
            res += "Invalid name\n";

        if(dob==null || dob.isBlank() || dob.length()>10)
            res +="Invalid date of birth\n";

        if(!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F"))
            res +="Invalid gender\n";
        
        return res;
    }

}
