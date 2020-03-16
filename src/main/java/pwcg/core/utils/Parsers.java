package pwcg.core.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class Parsers 
{
	public static String getString (String line)
	{
		String val = "";
		int start = line.indexOf("\"");
		int end = line.lastIndexOf(";");
		val = line.substring(start + 1, end -1);
		
		return val;
	}

	public static int getInt (String line)
	{
		Integer val = null;
		int start = line.indexOf("=");
		int end = line.lastIndexOf(";");
		String valString = line.substring(start + 2, end);
		val = Integer.valueOf(valString);
		return val.intValue();
	}
	
	public static String getKey (String line)
	{
		int start = 0;
		int end = line.indexOf(" =");
		String valString = line.substring(start, end);
		return valString;
	}
	
	public static String getVal (String line)
	{
		String val = null;
		int start = line.indexOf("=");
		int end = line.lastIndexOf(";");
		val = line.substring(start + 2, end);
		
		if (val.contains(";"))
		{
			PWCGLogger.log(LogLevel.ERROR, "Malformed value for line: " + line);
		}
		
		return val;
	}
	
	public static String getVal2 (String line)
	{
		String val = null;
		int start = line.indexOf("=");
		int end = line.indexOf(";");
		val = line.substring(start + 2, end);
		
		if (val.contains(";"))
		{
		    PWCGLogger.log(LogLevel.ERROR, "Malformed value for line: " + line);
		}


		return val;
	}
	
	public static double getDouble (String line)
	{
		Double val = null;
		int start = line.indexOf("=");
		int end = line.lastIndexOf(";");
		String valString = line.substring(start + 2, end);
		val = Double.valueOf(valString);
		return val.intValue();
	}

	public static Date getDate (String line) throws PWCGException
	{
		int start = line.indexOf("=");
		int end = line.lastIndexOf(";");
		String valString = line.substring(start + 2, end);
		valString = valString.replace(".", "/");
		
		return DateUtils.getDateNoCheck(valString);
	}
	
	public static List<Integer> getIntList (String line) 
	{
		List<Integer> values = new ArrayList<Integer>();
		
		try
		{
			int start = line.indexOf("[");
			int end = line.lastIndexOf("]");
			String valListString = line.substring(start + 1, end);
			
			if (valListString.length() > 0)
			{
				String[] splitLine = valListString.split(",");
				for (String val : splitLine)
				{
					Integer intVal = Integer.valueOf(val);
					values.add(intVal);
				}
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			throw e;
		}


		return values;
	}
	
	public static String removeExtension(String fileName)
	{
	    String truncatedFile = null;
	    
        int index = fileName.lastIndexOf('.');
        if (index > 0)
        {
            truncatedFile = fileName.substring(0, index);
        }
        else
        {
            truncatedFile = fileName;
        }
        
        return truncatedFile;
	}

	   
    public static Coordinate getCoordinate (String line) throws PWCGException 
    {
        Coordinate coordinate = new Coordinate();
        
        double[] pos = new double[2];
        
        int start = line.indexOf("[");
        int end = line.lastIndexOf("]");
        String valListString = line.substring(start + 1, end);
        
        if (valListString.length() > 0)
        {
            String[] splitLine = valListString.split(",");
            for (int i = 0; i < splitLine.length; ++i)
            {
                String stringVal = splitLine[i];
                pos[i] =  Double.valueOf(stringVal);
            }
        }
        
        coordinate.setXPos(pos[0]);
        coordinate.setZPos(pos[1]);

        return coordinate;
    }

}
