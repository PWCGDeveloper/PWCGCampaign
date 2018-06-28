package pwcg.campaign.context;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

public abstract class CountryBase
{
    public static String sNEUTRAL = "Neutral";
    public static String sFRANCE = "France";
    public static String sBELGIUM = "Belgium";
    public static String sUSA = "USA";
    public static String sBRITAIN = "Britain";
    public static String sRUSSIA = "Russia";
    public static String sGERMANY = "Germany";
    public static String sAUSTRIA = "Austria";
    public static String sITALY = "Italy";

    public static String sFRENCH = "French";
    public static String sBELGIAN = "Belgian";
    public static String sAMERICAN = "American";
    public static String sBRITISH = "British";
    public static String sRUSSIAN = "Russian";
    public static String sGERMAN = "German";
    public static String sAUSTRIAN = "Austrian";
    public static String sITALIAN = "Italian";

    public static int NEUTRAL_CODE = 0;

    protected Country country = Country.NEUTRAL;
    
    public abstract Side getSide();
    protected abstract int adjustCountryCode();
    protected abstract int countryToCountryCode (Country country);
    protected abstract Country countryCodeToCountry (int countryCode);

    public String getCountryName ()
    {
        String countryName = sNEUTRAL;

        if (country == Country.FRANCE)
        {
            countryName = sFRANCE;
        }
        else if (country == Country.BELGIUM)
        {
            countryName = sBELGIUM;
        }
        else if (country == Country.USA)
        {
            countryName = sUSA;
        }
        else if (country == Country.BRITAIN)
        {
            countryName = sBRITAIN;
        }
        else if (country == Country.GERMANY)
        {
            countryName = sGERMANY;
        }
        else if (country == Country.AUSTRIA)
        {
            countryName = sAUSTRIA;
        }
        else if (country == Country.RUSSIA)
        {
            countryName = sRUSSIA;
        }
        else if (country == Country.ITALY)
        {
            countryName = sITALY;
        }
        
        return countryName;
    }

    public String getNationality ()
    {
        String nationality = sNEUTRAL;

        if (country == Country.FRANCE)
        {
            nationality = sFRENCH;
        }
        else if (country == Country.BELGIUM)
        {
            nationality = sBELGIAN;
        }
        else if (country == Country.USA)
        {
            nationality = sAMERICAN;
        }
        else if (country == Country.BRITAIN)
        {
            nationality = sBRITISH;
        }
        else if (country == Country.GERMANY)
        {
            nationality = sGERMAN;
        }
        else if (country == Country.AUSTRIA)
        {
            nationality = sAUSTRIAN;
        }
        else if (country == Country.RUSSIA)
        {
            nationality = sRUSSIAN;
        }
        else if (country == Country.ITALY)
        {
            nationality = sITALIAN;
        }
        
        return nationality;
    }

    public boolean isNeutral()
    {
        boolean is = false;
        if (country == Country.NEUTRAL)
        {
            is = true;
        }
        return is;
    }

    public boolean isSameSide(ICountry otherCountry)
    {
        if (getSide() == otherCountry.getSide())
        {
            return true;
        }
        
        return false;
    }

    public boolean isCountryByCode(int testCountryCode)
    {
        if (this.getCountryCode() == testCountryCode)
        {
            return true;
        }
        
        return false;
    }

    public boolean isEnemy(ICountry otherCountry)
    {
        if (this.isNeutral())
        {
            return false;
        }
        
        if (getSide() != otherCountry.getSide())
        {
            return true;
        }
        
        return false;
    }

    public boolean isCountry(Country country)
    {
        if (this.country == country)
        {
            return true;
        }
        
        return false;
    }

    public boolean equals(ICountry otherCountry)
    {
        if (this.getCountryCode() == otherCountry.getCountryCode())
        {
            return true;
        }
        
        return false;
    }

    public int getCountryCode()
    {
        return countryToCountryCode(country);
    }

    public void write(BufferedWriter writer) throws PWCGIOException, PWCGException
    {
        try
        {
            int countryCode = countryToCountryCode(country);
            writer.write("    Country = " + countryCode + ";");
            writer.newLine();
         }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public void writeAdjusted(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            int adjustedCountryCode = adjustCountryCode();
                            
            writer.write("    Country = " + adjustedCountryCode + ";");
            writer.newLine();
         }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public static List<String> getCountryNames()
    {
        List<String> countryNames = new ArrayList<String>();
        countryNames.add(sFRANCE);
        countryNames.add(sUSA);
        countryNames.add(sBRITAIN);
        countryNames.add(sGERMANY);
        countryNames.add(sBELGIUM);
        countryNames.add(sAUSTRIA);
        countryNames.add(sITALY);
        countryNames.add(sRUSSIA);
        
        return countryNames;
    }

    public Side getSideNoNeutral() throws PWCGException
    {
        if (country == Country.NEUTRAL)
        {
            throw new PWCGException("Attempt to create neutral unit");
        }
        
        return getSide();
    }

    public Country getCountry()
    {
        return country;
    }
}
