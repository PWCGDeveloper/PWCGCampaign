package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.builder.ShipTypeChooser;
import pwcg.mission.ground.builder.ShippingUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class MissionShipBuilder
{
    private List<GroundUnitCollection> missionShips = new ArrayList<>();
    private Mission mission;
    private Campaign campaign;

    public MissionShipBuilder(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }
    
    public List<GroundUnitCollection> createMissionShips() throws PWCGException 
    {
        if (PWCGContext.getInstance().getMap(campaign.getCampaignMap()).hasShips())
        {
            makeShips();
        }
        return missionShips;
    }

    public int getUnitCount() 
    {
        int unitCount = 0;
        for (GroundUnitCollection groundUnitCollection : missionShips)
        {
            unitCount += groundUnitCollection.getUnitCount();
            PWCGLogger.log(LogLevel.INFO, "Unit count ships : " + groundUnitCollection.getUnitCount());
        }
        return unitCount;
    }

    private void makeShips() throws PWCGException
    {
        makeShipsForSide(Side.ALLIED);
        makeShipsForSide(Side.AXIS);
    }
    
    private void makeShipsForSide(Side shipSide) throws PWCGException
    {
        for (int i = 0; i < detemineNumbeConvoys(); ++i)
        {
            TargetDefinition targetDefinition = makeTargetDefinition(shipSide);
            Coordinate destination = makeRandomDestination(targetDefinition);
            ShippingUnitBuilder shippingFactory = new ShippingUnitBuilder(mission.getCampaign(), targetDefinition, destination);
            VehicleClass shipType = ShipTypeChooser.chooseShipType(targetDefinition.getCountry().getSide());
            GroundUnitCollection convoy = shippingFactory.createShippingUnit(shipType);
            
            missionShips.add(convoy);
        }
    }

    private Coordinate makeRandomDestination(TargetDefinition targetDefinition) throws PWCGException
    {
        int angle = RandomNumberGenerator.getRandom(360);
        Coordinate destination = MathUtils.calcNextCoord(campaign.getCampaignMap(), targetDefinition.getPosition(), angle, 50000);
        return destination;
    }
    
    private TargetDefinition makeTargetDefinition(Side shipSide) throws PWCGException
    {
        ICountry shipCountry = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroundCountryForMapBySide(shipSide);
        ShippingLane shippingLane = MissionAntiShippingSeaLaneFinder.getShippingLaneForMission(mission.getCampaignMap(), mission.getMissionBorders().getCenter(), shipSide);
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, shippingLane.getShippingLaneBorders().getCoordinateInBox(), shipCountry, "Ship");
        return targetDefinition;
    }
    
    private int detemineNumbeConvoys() throws PWCGException 
    {
        int numShipConvoysPerSide = 1;      
        ConfigManagerCampaign configManager = mission.getCampaign().getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            numShipConvoysPerSide = 1;      
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 60)
            {
                numShipConvoysPerSide = 1;
            }
            else
            {
                numShipConvoysPerSide = 2;
            }
        }
        else
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 30)
            {
                numShipConvoysPerSide = 1;
            }
            else if (roll < 70)
            {
                numShipConvoysPerSide = 2;
            }
            else
            {
                numShipConvoysPerSide = 3;
            }
        }

        return numShipConvoysPerSide;
    }
}
