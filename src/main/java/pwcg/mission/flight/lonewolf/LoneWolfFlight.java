package pwcg.mission.flight.lonewolf;

import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.target.TargetDefinition;

public class LoneWolfFlight extends PatrolFlight
{
    public LoneWolfFlight(IFlightInformation flightInformation, TargetDefinition targetDefinition)
    {
        super(flightInformation, targetDefinition);
    }
}
