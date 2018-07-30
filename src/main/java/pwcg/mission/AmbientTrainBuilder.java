package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.TargetDefinitionBuilder;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.ground.factory.TrainUnitFactory;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;

public class AmbientTrainBuilder extends AmbientUnitBuilder
{
    private List<GroundTrainUnit> ambientTrains = new ArrayList<GroundTrainUnit>();

    public AmbientTrainBuilder (Campaign campaign, Mission mission)
    {
        super(campaign, mission);
    }
    
    public List<GroundTrainUnit> generateAmbientTrains() throws PWCGException 
    {
        int maxTrains = getMaxAmbientTrains();
        Side enemySide = campaign.determineCountry().getSide().getOppositeSide();
        ICountry trainCountry = CountryFactory.makeMapReferenceCountry(enemySide);
        
        for (Block station : getRailroadsForAmbentTrains())
        {
            if (ambientTrains.size() >= maxTrains)
            {
                break;
            }

            possibleAmbientTrain(enemySide, trainCountry, station);
        }
        
        return ambientTrains;
    }

    private void possibleAmbientTrain(Side enemySide, ICountry trainCountry, Block station) throws PWCGException
    {
        if (station.getCountry(campaign.getDate()).getSide() == enemySide)
        {
            boolean isPlayerTarget = true;
            TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder();
            TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionNoFlight(campaign, trainCountry, TacticalTarget.TARGET_TRAIN, station.getPosition(), isPlayerTarget);

            TrainUnitFactory trainfactory =  new TrainUnitFactory(campaign, targetDefinition);
            GroundTrainUnit trainUnit = trainfactory.createTrainUnit();
            addAmbientTrain(trainUnit, station);
        }
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

    private ArrayList<Block> getRailroadsForAmbentTrains() throws PWCGException 
    {
        ArrayList<Block> selectedStations = new ArrayList<Block>();

        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();

        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);        
        CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(keepGroupSpread);

        GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        for (Block station : groupData.getRailroadList())
        {
            if (campaign.determineCountry().isEnemy(station.createCountry(campaign.getDate())))
            {
                if (missionBorders.isInBox(station.getPosition()))
                {
                    selectedStations.add(station);
                }
            }
        }
        
        return selectedStations;
    }

    private void addAmbientTrain(GroundTrainUnit trainUnit, Block station)
    {
        if (!mission.getMissionGroundUnitManager().isTrainStationInUse(station.getIndex()))
        {
            mission.getMissionGroundUnitManager().registerTrainStation(station);
            ambientTrains.add(trainUnit);
        }
    }
 }
