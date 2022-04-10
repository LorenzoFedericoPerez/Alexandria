package com.generation.model.view;

import java.util.List;

import com.generation.model.entities.Book;
import com.generation.model.entities.User;

public class BookView 
{

    public String renderBook(Book b)
    {
        String res = "";
        res +=  "ISBN: " + b.getIsbn() + ", Author: " + b.getAuthor() + ", Title: " + b.getTitle() + 
            ", Genre: " + b.getGenre() + ", Pages: " + b.getPages() + 
            "\nPrice: " + b.getPrice()+"\n";
        return res;
    }

    public String renderBooks(List<Book> b)
    {
        String res = "";
        for(Book book: b)
        {
            res += renderBook(book)+"----------\n";
        }

        return res;
    }

    public String renderUser(User u)
    {
        String res = "";
        res +=  "SSN: " + u.getSsn().getContent() + ", Name: " + u.getName() + ", Surname: " + u.getSurname() + 
                ", Email: " + u.getEmail().getContent() + ", Date of birth: " + u.getDob() + ", Gender: " + u.getGender() +
                "\nUsername: " + u.getUsername() +"\n";
        return res;
    }

    public String renderUsers(List<User> users)
    {
        String res = "";
        for(User u: users)
            res += renderUser(u);
        
        return res;
    }


    
}
