package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.shipping.CargoShipRoute;
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

public class CargoRouteBattleBuilder implements IBattleBuilder
{
    private Mission mission;
    
    public CargoRouteBattleBuilder(Mission mission)
    {
        this.mission = mission;
    }

    @Override
    public List<GroundUnitCollection> generateBattle() throws PWCGException
    {
        List<GroundUnitCollection> convoysOnCargoRoute = new ArrayList<>();
        
        if (mission.getSkirmish() != null && mission.getSkirmish().isCargoRouteBattle())
        {
            CargoShipRoute cargoRoute = getCargoRoutesForSkirmish(mission.getSkirmish());
            TargetDefinition targetDefinition = makeTargetDefinition(cargoRoute);        
            ShippingUnitBuilder shippingFactory = new ShippingUnitBuilder(mission.getCampaign(), targetDefinition, cargoRoute.getRouteDestination());
            VehicleClass shipType = ShipTypeChooser.chooseShipType(targetDefinition.getCountry().getSide());
            GroundUnitCollection convoy = shippingFactory.createShippingUnit(shipType);
            if (convoy != null)
            {
                convoysOnCargoRoute.add(convoy);
            }
        }
        return convoysOnCargoRoute;
    }

    private CargoShipRoute getCargoRoutesForSkirmish(Skirmish skirmish) throws PWCGException
    {
        CargoShipRoute cargoRouteForSide = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager().getCargoShipRouteByName(skirmish.getSkirmishName());
        return cargoRouteForSide;
    }

    private TargetDefinition makeTargetDefinition(CargoShipRoute cargoRoute) throws PWCGException
    {
        ICountry shipCountry = CountryFactory.makeCountryByCountry(cargoRoute.getCountry());
        Coordinate startPosition = getConvoyStartPosition(cargoRoute);
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, startPosition, shipCountry, "Cargo Ship");
        return targetDefinition;
    }
    
    private Coordinate getConvoyStartPosition(CargoShipRoute cargoRoute) throws PWCGException
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
