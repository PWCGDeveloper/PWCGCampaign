package pwcg.mission.flight.lonewolf;

import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.patrol.PatrolFlight;

public class LoneWolfFlight extends PatrolFlight
{
    public LoneWolfFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }
}
