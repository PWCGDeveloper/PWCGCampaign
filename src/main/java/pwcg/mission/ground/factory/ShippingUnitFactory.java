package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.CoalitionFactory;

public class ShippingUnitFactory
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public ShippingUnitFactory (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public ShipConvoyUnit createShippingUnit () throws PWCGException 
    {
        VehicleClass shipType = chooseShipType();
        ShipConvoyUnit shipConvoyUnit = generateConvoy(shipType);
        return shipConvoyUnit;
    }


    public ShipConvoyUnit generateConvoy(VehicleClass shipType) throws PWCGException 
    {
        MissionBeginUnitCheckZone missionBeginUnitShips = createMissionBegin();        
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit(missionBeginUnitShips);
        
        ShipConvoyUnit shipGroup = new ShipConvoyUnit(campaign, groundUnitInformation, shipType);
        shipGroup.createUnitMission();
        
        return shipGroup;
    }

    private MissionBeginUnitCheckZone createMissionBegin() throws PWCGException
    {
        Coalition enemyCoalition  = CoalitionFactory.getCoalitionBySide(targetDefinition.getTargetCountry().getSide().getOppositeSide());
        MissionBeginUnitCheckZone missionBeginUnitShips = new MissionBeginUnitCheckZone(targetDefinition.getTargetPosition(), 30000);
        missionBeginUnitShips.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalition(enemyCoalition);
        return missionBeginUnitShips;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit(MissionBeginUnitCheckZone missionBeginUnit) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, missionBeginUnit, targetDefinition);
        int angle = RandomNumberGenerator.getRandom(360);
        Coordinate destination = MathUtils.calcNextCoord(targetDefinition.getTargetPosition(), angle, 50000);
        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private VehicleClass chooseShipType()
    {
        int shipTypeRoll = RandomNumberGenerator.getRandom(100);
        VehicleClass shipType = VehicleClass.ShipCargo;
        if (targetDefinition.getTargetCountry().getSide() == Side.AXIS)
        {
            if (shipTypeRoll < 20)
            {
                shipType = VehicleClass.Submarine;
            }
            else if (shipTypeRoll < 85)
            {
                shipType = VehicleClass.ShipCargo;
            }
            else
            {
                shipType = VehicleClass.ShipWarship;
            }
        }
        else
        {
            if (shipTypeRoll < 10)
            {
                shipType = VehicleClass.Submarine;
            }
            else if (shipTypeRoll < 70)
            {
                shipType = VehicleClass.ShipCargo;
            }
            else
            {
                shipType = VehicleClass.ShipWarship;
            }
        }
        return shipType;
    }

}
