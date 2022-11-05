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
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AAAFrontLinesBuilder 
{
    private Campaign campaign;
	private List<GroundUnitCollection> aaaForFront = new ArrayList<>();
	private FrontLinesForMap frontLinesForMap;
	private Coordinate lastAAAPosition = null;

	public AAAFrontLinesBuilder(Campaign campaign) throws PWCGException
	{
        this.campaign = campaign;
	}
	
	public List<GroundUnitCollection> generateAAAEmplacements () throws PWCGException
	{
        frontLinesForMap =  PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());

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
        int mgSpacing =  getMGSpacingAlongFront();
        
        if (distanceToLastAAA >= mgSpacing)
        {
            lastAAAPosition = currentFrontPointPosition;
            return true;
        }
        
	    return false;
	}
	
	private int getMGSpacingAlongFront() throws PWCGException
	{
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentAASetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigAAKey);
        if (currentAASetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            return 6000;
        }
        else if (currentAASetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            return 4000;
        }
        else
        {
            return 2000;
        }
	}
    
    private void createAAAMg(Side side) throws PWCGException
    {
        Coordinate aaaMgPosition = frontLinesForMap.findPositionBehindLinesForSide(campaign.getCampaignMap(), lastAAAPosition, 1000, 50, 200, side);
        ICountry icountry = CountryFactory.makeMapReferenceCountry(campaign.getCampaignMap(), side);

        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, aaaMgPosition, icountry, "AAA For Front Lines");
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
        GroundUnitCollection aaaMg = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        aaaForFront.add(aaaMg);
    }
    
    private void createAAAArty(Side side) throws PWCGException
    {
        Coordinate aaaArtyPosition = frontLinesForMap.findPositionBehindLinesForSide(campaign.getCampaignMap(), lastAAAPosition, 1000, 1000, 2000, side);
        ICountry icountry = CountryFactory.makeMapReferenceCountry(campaign.getCampaignMap(), side);
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, aaaArtyPosition, icountry, "AAA For Front Lines");
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
        GroundUnitCollection aaaArty = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        aaaForFront.add(aaaArty);
    }

	public List<GroundUnitCollection> getAA()
	{
		return aaaForFront;
	}
}
