package com.generation.model.dao;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import com.generation.common.Password;
import com.generation.model.entities.Book;
import com.generation.model.entities.User;

public class BookDAOcsv 
{

    private List<Book> content = new ArrayList<Book>();
    private List<User> users = new ArrayList<User>();
    private List<String> importErrors = new ArrayList<String>();
	private String booksFileName, usersFileName;
	
	public BookDAOcsv(String booksFileName, String usersFileName) throws Exception
	{
        {
            this.booksFileName = booksFileName;
            
            Scanner dataReader = new Scanner(new File(this.booksFileName));

            while(dataReader.hasNextLine())
            {
                String row = dataReader.nextLine();
                try
                {
                    String[] parts = row.split(",");
                    Book temp = new Book(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), Double.parseDouble(parts[5]));

                    if(!temp.isValid())
                        throw new RuntimeException("Book "+temp+" from row "+row+" not valid.");

                    content.add(temp);
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    importErrors.add("Error importing, arrayindexoutofboundexception for "+row+" : "+e.getMessage());
                }
                catch(RuntimeException e)
                { 
                    importErrors.add(e.getMessage());
                }
            }
            dataReader.close();
        }
        
        {
            this.usersFileName = usersFileName;
            
            Scanner dataReader = new Scanner(new File(this.usersFileName));

            while(dataReader.hasNextLine())
            {
                String row = dataReader.nextLine();
                try
                {
                    String[] parts = row.split(",");
                    User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);

                    if(!user.isValid())
                        throw new RuntimeException("User "+user+" from row "+row+" not valid.");

                    if(isAlreadyPresent(user))
                        throw new RuntimeException("User "+user+" from row "+row+" already present.");

                    users.add(user);
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    importErrors.add("Error importing, arrayindexoutofboundexception for "+row+" : "+e.getMessage());
                }
                catch(RuntimeException e)
                { 
                    importErrors.add(e.getMessage());
                }
            }
            dataReader.close();
        }
    }    

    /**
     * 
     * @param u
     * @return
     */
    private boolean isAlreadyPresent(User u)
    {
        for(User user: users)
        {
            if(u.getSsn().getContent().equalsIgnoreCase(user.getSsn().getContent()) || u.getEmail().getContent().equalsIgnoreCase(user.getEmail().getContent()))
                return true;
        }

        return false;
    }

    /**
     * Search for a user based on the username, then checks if the encrypted password matches and returns true if login's been successful.
     * @param username
     * @param password
     * @return true || false
     * @throws NoSuchAlgorithmException
     */
    public boolean login(String username, String password) throws NoSuchAlgorithmException
    {
        User user =    getUsers()
                    .stream()
                    .filter((u) -> u.getUsername().equalsIgnoreCase(username))
                    .collect(Collectors.toList()).get(0);
        
        if(user!=null)
        {
            Password pw = new Password(password);
            String encryptedPassword = pw.encrypt();
            if(user.getPassword().getContent().equals(encryptedPassword))
                return true;
        }

        return false;
    }

    /**
	 * Restituire la lista di tutti i libri.
	 * Non restituire la lista originale. Dare una copia.
	 * @return
	 */
	public List<Book> getBooks()
	{
        List<Book> res = new ArrayList<Book>();
        res.addAll(content);
		return res;
	}

	/**
	 * restituire tutti i libri di questo genere
	 * @param genre
	 * @return List of parameter genre books
	 */
	public List<Book> getBooks(String genre)
	{
        List<Book> res = new ArrayList<Book>();
        for(Book b:getBooks())
            if(b.getGenre().equalsIgnoreCase(genre))
                res.add(b);
		return res;
	}

    /**
     * returns import errors list
     * @return
     */
    public List<String> getImportErrors()
    {
        return importErrors;
    }

    public List<User> getUsers()
    {
        List<User> res = new ArrayList<User>();
        res.addAll(users);
        return res;
    }
	
	/**
	 * restituire il libro col codice indicato, null altrimenti
	 * @param code
	 * @return
	 */
	public Book getBook(String isbn)
	{
        for(Book b:getBooks())
            if(b.getIsbn().equalsIgnoreCase(isbn))
                return b;

		return null;
	}
	
	/**
	 * salvare il libro che arriva nel file
	 * restituire "OK" se il salvataggio va a buon fine.
	 * restituire "ALREADYPRESENT" se c'è già.
	 * @param book
	 * @return
	 */
	public String insert(String isbn, String author, String title, String genre, int pages, double price)
	{
        Book temp = new Book(isbn, author, title, genre, pages, price);
        if(!temp.isValid())
            return "Invalid book, try again.";
        
        if(getBook(temp.getIsbn())!=null)
            return "Already present, try again.";

        content.add(temp);
		return "Book registered correctly.";
	}
	
	/**
	 * restituisce l'esito dell'eliminazione
	 * @param book
	 * @return
	 */
	public String delete(String isbn)
	{
        Book temp = getBook(isbn);
        if(temp==null)
            return "Book not found, try again";
        
        content.remove(temp);
		return "Book deleted successfully";
	}
    
}
