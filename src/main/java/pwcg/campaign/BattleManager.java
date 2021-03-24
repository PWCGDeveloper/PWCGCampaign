package pwcg.campaign;

import java.util.Date;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.io.json.BattleIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;

public class BattleManager
{
	private Battles battles = new Battles();
    private FrontMapIdentifier map;

	public BattleManager(FrontMapIdentifier map)
	{
	    this.map = map;
	}
	
	public void initialize()
	{
        try
        {
            battles = BattleIOJson.readJson(map.getMapName());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    public Battle getBattleForCampaign(FrontMapIdentifier mapId, Coordinate position, Date date) 
    {     
        try
        {
            for (Battle battle : battles.getBattles())
            {
                if (isBattleAtRightTime(date, battle))
                {
	                if (isBattleOnRightMap(battle, mapId))
	                {
                        if (isBattleNearPlayer(position, battle, mapId, date))
                        {
                            return battle;
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

    private boolean isBattleOnRightMap(Battle battle, FrontMapIdentifier mapId)
	{
    	if (mapId == null)
    	{
    		return false;
    	}
    	
    	if (battle.getMap() == mapId)
    	{
    		return true;
    	}
    	
		return false;
	}

	private boolean isBattleAtRightTime(Date date, Battle battle) throws PWCGException
    {
        if (date.after(battle.getStartDate()) && date.before(battle.getStopDate()))
        {
        	return true;
        }
            
        return false;
    }
    
    private boolean isBattleNearPlayer(Coordinate position, Battle battle, FrontMapIdentifier matchingMap, Date date) throws PWCGException
    {
        FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getMapByMapId(matchingMap).getFrontLinesForMap(date);
        Coordinate closestFrontLines = frontLineMarker.findClosestFrontCoordinateForSide(position, Side.ALLIED);

        double distanceFromBattleFront = MathUtils.calcDist(closestFrontLines, position);
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int closeToBattleDistance = productSpecific.getCloseToBattleDistance();
        if (distanceFromBattleFront < closeToBattleDistance)
        {
            return true;
        }
        
        return false;
    }
}
