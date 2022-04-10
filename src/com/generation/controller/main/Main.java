package com.generation.controller.main;

import static com.generation.common.Console.*;

import java.util.List;

import com.generation.model.dao.BookDAOcsv;
import com.generation.model.entities.Book;
import com.generation.model.entities.User;
import com.generation.model.view.BookView;

public class Main {

    static BookDAOcsv dao;
    static BookView view;
    static User user;

    public static void main(String[] args) throws Exception 
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
            print("Files not found.");
            System.exit(1);
        }
        
        
        do
		{
            print("Insert your credentials: (write \"bye\" to quit the program.)");
			String username = ask("Insert username: ");
			if(username.equalsIgnoreCase("bye")) 
			{
				print("Goodbye");
				System.exit(1);
			}
			String password = ask("Insert password");
			user = dao.login(username,password);

			if(user==null)
			{
				print("Wrong credentials, try again.\n");
			}
		}while(user==null);
        
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
                "5 to delete a book from the library\n"             +
                "6 to see your data\n"                                +
                "7 to see insert an user\n"                                +
                "8 to see delete an user\n"                                +
                "bye to close the program\n"
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

                case "6":
                    res = _viewUser();
                break;

                case "7":
                    res = _InsertUser();
                break;

                case "8":
                    res = _deleteUser();
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

    private static String _deleteUser() throws Exception 
    {
        String loginUsername = user.getUsername();
        String username = ask("Insert deleting user's username: ");
        return dao.deleteUser(loginUsername, username);
    }

    private static String _InsertUser() throws Exception 
    {
        String ssn = ask("Insert ssn");
        String name = ask("Insert name: ");
        String surname = ask("Insert surname: ");
        String email = ask("Insert email: ");
        String dob = ask("Insert date of birth (xx/xx/xxxx format): ");
        String gender = ask("Insert gender (M or F): ");
        String username = ask("Insert username: ");
        String password = ask("Insert password (Min. 1 upper case char, 1 lower case char, 1 number and 1 special char; 5 to 20 chars): ");
        return dao.insertUser(ssn, name, surname, email, dob, gender, username, password);
    }

    private static String _viewUser() 
    {
        return view.renderUser(user);
    }


    private static String _getBooks() 
    {
        List<Book> res = dao.getBooks();
        return !res.isEmpty() ?
            view.renderBooks(res)
            :
            "Couldn't find any book.";
    }


    private static String _getBooksByGenre() 
    {
        String genre = ask("Insert genre: ");
        List<Book> res = dao.getBooks(genre);
        return !res.isEmpty() ?
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

    //when this terminates program closes and get java.util.NoSuchElementException: No line found
    private static String _insertBook()
    { 
        try 
        {
            String isbn = ask("Insert isbn: ");
            String author = ask("Insert author: ");
            String title = ask("Insert title: ");
            String genre = ask("Insert genre: ");
            int pages = askInt("Insert pages: ");
            double price = askDouble("Insert price: ");

            return dao.insertBook(isbn, author, title, genre, pages, price);
        } catch (Exception e) 
        {
            return "Error during insertion.";
        }
    }

    private static String _deleteBook() 
    {
        try
        {
            String isbn = ask("Insert isbn: ");
            return dao.deleteBook(isbn);
        }  
        catch(Exception e)
        {
            e.printStackTrace();
            return "Error during file rewriting. ";
        }
    }
    
}
