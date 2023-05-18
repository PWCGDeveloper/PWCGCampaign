package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AAARailroadBuilder implements buildRailroadAAA 
{
	private Campaign campaign;
	private List<GroundUnitCollection> railroadAAA = new ArrayList<>();

	
    public AAARailroadBuilder(Campaign campaign) throws PWCGException
	{
        this.campaign = campaign;
	}

	public List<GroundUnitCollection> createAAAForRailroads() throws PWCGException
	{
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentAASetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigAAKey);
        if (!currentAASetting.equals(ConfigSimple.CONFIG_LEVEL_ULTRA_LOW))
        {
            createRailroadAAA();
        }
		
		return railroadAAA;
	}

    private void createRailroadAAA() throws PWCGException
    {
        GroupManager groupData = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager();

		for (Block railroadStation : groupData.getRailroadList())
		{
	        ICountry country = railroadStation.determineCountryOnDate(campaign.getCampaignMap(), campaign.getDate());
	        if (!country.isNeutral())
	        {
	            createRailroadAAAMG(railroadStation);
                createRailroadAAAArtillery(railroadStation);
	        }
		}
    }

    private void createRailroadAAAMG(Block railroadStation) throws PWCGException
    {
        ICountry country = railroadStation.getCountry(campaign.getCampaignMap(), campaign.getDate());
        if (country.isNeutral())
        {
            return;
        }
        
        double angle = RandomNumberGenerator.getRandom(360);
        double distance = 100 + RandomNumberGenerator.getRandom(200);
        Coordinate aaaPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), railroadStation.getPosition(), angle, distance);               
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, aaaPosition, country, "AAA For Railroad");
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
        GroundUnitCollection aaaMg = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        railroadAAA.add(aaaMg);
    }

    private void createRailroadAAAArtillery(Block railroadStation) throws PWCGException
    {
        double angle = RandomNumberGenerator.getRandom(360);
        double distance = 300 + RandomNumberGenerator.getRandom(200);
        Coordinate aaaPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), railroadStation.getPosition(), angle, distance);
        ICountry country =  railroadStation.getCountry(campaign.getCampaignMap(), campaign.getDate());
        if (country.isNeutral())
        {
            return;
        }
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, aaaPosition, country, "AAA For Railroad");
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
        GroundUnitCollection aaaArty = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        railroadAAA.add(aaaArty);
    }
}
