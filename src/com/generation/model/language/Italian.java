package com.generation.model.language;

import java.util.HashMap;
import java.util.Map;

public class Italian implements ILanguage
{
    private Map<String,String> dictionary = new HashMap<String,String>();

    public Italian()
    {
        dictionary.put("ASKCOMMAND","Inserisci un comando:\n"                                   +
                                    "1 per vedere tutti i libri\n"                              +
                                    "2 per vedere tutti i libri di una determinata categoria\n" +
                                    "3 per cercare un libro in base all'isbn\n"                 +
                                    "4 per inserire un nuovo libro nel sistema\n"               +
                                    "5 per eliminare un libro dal sistema\n"                    +
                                    "6 per vedere i tuoi dati personali\n"                      +
                                    "7 per inserire un nuovo utente nel sistema\n"              +
                                    "8 per eliminare un utente\n"                               +
                                    "close per chiudere il programma\n");
        dictionary.put("USERNOTFOUND","Utente non trovato, riprova.");
        dictionary.put("BOOKNOTFOUND","Libro non trovato, riprova.");
        dictionary.put("INVALIDBOOK","Libro non valido, riprova.");
        dictionary.put("INVALIDDATA","Dati non validi, riprova.");
        dictionary.put("INVALIDCREDENTIALS","Credenziali non valide, riprova.");
        dictionary.put("ASKCREDENTIALS","Inserire le credenziali d'accesso.");
        dictionary.put("ASKUSERNAME","Inserire l'username: ");
        dictionary.put("ASKPASSWORD","Inserire la password: ");
        dictionary.put("BOOKALREADYPRESENT","Libro già presente, riprova.");
        dictionary.put("BOOKREGISTERED","Libro registrato correttamente.");
        dictionary.put("BOOKDELETED","Libro cancellato correttamente.");
        dictionary.put("USERREGISTERED","Utente registrato correttamente.");
        dictionary.put("USERDELETED","Utente eliminato correttamente.");
        dictionary.put("USERNOTFOUND","Utente non trovato, riprova.");
        dictionary.put("WELCOMELIBRARY","Benvenuto nella libreria!");
        dictionary.put("WELCOMESYSTEM","Benvenuto nel sistema della libreria!");
        dictionary.put("FILENOTFOUND","File non trovato/i.");
        dictionary.put("","");
        dictionary.put("","");
        dictionary.put("CANTEXEC","C'è stato un problema, riprova.");
        dictionary.put("USERALREADYPRESENT","Utente già presente.");
        dictionary.put("BYETOQUIT","Scrivere close per quittare.");
    } //dictionary.put("","");
    
    public String translate(String code)
    {
        return  dictionary.containsKey(code)    ?
                dictionary.get(code)            :
                code                            ;
    }

}
