package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.transport.ShipCargoConvoyUnit;
import pwcg.mission.ground.unittypes.transport.ShipSubmarineConvoyUnit;
import pwcg.mission.ground.unittypes.transport.ShipWarshipConvoyUnit;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;

public class ShippingUnitBuilder
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public ShippingUnitBuilder (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public IGroundUnitCollection createShippingUnit () throws PWCGException 
    {
        VehicleClass shipType = chooseShipType();
        IGroundUnitCollection shipConvoyUnit = generateConvoy(shipType);
        return shipConvoyUnit;
    }


    public IGroundUnitCollection generateConvoy(VehicleClass shipType) throws PWCGException 
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit();
        
        IGroundUnit shipGroup = null;
        if (shipType == VehicleClass.ShipCargo)
        {
            shipGroup = new ShipCargoConvoyUnit(groundUnitInformation);
            shipGroup.createGroundUnit();
        }
        else if (shipType == VehicleClass.ShipWarship)
        {
            shipGroup = new ShipWarshipConvoyUnit(groundUnitInformation);
            shipGroup.createGroundUnit();
        }
        else
        {
            shipGroup = new ShipSubmarineConvoyUnit(groundUnitInformation);
            shipGroup.createGroundUnit();
        }
        
        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection(GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, "Ships", Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));
        groundUnitCollection.addGroundUnit(shipGroup);
        groundUnitCollection.finishGroundUnitCollection();
        return groundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit() throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, targetDefinition);
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
