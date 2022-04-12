package com.generation.model.dao;

import java.io.FileWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.generation.common.Password;
import com.generation.model.entities.Book;
import com.generation.model.entities.User;

/*
    Interface that make clear what methods the BookMSN are going to have or have to implement
*/
public interface ILibraryMNG 
{
    //abstract methods
    public List<Book> getBooks();

    public List<User> getUsers();
    
    public List<String> getImportErrors();

    public String insertUser
    (
        String ssn, String name, String surname, String email, String dob, String gender, 
        String username, String password
    ) throws Exception;

    public String deleteUser(String loginUsername, String username) throws Exception;

    public String insertBook(String isbn, String author, String title, String genre, int pages, double price) throws Exception;
    
    public String deleteBook(String isbn) throws Exception;

    public void _recreateUsersFile() throws Exception;

    public void _recreateBooksFile() throws Exception;

    //default methods
    /**
     * Search for a user based on the username, then checks if the encrypted password matches and returns true if login's been successful.
     * @param username
     * @param password
     * @return true || false
     * @throws NoSuchAlgorithmException
     */
    default public User login(String username, String password) throws NoSuchAlgorithmException
    {
        try
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
                    return user;
            }
        }
        catch(IndexOutOfBoundsException e)
        {
            return null;
        }
        
        return null;
    }

    /**
	 * restituire tutti i libri di questo genere
	 * @param genre
	 * @return List of parameter genre books
	 */
	default public List<Book> getBooks(String genre)
	{
        List<Book> res = new ArrayList<Book>();
        for(Book b:getBooks())
            if(b.getGenre().equalsIgnoreCase(genre))
                res.add(b);
		return res;
	}

    /**
     * 
     * @param u
     * @return
     */
    default public boolean isAlreadyPresent(User u)
    {
        for(User user: getUsers())
        {
            if(u.getSsn().getContent().equalsIgnoreCase(user.getSsn().getContent()) || u.getEmail().getContent().equalsIgnoreCase(user.getEmail().getContent()))
                return true;
        }

        return false;
    }
    
    /**
	 * restituire il libro col codice indicato, null altrimenti
	 * @param code
	 * @return
	 */
	default public Book getBook(String isbn)
	{
        for(Book b:getBooks())
            if(b.getIsbn().equalsIgnoreCase(isbn))
                return b;

		return null;
	}
    
}
