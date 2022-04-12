package com.generation.model.Manager;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.generation.common.Password;
import com.generation.model.entities.Book;
import com.generation.model.entities.Comic;
import com.generation.model.entities.User;

public class LibraryMNGcsv implements ILibraryMNG
{

    private List<Book> content = new ArrayList<Book>();
    private List<User> users = new ArrayList<User>();
    private List<String> importErrors = new ArrayList<String>();
	private String booksFileName, usersFileName;
	
	public LibraryMNGcsv(String booksFileName, String usersFileName) throws Exception
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
                    switch(parts[0])
                    {
                        case "Book":
                            Book bookTemp = new Book(parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]), Double.parseDouble(parts[6]));
                            if(!bookTemp.isValid())
                            throw new RuntimeException("Book "+bookTemp+" from row "+row+" not valid.");

                            content.add(bookTemp);
                        break;
                        
                        case "Comic":
                            Comic comicTemp = new Comic(parts[1], parts[2], parts[3], parts[4], parts[5], Integer.parseInt(parts[4]), Double.parseDouble(parts[5]));
                            if(!comicTemp.isValid())
                            throw new RuntimeException("Comic "+comicTemp+" from row "+row+" not valid.");

                            content.add(comicTemp);
                        break;
                        
                        default:
                            throw new RuntimeException("Book from row "+row+" not valid.");
                    }
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

    public List<User> getUsers()
    {
        List<User> res = new ArrayList<User>();
        res.addAll(users);
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

    /**
	 * salvare il libro che arriva nel file
	 * restituire "OK" se il salvataggio va a buon fine.
	 * restituire "ALREADYPRESENT" se c'è già.
	 * @param book
	 * @return
	 * @throws Exception
	 */
    public String insertBook(String isbn, String author, String title, String genre, int pages, double price) throws Exception
	{
        Book temp = new Book(isbn, author, title, genre, pages, price);
        if(!temp.isValid())
            return "Invalid book, try again.";
        
        if(getBook(temp.getIsbn())!=null)
            return "Already present, try again.";

        content.add(temp);
        _recreateBooksFile();
		return "Book registered correctly.";
	}

    /**
	 * restituisce l'esito dell'eliminazione
	 * @param book
	 * @return
	 * @throws Exception
	 */
	public String deleteBook(String isbn) throws Exception
	{
        Book temp = getBook(isbn);
        if(temp==null)
            return "Book not found, try again";
        
        content.remove(temp);
        _recreateBooksFile();
		return "Book deleted successfully";
	}

    /**
     * Gets as parameters user data, tries to create an user
     * @param ssn
     * @param name
     * @param surname
     * @param email
     * @param dob
     * @param gender
     * @param username
     * @param password
     * @return user.getErrors(), "Already present, try again.", "User registered correctly." 
     * @throws Exception
     */
    public String insertUser
    (
        String ssn, String name, String surname, String email, String dob, String gender, 
        String username, String password
    ) throws Exception  
    {
        Password pw = new Password(password);
        User temp = new User(ssn, name, surname, email, dob, gender, username, password);
        if(pw.isValid())
        {
            if(!temp.isValid() || username.contains(","))
                return temp.getErrors();
        
            if(isAlreadyPresent(temp))
                return "Already present, try again.";
            
            String encryptedPassword = temp.getPassword().encrypt();
            temp.setPassword(encryptedPassword);
            users.add(temp);
            _recreateUsersFile();
            return "User registered correctly.";
        }
        else
            return temp.getErrors();
    }

    /**
     * 
     * @param loginUsername
     * @param username
     * @return "You can't delete yourself, try again.", "User removed successfully.", "User not found, try again"
     * @throws Exception
     */
    public String deleteUser(String loginUsername, String username) throws Exception
    {
        if(loginUsername.equalsIgnoreCase(username))
            return "You can't delete yourself, try again.";

        for(User u:getUsers())
            if(u.getUsername().equalsIgnoreCase(username))
            {
                users.remove(u);
                _recreateUsersFile();
                return "User removed successfully.";
            }

        return "User not found, try again";
    }

    /**
     * Rewrite books file with the current books list.
     * @throws Exception
     */
    public void _recreateBooksFile() throws Exception
    {
        String res = "";
        FileWriter writer = new FileWriter(booksFileName);
    
        for(int i=0;i<getBooks().size();i++)
        {
            res += getBooks().get(i).toCSV();
            res += i != getBooks().size()-1 ? "\n" : "";
        }

        writer.write(res);
        writer.close();
    }

    /**
     * Rewrite users file with the current users list.
     * @throws Exception
     */
    public void _recreateUsersFile() throws Exception
    {
        String res = "";
		FileWriter writer = new FileWriter(usersFileName);
    
        for(int i=0;i<getUsers().size();i++)
        {
            res += getUsers().get(i).toCSV();
            res += i != getUsers().size()-1 ? "\n" : "";
        }

        writer.write(res);
        writer.close();
    }


}
