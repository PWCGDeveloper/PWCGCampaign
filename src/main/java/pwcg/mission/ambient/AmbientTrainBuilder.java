package pwcg.mission.ambient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.TargetSide;
import pwcg.mission.ground.builder.TrainUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderGround;
import pwcg.mission.target.TargetType;

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
        
        ArrayList<Block> stationsForSide = getRailroadsForAmbentTrains(targetSide);
        ArrayList<Block> sortedStationsByDistance = sortTrainStationsByDistanceFromMission(stationsForSide);
        for (Block station : sortedStationsByDistance)
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
        ArrayList<Block> stationsForSide = new ArrayList<Block>();

        Campaign campaign = mission.getCampaign();
 
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        for (Block station : groupData.getRailroadList())
        {
            if (targetSide == station.createCountry(campaign.getDate()).getSide())
            {
                stationsForSide.add(station);
            }
        }
        
        return stationsForSide;
    }
    
    private ArrayList<Block> sortTrainStationsByDistanceFromMission(ArrayList<Block> stationsForSide)
    {
        Map<Double, Block> sortedStationsByDistance = new TreeMap<>();
        
        Coordinate missionCenter = mission.getMissionBorders().getCenter();
        for (Block station : stationsForSide)
        {
            Double distanceFromMission = MathUtils.calcDist(missionCenter, station.getPosition());
            sortedStationsByDistance.put(distanceFromMission, station);
        }
        return new ArrayList<Block>(sortedStationsByDistance.values());
    }

    private void possibleAmbientTrain(Side targetSide, Block station) throws PWCGException
    {
        boolean isPlayerTarget = false;
        ICountry trainCountry = CountryFactory.makeMapReferenceCountry(targetSide);
        TargetDefinitionBuilderGround targetDefinitionBuilder = new TargetDefinitionBuilderGround(campaign);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionAmbient(
                trainCountry, TargetType.TARGET_TRAIN, station.getPosition(), station.getOrientation(), isPlayerTarget);

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
            maxTrains = 2;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            maxTrains = 4;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            maxTrains = 6;
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
