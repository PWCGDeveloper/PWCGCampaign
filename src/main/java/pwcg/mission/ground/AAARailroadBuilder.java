package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.factory.AAAUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class AAARailroadBuilder 
{
	private Campaign campaign;
	private List<IGroundUnitCollection> railroadAAA = new ArrayList<>();

	
    public AAARailroadBuilder(Campaign campaign) throws PWCGException
	{
        this.campaign = campaign;
	}

	public List<IGroundUnitCollection> createAAAForRailroads() throws PWCGException
	{
        GroupManager groupData = PWCGContext.getInstance().getCurrentMap().getGroupManager();

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
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, railroadStation.getCountry(campaign.getDate()), aaaPosition);
        IGroundUnitCollection aaaMg = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        railroadAAA.add(aaaMg);
    }

    private void createRailroadAAAArtillery(Block railroadStation) throws PWCGException
    {
        double angle = RandomNumberGenerator.getRandom(360);
        double distance = 300 + RandomNumberGenerator.getRandom(200);
        Coordinate aaaPosition = MathUtils.calcNextCoord(railroadStation.getPosition(), angle, distance);               
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, railroadStation.getCountry(campaign.getDate()), aaaPosition);
        IGroundUnitCollection aaaArty = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        railroadAAA.add(aaaArty);
    }
}
