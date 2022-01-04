package pwcg.campaign.shipping;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skirmish.SkirmishDistance;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionHumanParticipants;

public class CargoRouteManager
{
    private static int MINIMUM_DISTANCE_TO_DESTINATION = 7000;
    
    public static CargoRoute getCargoRouteForSide (Campaign campaign, MissionHumanParticipants participatingPlayers, Side side) throws PWCGException
    {
        Company squadron =  PWCGContext.getInstance().getCompanyManager().getCompany(participatingPlayers.getAllParticipatingPlayers().get(0).getCompanyId());
        Coordinate playerSquadronPosition = squadron.determineCurrentAirfieldAnyMap(campaign.getDate()).getPosition();
        CargoRoute cargoRouteForSide = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager().getNearbyCargoShipRouteBySide(campaign.getDate(), playerSquadronPosition, side);
        
        if (cargoRouteForSide != null)
        {
            Coordinate routeStartPosition = getInRangeStartPosition(cargoRouteForSide, playerSquadronPosition);
            cargoRouteForSide.setRouteStartPosition(routeStartPosition);
        }
        
        return cargoRouteForSide;
    }
    
    private static Coordinate getInRangeStartPosition(CargoRoute cargoRoute, Coordinate playerSquadronPosition) throws PWCGException
    {
        Coordinate routeStartPosition = getConvoyInitialStartPosition(cargoRoute);
        double distanceFromPlayer = MathUtils.calcDist(routeStartPosition, playerSquadronPosition);
        while (distanceFromPlayer > SkirmishDistance.findMaxSkirmishDistance())
        {
            double angle = MathUtils.calcAngle(routeStartPosition, cargoRoute.getRouteDestination());
            routeStartPosition = MathUtils.calcNextCoord(routeStartPosition.copy(), angle, 5000.0);
            
            double adjustedDistanceFromPlayer = MathUtils.calcDist(routeStartPosition, playerSquadronPosition);
            if (adjustedDistanceFromPlayer > distanceFromPlayer)
            {
                PWCGLogger.log(LogLevel.ERROR, "Moving away from player.  Using destination");
                return cargoRoute.getRouteDestination();
            }
            else
            {
                distanceFromPlayer = adjustedDistanceFromPlayer;
            }
        }
        return routeStartPosition;
    }

    private static Coordinate getConvoyInitialStartPosition(CargoRoute cargoRoute) throws PWCGException
    {
        double routeDistance = MathUtils.calcDist(cargoRoute.getRouteStartPosition(), cargoRoute.getRouteDestination());
        int startPosOnRoute = 0;
        int routeLengthForStart = Double.valueOf(routeDistance).intValue() - MINIMUM_DISTANCE_TO_DESTINATION;
        if (routeLengthForStart > 0)
        {
            startPosOnRoute = RandomNumberGenerator.getRandom(routeLengthForStart);
        }
        
        double angle = MathUtils.calcAngle(cargoRoute.getRouteStartPosition(), cargoRoute.getRouteDestination());
        Coordinate startPosition = MathUtils.calcNextCoord(cargoRoute.getRouteStartPosition(), angle, startPosOnRoute);
        return startPosition;
    }
}
