package com.generation.model.language;

public abstract class LanguageFactory 
{
    public LanguageFactory(){}

    public static ILanguage getLanguage(String code)
    {
        switch(code)
        {
            case "ITA":
                return new Italian();

            case "ENG":
                return new English();

            case "ABR":
                return new Abruzzese();
        }
        return new English();
    }
    
}
