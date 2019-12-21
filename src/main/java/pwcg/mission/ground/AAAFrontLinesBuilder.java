package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class AAAFrontLinesBuilder 
{
    private Campaign campaign;
	private List<IGroundUnitCollection> aaaForFront = new ArrayList<>();
	private FrontLinesForMap frontLinesForMap;
	private Coordinate lastAAAPosition = null;

	public AAAFrontLinesBuilder(Campaign campaign) throws PWCGException
	{
        this.campaign = campaign;
	}
	
	public List<IGroundUnitCollection> generateAAAEmplacements () throws PWCGException
	{
        frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());

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
        
        int distanceToLastAAA = Double.valueOf(MathUtils.calcDist(lastAAAPosition, currentFrontPointPosition)).intValue();
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

        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, icountry, aaaMgPosition);
        IGroundUnitCollection aaaMg = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        aaaForFront.add(aaaMg);
    }
    
    private void createAAAArty(Side side) throws PWCGException
    {
        Coordinate aaaArtyPosition = frontLinesForMap.findPositionBehindLinesForSide(lastAAAPosition, 1000, 1000, 2000, side);
        ICountry icountry = CountryFactory.makeMapReferenceCountry(side);
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, icountry, aaaArtyPosition);
        IGroundUnitCollection aaaArty = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        aaaForFront.add(aaaArty);
    }

	public List<IGroundUnitCollection> getAA()
	{
		return aaaForFront;
	}
}
