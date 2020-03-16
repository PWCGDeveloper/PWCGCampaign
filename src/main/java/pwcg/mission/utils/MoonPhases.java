package pwcg.mission.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;

public class MoonPhases 
{

	private static List <Date>fullMoon = new ArrayList<Date>();
	private static String[] fullMoonDates = 
	{
		"20/01/1943",
	};
	
	/**
	 * 
	 */
	private static void makeMoonPhases()
	{
		try
		{
			for (String fullMoonDate : fullMoonDates)
			{
				Date fullMoonOn = DateUtils.getDateNoCheck(fullMoonDate);
				fullMoon.add(fullMoonOn);
			}
		}
		catch (Exception e)
		{
			 PWCGLogger.logException(e);;
		}
	}
	
	/**
	 * @return
	 */
	public static List <Date> getMoonPhaseDates()
	{
		if (fullMoon.size() == 0)
		{
			makeMoonPhases();
		}
		
		return fullMoon;
	}
}
