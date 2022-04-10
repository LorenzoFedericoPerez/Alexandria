package com.generation.model.entities;

//functional interface java8, one abstract method, zero or more concrete method
public interface Validable 
{
    /**
     * Checks if objects have errors (abstract method getErrors()) and returns if they are valid or not.
     * @return true or false
     */
    default public boolean isValid()
    {
        return getErrors().length()>0 ? false : true;
    }

    public String getErrors();
}
