package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;

public class AAABridgeBuilder 
{
	private Campaign campaign;
	
	public AAABridgeBuilder(Campaign campaign) throws PWCGException
	{
		this.campaign = campaign;
	}

	public List<GroundUnitSpawning> createAAAForBridges() throws PWCGException
	{
		List<GroundUnitSpawning> bridgeAAA = new ArrayList<>();
        GroupManager groupData = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();

		for (Bridge bridge : groupData.getBridgeFinder().findAllBridges())
		{
	        ICountry country = bridge.createCountry(campaign.getDate());
	        if (!country.isNeutral())
	        {
	            double angle = RandomNumberGenerator.getRandom(360);
	            double distance = 100 + RandomNumberGenerator.getRandom(400);
	            Coordinate aaaPosition = MathUtils.calcNextCoord(bridge.getPosition(), angle, distance);
	            
	            GroundUnitAAAFactory groundUnitFactory = new GroundUnitAAAFactory(bridge.getCountry(campaign.getDate()), aaaPosition);
	            GroundUnitSpawning aaaArty = groundUnitFactory.createAAAArtilleryBattery(3);
	            bridgeAAA.add(aaaArty);
	        }
		}
		
		return bridgeAAA;
	}
}
