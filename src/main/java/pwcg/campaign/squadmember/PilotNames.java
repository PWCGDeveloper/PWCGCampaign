package pwcg.campaign.squadmember;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

public class PilotNames 
{
	private static ArrayList<String> firstNamesFrance = new ArrayList<String>();
	private static ArrayList<String> lastNamesFrance = new ArrayList<String>();
	private static ArrayList<String> firstNamesFlemish = new ArrayList<String>();
	private static ArrayList<String> lastNamesFlemish = new ArrayList<String>();
    private static ArrayList<String> firstNamesBritain = new ArrayList<String>();
    private static ArrayList<String> lastNamesBritain = new ArrayList<String>();
    private static ArrayList<String> firstNamesRussia = new ArrayList<String>();
    private static ArrayList<String> lastNamesRussia = new ArrayList<String>();
	private static ArrayList<String> firstNamesUSA = new ArrayList<String>();
	private static ArrayList<String> lastNamesUSA = new ArrayList<String>();
    private static ArrayList<String> firstNamesGermany = new ArrayList<String>();
    private static ArrayList<String> lastNamesGermany = new ArrayList<String>();
    private static ArrayList<String> firstNamesItaly = new ArrayList<String>();
    private static ArrayList<String> lastNamesItaly = new ArrayList<String>();
    private static ArrayList<String> firstNamesHungary = new ArrayList<String>();
    private static ArrayList<String> lastNamesHungary = new ArrayList<String>();

	private static PilotNames instance = null;

	private PilotNames ()
	{
	}

	public static PilotNames getInstance() throws PWCGException 
	{
		if (instance == null)
		{
			instance = new PilotNames();
			
			String namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "LastNamesBritain.txt";
			read(namesFile, lastNamesBritain);
			namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "FirstNamesBritain.txt";
			read(namesFile, firstNamesBritain);
	
			namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "LastNamesFrance.txt";
			read(namesFile, lastNamesFrance);
			namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "FirstNamesFrance.txt";
			read(namesFile, firstNamesFrance);
			
			namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "LastNamesFlemish.txt";
			read(namesFile, lastNamesFlemish);
			namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "FirstNamesFlemish.txt";
			read(namesFile, firstNamesFlemish);
	
			namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "LastNamesUSA.txt";
			read(namesFile, lastNamesUSA);
			namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "FirstNamesUSA.txt";
			read(namesFile, firstNamesUSA);
            
            namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "LastNamesRussia.txt";
            read(namesFile, lastNamesRussia);
            namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "FirstNamesRussia.txt";
            read(namesFile, firstNamesRussia);
            
            namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "LastNamesGermany.txt";
            read(namesFile, lastNamesGermany);
            namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "FirstNamesGermany.txt";
            read(namesFile, firstNamesGermany);
            
            namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "LastNamesItaly.txt";
            read(namesFile, lastNamesItaly);
            namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "FirstNamesItaly.txt";
            read(namesFile, firstNamesItaly);

            namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "LastNamesHungary.txt";
            read(namesFile, lastNamesHungary);
            namesFile = PWCGContextManager.getInstance().getDirectoryManager().getPwcgNamesDir() + "FirstNamesHungary.txt";
            read(namesFile, firstNamesHungary);
    		
    		instance.validateAscii();
		}
		
		return instance;
	}
	
	/**
	 * @param filename
	 * @param list
	 * @throws PWCGIOException 
	 */
	private static void read(String filename, ArrayList<String>list) throws PWCGIOException 
	{
		try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) 
            {
            	String name = line.trim();
            	if (name != null && name.length() != 0)
            	{
            		list.add(name);
            	}
            }
            
            reader.close();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	/**
	 * @param service
	 * @param namesUsed
	 * @return
	 */
	public String getName(ICountry country, Map<String, String> namesUsed)
	{
		String name = "";
		ArrayList<String> firstNameList = null;
		ArrayList<String> lastNameList = null;

        if (country.isCountry(Country.FRANCE))
		{
			firstNameList = firstNamesFrance;
			lastNameList = lastNamesFrance;
		}
        
        if (country.isCountry(Country.BELGIUM))
        {
            firstNameList = firstNamesFrance;
            lastNameList = lastNamesFrance;
            int odds = RandomNumberGenerator.getRandom(100);
            if (odds < 50)
            {
                firstNameList = firstNamesFlemish;
                lastNameList = lastNamesFlemish;
            }
        }

        if (country.isCountry(Country.USA))
		{
			firstNameList = firstNamesUSA;
			lastNameList = lastNamesUSA;
		}

        if (country.isCountry(Country.BRITAIN))
        {
            firstNameList = firstNamesBritain;
            lastNameList = lastNamesBritain;
        }
        
        
        if (country.isCountry(Country.RUSSIA))
        {
            firstNameList = firstNamesRussia;
            lastNameList = lastNamesRussia;
        }


        if (country.isCountry(Country.GERMANY))
        {
            firstNameList = firstNamesGermany;
            lastNameList = lastNamesGermany;
        }
        
        if (country.isCountry(Country.ITALY))
        {
            firstNameList = firstNamesItaly;
            lastNameList = lastNamesItaly;
        }
        
        // Use German and Hungarian names for Austria        
        if (country.isCountry(Country.AUSTRIA))
        {
            firstNameList = firstNamesHungary;
            lastNameList = lastNamesHungary;
            int odds = RandomNumberGenerator.getRandom(100);
            if (odds < 60)
            {
                firstNameList = firstNamesGermany;
                lastNameList = lastNamesGermany;
            }
        }

		int firstNameIndex = RandomNumberGenerator.getRandom(firstNameList.size());
		String firstName = firstNameList.get(firstNameIndex);
		
		int lastNameIndex = RandomNumberGenerator.getRandom(lastNameList.size());
		String lastName = lastNameList.get(lastNameIndex);
		
		while (namesUsed.containsKey(lastName))
		{
			lastNameIndex = RandomNumberGenerator.getRandom(lastNameList.size());
			lastName = lastNameList.get(lastNameIndex);
		}
		
		name = firstName + " " + lastName;
		
		return name;
	}
	
    
    /**
     * @return
     * @throws PWCGException 
     */
    public void validateAscii() throws PWCGException
    {
        boolean[] tests = new boolean[16];
        
        tests[0] = validateListForAsciiAscii("firstNamesFrance", firstNamesFrance);
        tests[1] = validateListForAsciiAscii("lastNamesFrance", lastNamesFrance);
        tests[2] = validateListForAsciiAscii("firstNamesFlemish", firstNamesFlemish);
        tests[3] = validateListForAsciiAscii("lastNamesFlemish", lastNamesFlemish);
        tests[4] = validateListForAsciiAscii("firstNamesBritain", firstNamesBritain);
        tests[5] = validateListForAsciiAscii("lastNamesBritain", lastNamesBritain);
        tests[6] = validateListForAsciiAscii("firstNamesRussia", firstNamesRussia);
        tests[7] = validateListForAsciiAscii("lastNamesRussia", lastNamesRussia);
        tests[8] = validateListForAsciiAscii("firstNamesUSA", firstNamesUSA);
        tests[9] = validateListForAsciiAscii("lastNamesUSA", lastNamesUSA);
        tests[10] = validateListForAsciiAscii("firstNamesGermany", firstNamesGermany);
        tests[11] = validateListForAsciiAscii("lastNamesGermany", lastNamesGermany);
        tests[12] = validateListForAsciiAscii("firstNamesItaly", firstNamesItaly);
        tests[13] = validateListForAsciiAscii("lastNamesItaly", lastNamesItaly);
        tests[14] = validateListForAsciiAscii("firstNamesHungary", firstNamesHungary);
        tests[15] = validateListForAsciiAscii("lastNamesHungary", lastNamesHungary);
        
        for (boolean test : tests)
        {
            if (!test)
            {
                throw new PWCGException("Non Ascii name found.  See error log for details");
            }
        }        
    }

    private static CharsetEncoder asciiEncoder = 
                    Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

    /**
     * @return
     */
    private boolean validateListForAsciiAscii(String listName, ArrayList<String>names)
    {
        boolean isGood = true;

        for (String name : names)
        {
            if (!asciiEncoder.canEncode(name))
            {
                String error = "Bad name in list: " + listName + " is " + name;
                Logger.log(LogLevel.ERROR, error);
            }
       }
        
        return isGood;
    }

}
