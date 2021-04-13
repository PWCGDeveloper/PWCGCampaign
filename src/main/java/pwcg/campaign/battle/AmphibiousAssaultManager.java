package pwcg.campaign.battle;

import java.util.Date;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.io.json.AmphibiousAssaultIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;

public class AmphibiousAssaultManager
{
	private AmphibiousAssaults amphibiousAssaults = new AmphibiousAssaults();
    private FrontMapIdentifier map;

	public AmphibiousAssaultManager(FrontMapIdentifier map)
	{
	    this.map = map;
	}
	
	public void initialize()
	{
        try
        {
            amphibiousAssaults = AmphibiousAssaultIOJson.readJson(map.getMapName());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    public AmphibiousAssault getAmphibiousAssaultsForCampaign(Date date) 
    {     
        try
        {
            for (AmphibiousAssault amphibiousAssault : amphibiousAssaults.getAmphibiousAssaults())
            {
                if (isAmphibiousAssaultAtRightTime(date, amphibiousAssault))
                {
                    return amphibiousAssault;
                }
            }
        }
        catch (Throwable t)
        {
           PWCGLogger.logException(t);
        }
        
        return null;
    }

	private boolean isAmphibiousAssaultAtRightTime(Date date, AmphibiousAssault amphibiousAssault) throws PWCGException
    {
        if (DateUtils.isDateInRange(date, amphibiousAssault.getLandingStartDate(), amphibiousAssault.getLandingStopDate()))
        {
        	return true;
        }
            
        return false;
    }
}
