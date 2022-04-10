package com.generation.model.entities;

public class Book implements Validable, CSV
{

    private static final String[] VALIDGENRES = 
                {   "Epic poem","Novel",
                    "Shonen","Seinen",
                    "Sci-fi","Scifi",
                    "Thriller","Crime novel",
                    "Biography","Horror",
                    "Adventure"
                };

    // cambiare visibilità, usare getter e setter
	private String isbn, author, title, genre;
	private int pages;
	private double price;
	
	public Book(String isbn, String author, String title, String genre, int pages, double price) {
		this.isbn = isbn;
		this.author = author;
		this.title = title;
		this.genre = genre;
		this.pages = pages;
		this.price = price;
	}

	public String getIsbn() 
    {
		return isbn;
	}

	public void setIsbn(String isbn) 
    {
		this.isbn = isbn;
	}

	public String getAuthor() 
    {
		return author;
	}

	public void setAuthor(String author) 
    {
		this.author = author;
	}

	public String getTitle() 
    {
		return title;
	}

	public void setTitle(String title) 
    {
		this.title = title;
	}

	public String getGenre() 
    {
		return genre;
	}

	public void setGenre(String genre) 
    {
		this.genre = genre;
	}

	public int getPages() 
    {
		return pages;
	}

	public void setPages(int pages) 
    {
		this.pages = pages;
	}

	public double getPrice() 
    {
		return price;
	}

	public void setPrice(double price) 
    {
		this.price = price;
	}

    public boolean isValid()
    {
        return isbn!=null && !isbn.isBlank() &&
                author!=null && !author.isBlank() &&
                title!=null && !title.isBlank() &&
                hasValidGenre();
    }

	private boolean hasValidGenre() 
    {
        for(int i=0;i<VALIDGENRES.length;i++)
            if(genre.equalsIgnoreCase(VALIDGENRES[i]))
                return true;
                
        return false;
    }

    @Override
	public String toString() 
    {
		return "Book [author=" + author + ", isbn=" + isbn + ", genre=" + genre + ", pages=" + pages + ", price="
				+ price + ", title=" + title + "]";
	}

    @Override
    public String getErrors() 
    {
        String res = "";

        if(author.isBlank() || author==null)
            res += "Invalid author\n";

        if(isbn.isBlank() || isbn == null)
            res += "Invalid code\n";

        if(!hasValidGenre())
            res += "Invalid genre\n";

        if(pages<1)
            res += "Invalid amount of pages\n";

        if(price<=0)
            res += "Invalid price\n";
        
        if(title.isBlank() || title==null)
            res += "Invalid title\n";

        return res;
    }

    @Override
    public String toCSV() 
    { 
        return isbn+","+author+","+title+","+genre+","+pages+","+price;
    }
    
}
