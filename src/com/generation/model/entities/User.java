package com.generation.model.entities;

import com.generation.common.*;

public class User extends Person
{
    
    String username;
    //imported with common.jar
    Password password;

    public User(String ssn, String name, String surname, String email, String dob, String gender, String username, String password) throws Exception
    {
        super(ssn, name, surname, email, dob, gender);
        this.username = username;
        this.password = new Password(password);
        //password encrypted with md5 encryption algorithm
        this.password.encrypt();
    }

    public String getUsername() 
    {
        return username;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public Password getPassword() 
    {
        return password;
    }

    public void setPassword(Password password) 
    {
        this.password = password;
    }

    @Override
    public String getErrors()
    {
        String res = super.getErrors();
        if(username.isBlank() || username==null)
            res+="Invalid username\n";
        
        return res;
    }

    

}
