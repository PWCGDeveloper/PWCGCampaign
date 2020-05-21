package pwcg.mission.ground;

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
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.TrainUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class MissionTrainBuilder extends MissionUnitBuilder
{
    private List<IGroundUnitCollection> missionTrains = new ArrayList<>();

    public MissionTrainBuilder (Campaign campaign, Mission mission)
    {
        super(campaign, mission);
    }
    
    public List<IGroundUnitCollection> generateMissionTrains() throws PWCGException 
    {
        buildTrainsForSide(Side.ALLIED);
        buildTrainsForSide(Side.AXIS);
        return missionTrains;
    }

    private void buildTrainsForSide(Side trainSide) throws PWCGException
    {
        int maxTrains = getMaxTrains();
        ArrayList<Block> stationsForSide = getRailroadsForTrains(trainSide);
        ArrayList<Block> sortedStationsByDistance = sortTrainStationsByDistanceFromMission(stationsForSide);
        for (Block station : sortedStationsByDistance)
        {
            if (missionTrains.size() >= maxTrains)
            {
                break;
            }

            possibleTrain(trainSide, station);
        }
    }

    private ArrayList<Block> getRailroadsForTrains(Side trainSide) throws PWCGException 
    {
        ArrayList<Block> stationsForSide = new ArrayList<Block>();
        Campaign campaign = mission.getCampaign();
 
        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        for (Block station : groupData.getRailroadList())
        {
            if (trainSide == station.createCountry(campaign.getDate()).getSide())
            {
                if (!mission.getMissionGroundUnitManager().isTrainStationInUse(station.getIndex()))
                {
                    stationsForSide.add(station);
                }
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

    private void possibleTrain(Side trainSide, Block station) throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            ICountry trainCountry = CountryFactory.makeMapReferenceCountry(trainSide);
            TrainUnitBuilder trainfactory =  new TrainUnitBuilder(mission, station, trainCountry);
            IGroundUnitCollection trainUnit = trainfactory.createTrainUnit();
            addTrain(trainUnit, station);
        }
    }

	private int getMaxTrains() throws PWCGException
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

    private void addTrain(IGroundUnitCollection trainUnit, Block station) throws PWCGException
    {
        mission.getMissionGroundUnitManager().registerTrainStation(station.getPosition());
        missionTrains.add(trainUnit);
    }
 }
