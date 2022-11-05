package pwcg.campaign.shipping;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skirmish.TargetDistance;
import pwcg.campaign.squadron.Squadron;
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
    
    public static CargoShipRoute getCargoRouteForSide (Campaign campaign, MissionHumanParticipants participatingPlayers, Side side) throws PWCGException
    {
        Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(participatingPlayers.getAllParticipatingPlayers().get(0).getSquadronId());
        CargoShipRoute cargoRouteForSide = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getShippingLaneManager().
                getNearbyCargoShipRouteBySide(campaign, squadron, side);
        
        if (cargoRouteForSide != null)
        {
            Coordinate routeStartPosition = getInRangeStartPosition(campaign, cargoRouteForSide, squadron);
            cargoRouteForSide.setRouteStartPosition(routeStartPosition);
        }
        
        return cargoRouteForSide;
    }
    
    private static Coordinate getInRangeStartPosition(Campaign campaign, CargoShipRoute cargoRoute, Squadron squadron) throws PWCGException
    {
        Coordinate routeStartPosition = getConvoyInitialStartPosition(campaign, cargoRoute);
        double distanceFromPlayer = MathUtils.calcDist(routeStartPosition, squadron.determineCurrentPosition(campaign.getDate()));
        while (distanceFromPlayer > TargetDistance.findMaxTargetDistanceForSquadron(campaign, squadron.getSquadronId()))
        {
            double angle = MathUtils.calcAngle(routeStartPosition, cargoRoute.getRouteDestination());
            routeStartPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), routeStartPosition.copy(), angle, 5000.0);
            
            double adjustedDistanceFromPlayer = MathUtils.calcDist(routeStartPosition, squadron.determineCurrentPosition(campaign.getDate()));
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

    private static Coordinate getConvoyInitialStartPosition(Campaign campaign, CargoShipRoute cargoRoute) throws PWCGException
    {
        double routeDistance = MathUtils.calcDist(cargoRoute.getRouteStartPosition(), cargoRoute.getRouteDestination());
        int startPosOnRoute = 0;
        int routeLengthForStart = Double.valueOf(routeDistance).intValue() - MINIMUM_DISTANCE_TO_DESTINATION;
        if (routeLengthForStart > 0)
        {
            startPosOnRoute = RandomNumberGenerator.getRandom(routeLengthForStart);
        }
        
        double angle = MathUtils.calcAngle(cargoRoute.getRouteStartPosition(), cargoRoute.getRouteDestination());
        Coordinate startPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), cargoRoute.getRouteStartPosition(), angle, startPosOnRoute);
        return startPosition;
    }
}
