package pwcg.mission.flight.waypoint.patterns;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class PathAlongFrontDataBuilder
{
    private IFlight flight;
    
    public PathAlongFrontDataBuilder (IFlight flight)
    {
        this.flight = flight;
    }
    
    public PathAlongFrontData buildPathAlongFrontData(Coordinate generalStartPosition, int depthOfPenetration, int patrolDistance) throws PWCGException
    {
        Campaign campaign = flight.getCampaign();
        
        patrolDistance = adjustPatrolDistanceForAircraftRange(patrolDistance);        
        patrolDistance = adjustPatrolDistanceForReturnLeg(patrolDistance);

        PathAlongFrontData pathAlongFrontData = new PathAlongFrontData();
        pathAlongFrontData.setMission(flight.getMission());
        pathAlongFrontData.setDate(campaign.getDate());
        pathAlongFrontData.setOffsetTowardsEnemy(depthOfPenetration);
        pathAlongFrontData.setPathDistance(patrolDistance / 2);
        pathAlongFrontData.setTargetGeneralLocation(generalStartPosition);
        pathAlongFrontData.setReturnAlongRoute(determineHasReturnLeg());
        pathAlongFrontData.setSide(flight.getFlightInformation().getCountry().getSide());
        
        return pathAlongFrontData;
    }

    private boolean determineHasReturnLeg()
    {
        if (flight.getFlightType() == FlightTypes.LOW_ALT_PATROL)
        {
            return true;
        }
        
        return false;
    }

    private int adjustPatrolDistanceForAircraftRange(int patrolDistance)
    {
        return patrolDistance;
    }
    
    private int adjustPatrolDistanceForReturnLeg(int patrolDistance)
    {
        if (determineHasReturnLeg())
        {
            Double patrolDistanceDouble = Integer.valueOf(patrolDistance).doubleValue();
            patrolDistanceDouble = patrolDistanceDouble * 0.67;
            return patrolDistanceDouble.intValue();
        }
        return patrolDistance;
    }
}
