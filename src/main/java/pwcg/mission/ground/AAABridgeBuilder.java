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
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AAABridgeBuilder 
{
	private Campaign campaign;
	
	public AAABridgeBuilder(Campaign campaign) throws PWCGException
	{
		this.campaign = campaign;
	}

	public List<IGroundUnitCollection> createAAAForBridges() throws PWCGException
	{
		List<IGroundUnitCollection> bridgeAAA = new ArrayList<>();
        GroupManager groupData = PWCGContext.getInstance().getCurrentMap().getGroupManager();

		for (Bridge bridge : groupData.getBridgeFinder().findAllBridges())
		{
	        ICountry country = bridge.createCountry(campaign.getDate());
	        if (!country.isNeutral())
	        {
	            double angle = RandomNumberGenerator.getRandom(360);
	            double distance = 100 + RandomNumberGenerator.getRandom(400);
	            Coordinate aaaPosition = MathUtils.calcNextCoord(bridge.getPosition(), angle, distance);
	            
	            TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, aaaPosition, bridge.getCountry(campaign.getDate()));
	            AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
	            IGroundUnitCollection aaaArty = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
	            bridgeAAA.add(aaaArty);
	        }
		}
		
		return bridgeAAA;
	}
}
