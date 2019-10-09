package pwcg.campaign.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

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
	
	public void write() throws PWCGIOException 
	{
		try
        {
            String userfilename = PWCGContext.getInstance().getDirectoryManager().getPwcgUserDir() + "PlanesOwned.config"; 
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
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	public void read() 
	{
		try
		{
			String planesOwnedFilename = PWCGContext.getInstance().getDirectoryManager().getPwcgUserDir() + "PlanesOwned.config"; 
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
		// Don't throw an exception here.  This just means that the file
		// has not yet been created.
		catch (Exception e)
		{
			Logger.logException(e);
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
	
	/**
	 * @return
	 */
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
