package pwcg.mission.flight.scramble;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.patterns.InterceptAtTargetPattern;
import pwcg.mission.target.TargetDefinition;

public class PlayerScrambleFlight extends Flight implements IFlight
{
    public PlayerScrambleFlight(FlightInformation flightInformation, TargetDefinition targetDefinition)
    {
        super(flightInformation, targetDefinition);
    }

    public void createFlight() throws PWCGException
    {
        initialize(this);
        setFlightPayload();
        createWaypoints();
        createFlightCommonPostBuild();
    }
    
    private void createWaypoints() throws PWCGException
    {
        List<IMissionPointSet> interceptMissionSets = InterceptAtTargetPattern.generateInterceptSegments(this);
        for (IMissionPointSet interceptMissionSet : interceptMissionSets)
        {
            this.getWaypointPackage().addMissionPointSet(interceptMissionSet);
        }
    }
}
