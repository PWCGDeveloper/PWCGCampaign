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
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.TruckConvoyBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class MissionTruckConvoyBuilder extends MissionUnitBuilder
{
    private List<IGroundUnitCollection> missionTransportConvoys = new ArrayList<>();

    public MissionTruckConvoyBuilder (Campaign campaign, Mission mission)
    {
        super(campaign, mission);
    }

    
    public List<IGroundUnitCollection> generateMissionTrucks() throws PWCGException 
    {
        buildTruckConvoysForSide(Side.ALLIED);
        buildTruckConvoysForSide(Side.AXIS);
        return missionTransportConvoys;
    }

    public List<IGroundUnitCollection> buildTruckConvoysForSide(Side truckSide) throws PWCGException 
    {
        int maxTrucks = getMaxTruckConvoys(campaign);
        
        ArrayList<Bridge> bridgesForSide = getBridgesForConvoys(truckSide);
        ArrayList<Bridge> sortedBridgesByDistance = sortBridgesDistanceFromMission(bridgesForSide);
        for (Bridge bridge : sortedBridgesByDistance)
        {
            if (missionTransportConvoys.size() >= maxTrucks)
            {
                break;
            }
            
            possibleTruckConvoy(truckSide, bridge);
        }
        
        return missionTransportConvoys;
    }

    private ArrayList<Bridge> getBridgesForConvoys(Side truckSide) throws PWCGException 
    {
        ArrayList<Bridge> bridgesForSide = new ArrayList<Bridge>();
        Campaign campaign = mission.getCampaign();

        GroupManager groupData =  PWCGContext.getInstance().getCurrentMap().getGroupManager();
        for (Bridge bridge : groupData.getBridgeFinder().findAllBridges())
        {
            if (bridge.createCountry(campaign.getDate()).getSide() == truckSide)
            {
                bridgesForSide.add(bridge);
            }
        }
        
        return bridgesForSide;
    }
    
    private ArrayList<Bridge> sortBridgesDistanceFromMission(ArrayList<Bridge> bridgesForSide)
    {
        Map<Double, Bridge> sortedStationsByDistance = new TreeMap<>();
        
        Coordinate missionCenter = mission.getMissionBorders().getCenter();
        for (Bridge bridge : bridgesForSide)
        {
            Double distanceFromMission = MathUtils.calcDist(missionCenter, bridge.getPosition());
            sortedStationsByDistance.put(distanceFromMission, bridge);
        }
        return new ArrayList<Bridge>(sortedStationsByDistance.values());
    }

    private void possibleTruckConvoy(Side truckSide, Bridge bridge) throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            ICountry truckCountry = CountryFactory.makeMapReferenceCountry(truckSide);
            TruckConvoyBuilder groundUnitFactory =  new TruckConvoyBuilder(mission, bridge, truckCountry);
            IGroundUnitCollection truckUnit = groundUnitFactory.createTruckConvoy();
            if (truckUnit != null)
            {
                addTruckConvoy(truckUnit, bridge);
            }
        }
    }


    private int getMaxTruckConvoys(Campaign campaign) throws PWCGException
    {
        int maxTrucks = 3;
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            maxTrucks = 2;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            maxTrucks = 5;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            maxTrucks = 8;
        }
        return maxTrucks;
    }

    private void addTruckConvoy(IGroundUnitCollection truckUnit, Bridge bridge)
    {
        if (!mission.getMissionGroundUnitManager().isBridgeInUse(bridge.getIndex()))
        {
            mission.getMissionGroundUnitManager().registerBridge(bridge);
            missionTransportConvoys.add(truckUnit);
        }
    }
 }
