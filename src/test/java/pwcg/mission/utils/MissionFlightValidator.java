package pwcg.mission.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class MissionFlightValidator
{
    
    public static void validateMission(Mission mission) throws PWCGException
    {
        validateSquadronsUsedOnlyOnce(mission);
        validateAirfieldUsedOnlyOnce(mission);
    }

    private static void validateSquadronsUsedOnlyOnce(Mission mission) throws PWCGException
    {
        Set<Integer> squadronsUsedInMission = new HashSet<>();
        for (IFlight flight: mission.getFlights().getAllAerialFlights())
        {
            System.out.println(flight.getFlightInformation().getSquadron().determineDisplayName(mission.getCampaign().getDate()) + "    " + flight.getFlightInformation().getFlightType());
            Assertions.assertTrue (!squadronsUsedInMission.contains(flight.getSquadron().getCompanyId()));
            squadronsUsedInMission.add(flight.getSquadron().getCompanyId());
        }
    }

    private static void validateAirfieldUsedOnlyOnce(Mission mission) throws PWCGException
    {
        Map<String, Airfield> includedAirfields = new HashMap<>();
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            if (flight.getFlightInformation().isAirStart())
            {
                continue;
            }
            
            Airfield airfield = flight.getSquadron().determineCurrentAirfieldAnyMap(mission.getCampaign().getDate());
            System.out.println(flight.getFlightInformation().getSquadron().determineDisplayName(mission.getCampaign().getDate()) + "    " + airfield.getName() + "    " + flight.getFlightInformation().getFlightType());
            assert(!includedAirfields.containsKey(airfield.getName()));
            includedAirfields.put(airfield.getName(), airfield);
        }
    }
}
