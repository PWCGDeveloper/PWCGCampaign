package pwcg.mission.flight.lonewolf;

import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.target.TargetDefinition;

public class LoneWolfFlight extends PatrolFlight
{
    public LoneWolfFlight(FlightInformation flightInformation, TargetDefinition targetDefinition)
    {
        super(flightInformation, targetDefinition);
    }
}
