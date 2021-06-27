package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.transport.ShipCargoConvoyUnit;
import pwcg.mission.ground.unittypes.transport.ShipSubmarineConvoyUnit;
import pwcg.mission.ground.unittypes.transport.ShipWarshipConvoyUnit;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class ShippingUnitBuilder
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    private Coordinate destination;
    
    public ShippingUnitBuilder (Campaign campaign, TargetDefinition targetDefinition, Coordinate destination)
    {
        this.campaign = campaign;
        this.targetDefinition  = targetDefinition;
        this.destination  = destination;
    }

    public  GroundUnitCollection createShippingUnit (VehicleClass shipType) throws PWCGException 
    {
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit();

        IGroundUnit shipGroup = null;
        IGroundUnit shipGroupEscort = null;
        if (shipType == VehicleClass.ShipCargo)
        {
            shipGroup = new ShipCargoConvoyUnit(groundUnitInformation);
            shipGroup.createGroundUnit();
            
            GroundUnitInformation escortGroundUnitInformation = createEscortGroundUnitInformationForUnit();
            shipGroupEscort = new ShipWarshipConvoyUnit(escortGroundUnitInformation);
            shipGroupEscort.createGroundUnit();
        }
        else if (shipType == VehicleClass.ShipWarship)
        {
            shipGroup = new ShipWarshipConvoyUnit(groundUnitInformation);
            shipGroup.createGroundUnit();
        }
        else if (shipType == VehicleClass.Submarine)
        {
            shipGroup = new ShipSubmarineConvoyUnit(groundUnitInformation);
            shipGroup.createGroundUnit();
        }
        else
        {
            throw new PWCGException("Invalid vehicle type for ship builder " + shipType);
        }
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Ships", 
                TargetType.TARGET_SHIPPING,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        GroundUnitCollection convoyGroundUnitCollection = new GroundUnitCollection (campaign, "Shipping", groundUnitCollectionData);
        convoyGroundUnitCollection.addGroundUnit(shipGroup);
        if (shipGroupEscort != null)
        {
            convoyGroundUnitCollection.addGroundUnit(shipGroupEscort);
        }
        convoyGroundUnitCollection.setPrimaryGroundUnit(shipGroup);
        convoyGroundUnitCollection.finishGroundUnitCollection();

        return convoyGroundUnitCollection;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit() throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                targetDefinition.getCountry(), 
                TargetType.TARGET_SHIPPING,
                targetDefinition.getPosition(), 
                destination,
                Orientation.createRandomOrientation());

        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

    private GroundUnitInformation createEscortGroundUnitInformationForUnit() throws PWCGException
    {
        double angleAheadOfCargoShips = MathUtils.calcAngle(targetDefinition.getPosition(), destination);
        Coordinate escortStartPosition = MathUtils.calcNextCoord(targetDefinition.getPosition(), angleAheadOfCargoShips, 2000);

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                targetDefinition.getCountry(), 
                TargetType.TARGET_SHIPPING,
                escortStartPosition, 
                destination,
                Orientation.createRandomOrientation());

        groundUnitInformation.setDestination(destination);
        return groundUnitInformation;
    }

}
