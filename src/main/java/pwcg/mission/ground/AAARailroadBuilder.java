package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;

public class AAARailroadBuilder 
{
	private Campaign campaign;

    public AAARailroadBuilder(Campaign campaign) throws PWCGException
	{
        this.campaign = campaign;
	}

	public List<GroundUnitSpawning> createAAAForRailroads() throws PWCGException
	{
		List<GroundUnitSpawning> railroadAAA = new ArrayList<>();
        GroupManager groupData = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();

		for (Block railroadStation : groupData.getRailroadList())
		{
	        ICountry country = railroadStation.createCountry(campaign.getDate());
	        if (!country.isNeutral())
	        {
	            double angle = RandomNumberGenerator.getRandom(360);
	            double distance = 100 + RandomNumberGenerator.getRandom(400);
	            Coordinate aaaPosition = MathUtils.calcNextCoord(railroadStation.getPosition(), angle, distance);
	            
	            GroundUnitAAAFactory groundUnitFactory =  new GroundUnitAAAFactory(railroadStation.getCountry(campaign.getDate()), aaaPosition);
	            GroundUnitSpawning aaaArty = groundUnitFactory.createAAAArtilleryBattery(3);
	            railroadAAA.add(aaaArty);
	        }
		}
		
		return railroadAAA;
	}
}
