package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.shipping.ShipEncounterZone;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.IBattleBuilder;
import pwcg.mission.ground.builder.ShipTypeChooser;
import pwcg.mission.ground.builder.ShippingUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class ShippingEncounterBattleBuilder implements IBattleBuilder
{
    private Mission mission;
    private List<ShipEncounterDefinition> shipEncounterDefinitions = new ArrayList<>();
    
    private class ShipEncounterDefinition
    {
        private TargetDefinition targetDefinition;
        private Coordinate destination;
    }
    
    public ShippingEncounterBattleBuilder(Mission mission)
    {
        this.mission = mission;
    }

    @Override
    public List<GroundUnitCollection> generateBattle() throws PWCGException
    {
        List<GroundUnitCollection> convoysOnShipEncounterZone = new ArrayList<>();
        
        if (mission.getSkirmish() != null && mission.getSkirmish().isShipEncounterZoneBattle())
        {
            ShipEncounterZone shipEncounterZone = getShipEncounterZoneForSkirmish(mission.getSkirmish());
            makeShipEncounterDefinitions(shipEncounterZone);        
            for (ShipEncounterDefinition shipEncounterDefinition : shipEncounterDefinitions)
            {
                ShippingUnitBuilder shippingFactory = new ShippingUnitBuilder(mission.getCampaign(), shipEncounterDefinition.targetDefinition, shipEncounterDefinition.destination);
                VehicleClass shipType = ShipTypeChooser.chooseShipTypeForEncounter();
                GroundUnitCollection convoy = shippingFactory.createShippingUnit(shipType);
                if (convoy != null)
                {
                    convoysOnShipEncounterZone.add(convoy);
                }
            }
        }
        return convoysOnShipEncounterZone;
    }

    private ShipEncounterZone getShipEncounterZoneForSkirmish(Skirmish skirmish) throws PWCGException
    {
        ShipEncounterZone shipEncounterZone = PWCGContext.getInstance().getMap(mission.getCampaignMap()).getShippingLaneManager().getShipEncounterByName(skirmish.getSkirmishName());
        return shipEncounterZone;
    }

    private void makeShipEncounterDefinitions(ShipEncounterZone shipEncounterZone) throws PWCGException
    {
        ICountry alliedMapCountry = PWCGContext.getInstance().getMap(mission.getCampaignMap()).getGroundCountryForMapBySide(Side.ALLIED);
        Coordinate alliedStartPosition = getAlliedStartPosition(shipEncounterZone);
        TargetDefinition alliedTargetDefinition = makeTargetDefinition(shipEncounterZone, alliedMapCountry, alliedStartPosition);
        Coordinate alliedDestination = getDestination(shipEncounterZone, alliedTargetDefinition);
        
        ShipEncounterDefinition alliedShipEncounterDefinition = new ShipEncounterDefinition();
        alliedShipEncounterDefinition.targetDefinition = alliedTargetDefinition;
        alliedShipEncounterDefinition.destination = alliedDestination;
        shipEncounterDefinitions.add(alliedShipEncounterDefinition);
        
        
        ICountry axisMapCountry = PWCGContext.getInstance().getMap(mission.getCampaignMap()).getGroundCountryForMapBySide(Side.AXIS);
        Coordinate axisStartPosition = getAxisStartPosition(shipEncounterZone, alliedStartPosition);
        TargetDefinition axisTargetDefinition = makeTargetDefinition(shipEncounterZone, axisMapCountry, axisStartPosition);
        Coordinate axisDestination = getDestination(shipEncounterZone, axisTargetDefinition);
        
        ShipEncounterDefinition axisShipEncounterDefinition = new ShipEncounterDefinition();
        axisShipEncounterDefinition.targetDefinition = axisTargetDefinition;
        axisShipEncounterDefinition.destination = axisDestination;
        shipEncounterDefinitions.add(axisShipEncounterDefinition);
    }

    private Coordinate getAlliedStartPosition(ShipEncounterZone shipEncounterZone) throws PWCGException
    {
        int distance = 2000 + RandomNumberGenerator.getRandom(2000);
        double angle = RandomNumberGenerator.getRandom(360);
        Coordinate startPosition = MathUtils.calcNextCoord(mission.getCampaignMap(), shipEncounterZone.getEncounterPoint(), angle, distance);
        return startPosition;
    }
    
    private Coordinate getAxisStartPosition(ShipEncounterZone shipEncounterZone, Coordinate alliedStartPosition) throws PWCGException
    {
        double alliedAngle = MathUtils.calcAngle(shipEncounterZone.getEncounterPoint(), alliedStartPosition);
        double axisAngle = MathUtils.adjustAngle(alliedAngle, 180);
        int furtherOffset = RandomNumberGenerator.getRandom(180) - 90;
        axisAngle = MathUtils.adjustAngle(axisAngle, furtherOffset);

        int distance = 2000 + RandomNumberGenerator.getRandom(2000);
        Coordinate startPosition = MathUtils.calcNextCoord(mission.getCampaignMap(), shipEncounterZone.getEncounterPoint(), axisAngle, distance);
        return startPosition;
    }

    private Coordinate getDestination (ShipEncounterZone shipEncounterZone, TargetDefinition targetDefinition) throws PWCGException
    {
        double angleToDestination = MathUtils.calcAngle(targetDefinition.getPosition(), shipEncounterZone.getEncounterPoint());
        Coordinate startPosition = MathUtils.calcNextCoord(mission.getCampaignMap(), shipEncounterZone.getEncounterPoint(), angleToDestination, 4000);
        return startPosition;
    }
    
    private TargetDefinition makeTargetDefinition(ShipEncounterZone shipEncounterZone, ICountry shipCountry, Coordinate startPosition) throws PWCGException
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, startPosition, shipCountry, "Warship");
        return targetDefinition;
    }
}
