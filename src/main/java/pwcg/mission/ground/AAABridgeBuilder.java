package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AAABridgeBuilder 
{
	private Campaign campaign;
	
	public AAABridgeBuilder(Campaign campaign) throws PWCGException
	{
		this.campaign = campaign;
	}

	public List<GroundUnitCollection> createAAAForBridges() throws PWCGException
	{
		List<GroundUnitCollection> bridgeAAA = new ArrayList<>();
        GroupManager groupData = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager();

		for (Bridge bridge : groupData.getBridgeFinder().findAllBridges())
		{
	        ICountry country = bridge.determineCountryOnDate(campaign.getCampaignMap(), campaign.getDate());
	        if (!country.isNeutral())
	        {
	            double angle = RandomNumberGenerator.getRandom(360);
	            double distance = 100 + RandomNumberGenerator.getRandom(400);
	            Coordinate aaaPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), bridge.getPosition(), angle, distance);
	            
	            TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, aaaPosition, bridge.getCountry(campaign.getCampaignMap(), campaign.getDate()), "AAA For Bridge");
	            AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
	            GroundUnitCollection aaaArty = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
	            bridgeAAA.add(aaaArty);
	        }
		}
		
		return bridgeAAA;
	}
}
