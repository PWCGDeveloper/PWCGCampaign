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
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;

public class AAARailroadBuilder 
{
	private Campaign campaign;
	private List<GroundUnitSpawning> railroadAAA = new ArrayList<>();

	
    public AAARailroadBuilder(Campaign campaign) throws PWCGException
	{
        this.campaign = campaign;
	}

	public List<GroundUnitSpawning> createAAAForRailroads() throws PWCGException
	{
        GroupManager groupData = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();

		for (Block railroadStation : groupData.getRailroadList())
		{
	        ICountry country = railroadStation.createCountry(campaign.getDate());
	        if (!country.isNeutral())
	        {
	            createRailroadAAAMG(railroadStation);
                createRailroadAAAArtillery(railroadStation);
	        }
		}
		
		return railroadAAA;
	}

    private void createRailroadAAAMG(Block railroadStation) throws PWCGException
    {
        double angle = RandomNumberGenerator.getRandom(360);
        double distance = 100 + RandomNumberGenerator.getRandom(200);
        Coordinate aaaPosition = MathUtils.calcNextCoord(railroadStation.getPosition(), angle, distance);               
        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign, railroadStation.getCountry(campaign.getDate()), aaaPosition);
        GroundUnitSpawning aaaMg = groundUnitFactory.createAAAMGBattery(2, 2);
        railroadAAA.add(aaaMg);
    }

    private void createRailroadAAAArtillery(Block railroadStation) throws PWCGException
    {
        double angle = RandomNumberGenerator.getRandom(360);
        double distance = 300 + RandomNumberGenerator.getRandom(200);
        Coordinate aaaPosition = MathUtils.calcNextCoord(railroadStation.getPosition(), angle, distance);               
        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign, railroadStation.getCountry(campaign.getDate()), aaaPosition);
        GroundUnitSpawning aaaArty = groundUnitFactory.createAAAArtilleryBattery(2, 2);
        railroadAAA.add(aaaArty);
    }
}
