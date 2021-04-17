package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.shipping.CargoRoute;
import pwcg.campaign.squadron.Squadron;
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

public class SeaBattleBuilder implements IBattleBuilder
{
    private Mission mission;
    
    public SeaBattleBuilder(Mission mission)
    {
        this.mission = mission;
    }

    @Override
    public List<GroundUnitCollection> generateBattle() throws PWCGException
    {
        List<GroundUnitCollection> convoysOnCargoRoute = new ArrayList<>();
        
        for (Side side : mission.getParticipatingPlayers().getMissionPlayerSides())
        {
            CargoRoute cargoRoute = getCargoRoutesForSide(mission, side);
            TargetDefinition targetDefinition = makeTargetDefinition(cargoRoute);        
            ShippingUnitBuilder shippingFactory = new ShippingUnitBuilder(mission.getCampaign(), targetDefinition);
            VehicleClass shipType = ShipTypeChooser.chooseShipType(targetDefinition.getCountry().getSide());
            GroundUnitCollection convoy = shippingFactory.createShippingUnit(shipType);
            if (convoy != null)
            {
                convoysOnCargoRoute.add(convoy);
            }
        }
        return convoysOnCargoRoute;
    }

    private CargoRoute getCargoRoutesForSide(Mission mission, Side side) throws PWCGException
    {
        Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0).getSquadronId());
        Coordinate playerSquadronPosition = squadron.determineCurrentAirfieldAnyMap(mission.getCampaign().getDate()).getPosition();
        CargoRoute cargoRouteForSide = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager().getNearbyCargoShipRouteBySide(playerSquadronPosition, side);
        return cargoRouteForSide;
    }

    private TargetDefinition makeTargetDefinition(CargoRoute cargoRoute) throws PWCGException
    {
        ICountry shipCountry = CountryFactory.makeCountryByCountry(cargoRoute.getCountry());
        Coordinate startPosition = getConvoyStartPosition(cargoRoute);
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, startPosition, shipCountry);
        return targetDefinition;
    }
    
    private Coordinate getConvoyStartPosition(CargoRoute cargoRoute) throws PWCGException
    {
        double routeDistance = MathUtils.calcDist(cargoRoute.getRouteStartPosition(), cargoRoute.getRouteDestination());
        int startPosOnRoute = 0;
        int routeLengthForStart = Double.valueOf(routeDistance).intValue() - 7000;
        if (routeLengthForStart > 0)
        {
            startPosOnRoute = RandomNumberGenerator.getRandom(routeLengthForStart);
        }
        
        double angle = MathUtils.calcAngle(cargoRoute.getRouteStartPosition(), cargoRoute.getRouteDestination());
        Coordinate startPosition = MathUtils.calcNextCoord(cargoRoute.getRouteStartPosition(), angle, startPosOnRoute);
        return startPosition;
    }
}
