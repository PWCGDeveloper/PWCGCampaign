package pwcg.campaign.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class PlanesOwnedManager 
{

	static private ArrayList<String> planesOwnedList = new ArrayList<String>();
	
	static private PlanesOwnedManager instance = null;
	
	private PlanesOwnedManager ()
	{
	}
	
	public static PlanesOwnedManager getInstance()
	{
		if (instance == null)
		{
			instance = new PlanesOwnedManager();
			instance.read();
		}
		
		return instance;
	}
	
	public void write() throws PWCGException 
	{
		try
        {
            String userfilename = PWCGDirectoryUserManager.getInstance().getPwcgUserConfigDir() + "PlanesOwned.config"; 
            File userFile = new File(userfilename);		
            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
            
            for(int i = 0; i < planesOwnedList.size(); ++i)
            {
            	String plane = planesOwnedList.get(i);
            	writer.write(plane);
            	writer.newLine();
            }
            
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}
	
	public void read() 
	{
		try
		{
			String planesOwnedFilename = PWCGDirectoryUserManager.getInstance().getPwcgUserConfigDir() + "PlanesOwned.config"; 
			File planesOwnedFile = new File(planesOwnedFilename);
			
			BufferedReader reader = new BufferedReader(new FileReader(planesOwnedFile));
			String line = "";
			
			while ((line = reader.readLine()) != null) 
			{
				line = line.trim();
	
				if (line != null && line.length() > 0)
				{
					planesOwnedList.add(line);
				}
			}
			
			reader.close();
		}
		catch (Exception e)
		{
			PWCGLogger.log(LogLevel.DEBUG, "Planes owned file not found");
		}
	}
	
	public boolean isPlaneOwned(String planeName)
	{
		boolean isPlaneOwned = false;
		
		for(int i = 0; i < planesOwnedList.size(); ++i)
		{
			String plane = planesOwnedList.get(i);
			if (planeName.equalsIgnoreCase(plane))
			{
				isPlaneOwned = true;
			}
		}
		
		return isPlaneOwned;
	}

	public boolean hasPlanesOwned()
	{
		if (planesOwnedList.size() > 0)
		{
			return true;
		}

		return false;
	}

	public void clear()
	{
		planesOwnedList.clear();
	}

	public void setPlaneOwned(String planeName)
	{
		planesOwnedList.add(planeName);
	}
}
