package com.generation.controller.main;

import static com.generation.common.Console.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.generation.model.dao.BookDAOcsv;
import com.generation.model.entities.Book;
import com.generation.model.view.BookView;

public class Main {

    static BookDAOcsv dao;
    static BookView view;

    public static void main(String[] args) throws NoSuchAlgorithmException 
    {
        print("Welcome to the Alexandria Library");
        String res = "", cmd = "";
        view = new BookView();

        try
        {
            dao = new BookDAOcsv("books.csv", "users.csv");
        }
        catch(Exception e)
        {
            print("Files not found");
            System.exit(1);
        }
        

        boolean login = false;
        do
        {
            print("Insert your credentials: (write \"stop\" to quit the program.)");
            String username = ask("Insert username: ");
            if(username.equalsIgnoreCase("stop"))
            {
                print("Bye");
                System.exit(1);
            }

            String password = ask("Insert password: ");
            login = dao.login(username, password);
            if(login==false)
                print("Wrong credentials, try again.\n");
        }
        while(login==false);

        
        print("Welcome to the library.\n"); 

        do
        {
            cmd = ask
            (
                "Insert a command:\n"                                       +
                "1 to see all books\n"                               +
                "2 to see all books of a particular genre\n"  +
                "3 to search for a particular isbn\n"           +
                "4 to insert a new book into the library\n"        +
                "5 to delete a book from the library\n"
            );

            switch(cmd)
            {
                case "1":
                    res = _getBooks();
                break;

                case "2":
                    res = _getBooksByGenre();
                break;

                case "3":
                    res = _getBookByIsbn();
                break;

                case "4":
                    res = _insertBook();
                break;

                case "5":
                    res = _deleteBook();
                break;

                case "bye":
                    res = "Bye.";
                break;

                default:
                    res = "Invalid Command, try again";
            }

            print(res);
        }
        while(res!="Bye.");
        
    }


    private static String _getBooks() 
    {
        List<Book> res = dao.getBooks();
        return res.size()>0 ?
            view.renderBooks(res)
            :
            "Couldn't find any book.";
    }


    private static String _getBooksByGenre() 
    {
        String genre = ask("Insert genre: ");
        List<Book> res = dao.getBooks(genre);
        return res!=null ?
                view.renderBooks(res)
                :
                "Couldn't find any book with the genre "+genre+", try again.";
    }

    private static String _getBookByIsbn() 
    {
        String isbn = ask("Insert isbn: ");
        Book res = dao.getBook(isbn);
        return res!=null ?
                view.renderBook(res)
                :
                "Couldn't find any book with the isbn "+isbn+", try again.";
    }


    private static String _insertBook() 
    {
        try
        {
            String isbn = ask("Insert isbn: ");
            String author = ask("Insert author: ");
            String title = ask("Insert title: ");
            String genre = ask("Insert genre: ");
            int pages = askInt("Insert pages: ");
            double price = _askDouble("Insert price: ");

            return dao.insert(isbn, author, title, genre, pages, price);
        }
        catch(NumberFormatException e)
        {
            return "Error during the input, try again";
        }
        
    }

    private static String _deleteBook() 
    {
        String isbn = ask("Insert isbn: ");
        return dao.delete(isbn);
    }

    private static double _askDouble(String ask)
    {
        System.out.print(ask);
        Scanner dr = new Scanner(System.in);
        double number = Double.parseDouble(dr.nextLine());
        dr.close();
        return number;
    }
    
}
