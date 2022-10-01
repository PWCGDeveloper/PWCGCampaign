package pwcg.testutils;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetType;

public class FlightTypeFinder
{
    static public boolean hasFlightWithTargetType (Mission mission, TargetType targetType) throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            System.out.println("Flight " + flight.getSquadron().determineDisplayName(mission.getCampaign().getDate()) + " has target " + flight.getTargetDefinition().getTargetType());
            if (flight.getTargetDefinition().getTargetType() == targetType)
            {
                return true;
            }
        }
        
        return false;
    }
    
    static public boolean hasFlightType (Mission mission, FlightTypes flightType) throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            System.out.println("Flight " + flight.getSquadron().determineDisplayName(mission.getCampaign().getDate()) + " has flight type " + flight.getFlightType());
            if (flight.getFlightType() == flightType)
            {
                return true;
            }
        }
        
        return false;
    }
}
