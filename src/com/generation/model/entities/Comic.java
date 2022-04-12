package com.generation.model.entities;

public class Comic extends Book
{
    private String illustrator;
    
    public Comic(String isbn, String author, String illustrator, String title, String genre, int pages, double price)
    {
        super(isbn, author, title, genre, pages, price);
        this.illustrator = illustrator;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public void setIllustrator(String illustrator) {
        this.illustrator = illustrator;
    }

    @Override
    public String getErrors()
    {
        String res = "";
        res +=super.getErrors();
        if(illustrator==null || illustrator.isBlank())
            res+="Invalid illustrator.\n";

        return res;
    }

    @Override
    public String toCSV()
    {
        return "Comic"+","+super.getIsbn()+","+super.getAuthor()+","+illustrator+","+super.getTitle()+","+super.getGenre()+","+super.getPages()+","+super.getPrice();
    }

    
}
