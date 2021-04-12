package pwcg.campaign.battle;

import java.util.Date;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.io.json.AmphibiousAssaultIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
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

    public AmphibiousAssault getAmphibiousAssaultsForCampaign(FrontMapIdentifier mapId, Coordinate position, Date date) 
    {     
        try
        {
            for (AmphibiousAssault amphibiousAssault : amphibiousAssaults.getAmphibiousAssaults())
            {
                if (isAmphibiousAssaultAtRightTime(date, amphibiousAssault))
                {
	                if (isAmphibiousAssaultOnRightMap(amphibiousAssault, mapId))
	                {
                        if (isAmphibiousAssaultNearPlayer(position, amphibiousAssault, mapId, date))
                        {
                            return amphibiousAssault;
                        }
                    }
                }
            }
        }
        catch (Throwable t)
        {
           PWCGLogger.logException(t);
        }
        
        return null;
    }

    private boolean isAmphibiousAssaultOnRightMap(AmphibiousAssault amphibiousAssault, FrontMapIdentifier mapId)
	{
    	if (mapId == null)
    	{
    		return false;
    	}
    	
    	if (map == mapId)
    	{
    		return true;
    	}
    	
		return false;
	}

	private boolean isAmphibiousAssaultAtRightTime(Date date, AmphibiousAssault amphibiousAssault) throws PWCGException
    {
        if (DateUtils.isDateInRange(date, amphibiousAssault.getLandingStartDate(), amphibiousAssault.getLandingStopDate()))
        {
        	return true;
        }
            
        return false;
    }
    
    private boolean isAmphibiousAssaultNearPlayer(Coordinate position, AmphibiousAssault amphibiousAssault, FrontMapIdentifier matchingMap, Date date) throws PWCGException
    {
        FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getMapByMapId(matchingMap).getFrontLinesForMap(date);
        Coordinate closestFrontLines = frontLineMarker.findClosestFrontCoordinateForSide(position, Side.ALLIED);

        double distanceFromAmphibiousAssaultFront = MathUtils.calcDist(closestFrontLines, position);
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int closeToAmphibiousAssaultDistance = productSpecific.getCloseToBattleDistance();
        if (distanceFromAmphibiousAssaultFront < closeToAmphibiousAssaultDistance)
        {
            return true;
        }
        
        return false;
    }
}
