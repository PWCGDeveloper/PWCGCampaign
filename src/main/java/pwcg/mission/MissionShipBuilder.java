package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.builder.ShipTypeChooser;
import pwcg.mission.ground.builder.ShippingUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class MissionShipBuilder
{
    private List<IGroundUnitCollection> missionShips = new ArrayList<>();
    private Mission mission;

    public MissionShipBuilder(Mission mission)
    {
        this.mission = mission;
    }
    
    public List<IGroundUnitCollection> createMissionShips() throws PWCGException 
    {
        if (shouldMakeShips())
        {
            makeShips();
        }
        return missionShips;
    }

    public int getUnitCount() 
    {
        int missionBalloonUnitCount = 0;
        for (IGroundUnitCollection groundUnitCollection : missionShips)
        {
            missionBalloonUnitCount += groundUnitCollection.getUnitCount();
            System.out.println("Unit count balloon : " + groundUnitCollection.getUnitCount());
        }
        return missionBalloonUnitCount;
    }

    private boolean shouldMakeShips() throws PWCGException
    {
        if (PWCGContext.getInstance().getCurrentMap().getMapIdentifier() == FrontMapIdentifier.KUBAN_MAP)
        {
            return true;
        }

        return false;
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
            ShippingUnitBuilder shippingFactory = new ShippingUnitBuilder(mission.getCampaign(), targetDefinition);
            VehicleClass shipType = ShipTypeChooser.chooseShipType(targetDefinition.getCountry().getSide());
            IGroundUnitCollection convoy = shippingFactory.createShippingUnit(shipType);
            
            missionShips.add(convoy);
        }
    }

    private TargetDefinition makeTargetDefinition(Side shipSide) throws PWCGException
    {
        ICountry shipCountry = PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(shipSide);
        ShippingLane shippngLane = MissionAntiShippingSeaLaneFinder.getShippingLaneForMission(mission, shipSide);
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, shippngLane.getShippingLaneBorders().getCoordinateInBox(), shipCountry);
        return targetDefinition;
    }
    
    private int detemineNumbeConvoys() throws PWCGException 
    {
        int numShipCOnvoysPerSide = 1;      
        ConfigManagerCampaign configManager = mission.getCampaign().getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            numShipCOnvoysPerSide = 1;      
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 60)
            {
                numShipCOnvoysPerSide = 1;
            }
            else
            {
                numShipCOnvoysPerSide = 2;
            }
        }
        else
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 30)
            {
                numShipCOnvoysPerSide = 1;
            }
            else if (roll < 70)
            {
                numShipCOnvoysPerSide = 2;
            }
            else
            {
                numShipCOnvoysPerSide = 3;
            }
        }

        return numShipCOnvoysPerSide;
    }
}
