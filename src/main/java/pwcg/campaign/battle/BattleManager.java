package pwcg.campaign.battle;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.io.json.BattleIOJson;
import pwcg.campaign.skirmish.TargetDistance;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
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

    public Battle getBattleForCampaign(Campaign campaign, Coordinate battleLocation)
    {
        try
        {
            for (Battle battle : battles.getBattles())
            {
                if (isBattleAtRightTime(campaign.getDate(), battle))
                {
	                if (isBattleOnRightMap(battle, campaign.getCampaignMap()))
	                {
                        if (isBattleNearPlayer(campaign, battleLocation))
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
	    if (DateUtils.isDateInRange(date, battle.getStartDate(), battle.getStopDate()))
        {
        	return true;
        }
            
        return false;
    }
    
    private boolean isBattleNearPlayer(Campaign campaign, Coordinate battleLocation) throws PWCGException
    {
        int distanceToPlayer = TargetDistance.findTargetDistanceToReferencePlayer(campaign, battleLocation);        
        if (distanceToPlayer < TargetDistance.findMaxTargetDistanceForConfiguration(campaign))
        {
            return true;
        }
        
        return false;
    }
}
