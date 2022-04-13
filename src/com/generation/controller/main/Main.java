package com.generation.controller.main;

import static com.generation.common.Console.ask;
import static com.generation.common.Console.askDouble;
import static com.generation.common.Console.askInt;
import static com.generation.common.Console.print;

import java.util.List;

import javax.naming.NameNotFoundException;

import com.generation.model.Manager.ILibraryMNG;
import com.generation.model.Manager.LibraryMNGcsv;
import com.generation.model.entities.Book;
import com.generation.model.entities.User;
import com.generation.model.language.ILanguage;
import com.generation.model.language.LanguageFactory;
import com.generation.model.view.BookView;

public class Main {

    static ILibraryMNG mng;
    static String[] VALIDLANGCODES = {"ITA", "ENG", "ABR"};
    static ILanguage language;
    static BookView view;
    static User user;


    public static void main(String[] args) throws Exception 
    {
        print("Welcome.");
        do
        {
            language = _selectLanguage();
            if(language == null)
                print("Invalid code, try again.\n");
        }
        while(language == null);

        

        print(language.translate("WELCOMELIBRARY"));
        String res = "", cmd = "";
        view = new BookView();

        try
        {
            mng = new LibraryMNGcsv("books.csv", "users.csv");
        }
        catch(Exception e)
        {
            print(language.translate("FILENOTFOUND"));
            System.exit(1);
        }
        
        
        do
		{
            language.translate("ASKCREDENTIALS");
			String username = ask(language.translate("ASKUSERNAME")+"\n"+language.translate("BYETOQUIT"));
			if(username.equalsIgnoreCase("close")) 
			{
				print("Goodbye");
				System.exit(1);
			}
			String password = ask(language.translate("ASKPASSWORD"));
			user = mng.login(username,password);

			if(user==null)
			{
				print("Wrong credentials, try again.\n");
			}
		}while(user==null);
        
        print(language.translate("WELCOMESYSTEM")); 

        do
        {
            cmd = ask(language.translate("ASKCOMMAND"));

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
                    res = _InsertUser();
                break;

                case "7":
                    res = _deleteUser();
                break;
                
                case "8":
                    res = _viewUser();
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
        return mng.deleteUser(loginUsername, username);
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
        return mng.insertUser(ssn, name, surname, email, dob, gender, username, password);
    }

    private static String _viewUser() 
    {
        return view.renderUser(user);
    }


    private static String _getBooks() 
    {
        List<Book> res = mng.getBooks();
        return !res.isEmpty() ?
            view.renderBooks(res)
            :
            "Couldn't find any book.";
    }

    private static String _getBooksByGenre() 
    {
        String genre = ask("Insert genre: ");
        List<Book> res = mng.getBooks(genre);
        return !res.isEmpty() ?
                view.renderBooks(res)
                :
                "Couldn't find any book with the genre "+genre+", try again.";
    }

    private static String _getBookByIsbn() 
    {
        String isbn = ask("Insert isbn: ");
        Book res = mng.getBook(isbn);
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
            double price = askDouble("Insert price: ");

            return mng.insertBook(isbn, author, title, genre, pages, price);
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
            return mng.deleteBook(isbn);
        }  
        catch(Exception e)
        {
            e.printStackTrace();
            return "Error during file rewriting. ";
        }
    }

    private static ILanguage _selectLanguage()
    {
        String langcode = ask
        (
            "Please, select language:\n"+
            "ITA for Italian\n" +
            "ENG for English\n" +
            "ABR for Abruzzese\n"
        );

        for(String s: VALIDLANGCODES)
            if(langcode.equalsIgnoreCase(s))
            {
                return LanguageFactory.getLanguage(s);
            }

        return null;
    }
    
}
