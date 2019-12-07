package pwcg.mission.ambient;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.TargetSide;
import pwcg.mission.ground.factory.TrainUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderGround;

public class AmbientTrainBuilder extends AmbientUnitBuilder
{
    private List<IGroundUnitCollection> ambientTrains = new ArrayList<>();

    public AmbientTrainBuilder (Campaign campaign, Mission mission)
    {
        super(campaign, mission);
    }
    
    public List<IGroundUnitCollection> generateAmbientTrains() throws PWCGException 
    {
        Side targetSide = TargetSide.ambientTargetSide(campaign);
        int maxTrains = getMaxAmbientTrains();
        
        for (Block station : getRailroadsForAmbentTrains(targetSide))
        {
            if (ambientTrains.size() >= maxTrains)
            {
                break;
            }

            possibleAmbientTrain(targetSide, station);
        }
        
        return ambientTrains;
    }

    private ArrayList<Block> getRailroadsForAmbentTrains(Side targetSide) throws PWCGException 
    {
        ArrayList<Block> selectedStations = new ArrayList<Block>();

        Campaign campaign = PWCGContext.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();

        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);        
        CoordinateBox missionBorders = mission.getMissionBorders().expandBox(keepGroupSpread);

        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        for (Block station : groupData.getRailroadList())
        {
            if (targetSide == station.createCountry(campaign.getDate()).getSide())
            {
                if (missionBorders.isInBox(station.getPosition()))
                {
                    selectedStations.add(station);
                }
            }
        }
        
        return selectedStations;
    }

    private void possibleAmbientTrain(Side targetSide, Block station) throws PWCGException
    {
        boolean isPlayerTarget = true;
        ICountry trainCountry = CountryFactory.makeMapReferenceCountry(targetSide);
        TargetDefinitionBuilderGround targetDefinitionBuilder = new TargetDefinitionBuilderGround(campaign);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionAmbient(trainCountry, TacticalTarget.TARGET_TRAIN, station.getPosition(), isPlayerTarget);

        TrainUnitBuilder trainfactory =  new TrainUnitBuilder(mission, targetDefinition);
        IGroundUnitCollection trainUnit = trainfactory.createTrainUnit();
        addAmbientTrain(trainUnit, station);
    }

	private int getMaxAmbientTrains() throws PWCGException
	{
		int maxTrains = 3;
        
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            maxTrains = 1;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            maxTrains = 2;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            maxTrains = 3;
        }
		return maxTrains;
	}

    private void addAmbientTrain(IGroundUnitCollection trainUnit, Block station)
    {
        if (!mission.getMissionGroundUnitManager().isTrainStationInUse(station.getIndex()))
        {
            mission.getMissionGroundUnitManager().registerTrainStation(station);
            ambientTrains.add(trainUnit);
        }
    }
 }
