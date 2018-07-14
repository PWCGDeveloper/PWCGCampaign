package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;

public class AAAFrontLinesBuilder 
{
    private Campaign campaign;
	private List<GroundUnitSpawning> aaaForFront = new ArrayList<GroundUnitSpawning>();
	private FrontLinesForMap frontLinesForMap;
	private Coordinate lastAAAPosition = null;

	public AAAFrontLinesBuilder(Campaign campaign) throws PWCGException
	{
        this.campaign = campaign;
	}
	
	public List<GroundUnitSpawning> generateAAAEmplacements () throws PWCGException
	{
        frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());

		generateAAAEmplacementsForSide(Side.ALLIED);
		generateAAAEmplacementsForSide(Side.AXIS);
		
		return aaaForFront;
	}
	
	private void generateAAAEmplacementsForSide (Side side) throws PWCGException
	{
		
        for (int frontIndex = 5; frontIndex < frontLinesForMap.getFrontLines(side).size(); ++frontIndex)
		{
            if (isAddAAA(side, frontIndex))
            {
                if ((aaaForFront.size() %  3) == 0)
                {
                    createAAAMg(side);                 
                }
                else
                {
                    createAAAArty(side);
                }
			}
		}
	}
	
	private boolean isAddAAA(Side side, int frontIndex) throws PWCGException
	{
        Coordinate currentFrontPointPosition = frontLinesForMap.getFrontLines(side).get(frontIndex).getPosition().copy();
	    if (lastAAAPosition == null)
	    {
	        lastAAAPosition = currentFrontPointPosition;
	        return true;
	    }
        
        int distanceToLastAAA = new Double(MathUtils.calcDist(lastAAAPosition, currentFrontPointPosition)).intValue();
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int mgSpacing =  configManager.getIntConfigParam(ConfigItemKeys.MGSpacingKey);
        
        if (distanceToLastAAA >= mgSpacing)
        {
            lastAAAPosition = currentFrontPointPosition;
            return true;
        }
        
	    return false;
	}
    
    private void createAAAMg(Side side) throws PWCGException
    {
        Coordinate aaaMgPosition = frontLinesForMap.findPositionBehindLinesForSide(lastAAAPosition, 1000, 50, 200, side);
        ICountry icountry = CountryFactory.makeMapReferenceCountry(side);

        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign, icountry, aaaMgPosition);
        GroundUnitSpawning aaaMg = groundUnitFactory.createAAAMGBattery(1, 1);
        aaaForFront.add(aaaMg);
    }
    
    private void createAAAArty(Side side) throws PWCGException
    {
        Coordinate aaaArtyPosition = frontLinesForMap.findPositionBehindLinesForSide(lastAAAPosition, 1000, 1000, 2000, side);
        ICountry icountry = CountryFactory.makeMapReferenceCountry(side);
        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign, icountry, aaaArtyPosition);
        GroundUnitSpawning aaaArty = groundUnitFactory.createAAAArtilleryBattery(1, 1);
        aaaForFront.add(aaaArty);
    }

	public List<GroundUnitSpawning> getAA()
	{
		return aaaForFront;
	}
}
