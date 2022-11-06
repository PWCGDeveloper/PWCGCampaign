package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.Arrays;
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
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.TrainUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;

public class MissionTrainBuilder extends MissionUnitBuilder
{
    private List<GroundUnitCollection> missionTrains = new ArrayList<>();

    public MissionTrainBuilder (Campaign campaign, Mission mission)
    {
        super(campaign, mission);
    }
    
    public List<GroundUnitCollection> generateMissionTrains() throws PWCGException 
    {
        missionTrains.addAll(buildTrainsForSide(Side.ALLIED));
        missionTrains.addAll(buildTrainsForSide(Side.AXIS));
        return missionTrains;
    }

    private List<GroundUnitCollection> buildTrainsForSide(Side trainSide) throws PWCGException
    {
        List<GroundUnitCollection> missionTrainsForSide = new ArrayList<>();
        int maxTrains = getMaxTrains();
        ArrayList<Block> stationsForSide = getRailroadsForTrains(trainSide);
        List<Block> sortedStationsByDistance = sortTrainStationsByDistanceFromMission(stationsForSide);
        for (Block station  : sortedStationsByDistance)
        {
            if (missionTrainsForSide.size() >= maxTrains)
            {
                break;
            }
            
            GroundUnitCollection trainUnit = makeTrain(trainSide, station);
            missionTrainsForSide.add(trainUnit);
        }
        return missionTrainsForSide;
    }

    private ArrayList<Block> getRailroadsForTrains(Side trainSide) throws PWCGException 
    {
        ArrayList<Block> stationsForSide = new ArrayList<Block>();
        Campaign campaign = mission.getCampaign();
 
        GroupManager groupData =  PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager();
        for (Block station : groupData.getRailroadList())
        {
            if (trainSide == station.determineCountryOnDate(campaign.getCampaignMap(), campaign.getDate()).getSide())
            {
                stationsForSide.add(station);
            }
        }
        
        return stationsForSide;
    }
    
    private List<Block> sortTrainStationsByDistanceFromMission(ArrayList<Block> stationsForSide) throws PWCGException
    {        
        Map<Double, Block> sortedStationsByDistance = new TreeMap<>();
        Coordinate missionCenter = mission.getMissionBorders().getCenter();
        for (Block station : stationsForSide)
        {
            Double distanceFromMission = MathUtils.calcDist(missionCenter, station.getPosition());
            sortedStationsByDistance.put(distanceFromMission, station);
        }
        
        CoordinateBox expandedToFirstStation = CoordinateBox.copy(mission.getMissionBorders());
        for (Block station : sortedStationsByDistance.values())
        {
            expandedToFirstStation.expandBoxCornersFromCoordinates(Arrays.asList(station.getPosition()));
            expandedToFirstStation.expandBox(1000);
            break;
        }
                
        List<Block> inRangeTrains = new ArrayList<>();
        for (Block station : sortedStationsByDistance.values())
        {
            if (expandedToFirstStation.isInBox(station.getPosition()))
            {
                inRangeTrains.add(station);
            }
        }

        return inRangeTrains;
    }

    private GroundUnitCollection makeTrain(Side trainSide, Block station) throws PWCGException
    {
        ICountry trainCountry = CountryFactory.makeMapReferenceCountry(campaign.getCampaignMap(), trainSide);
        TrainUnitBuilder trainfactory =  new TrainUnitBuilder(campaign, station, trainCountry);
        GroundUnitCollection trainUnit = trainfactory.createTrainUnit();
        return trainUnit;
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
 }
