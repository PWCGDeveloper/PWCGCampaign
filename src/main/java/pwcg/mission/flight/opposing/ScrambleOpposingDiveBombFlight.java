package pwcg.mission.flight.opposing;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.begin.IngressBackoffUsingConfigsCalculator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class ScrambleOpposingDiveBombFlight extends Flight implements IFlight
{	
    public ScrambleOpposingDiveBombFlight(FlightInformation flightInformation, TargetDefinition targetDefinition)
    {
        super(flightInformation, targetDefinition);
    }

    public void createFlight() throws PWCGException
    {
        initialize(this);
        setFlightPayload();
        createWaypoints();
        WaypointPriority.setWaypointsNonFighterPriority(this);
        createFlightCommonPostBuild();
    }

    private void createWaypoints() throws PWCGException
    {
        double ingressDistanceFromTarget = IngressBackoffUsingConfigsCalculator.calculateIngressDistanceFromTarget(this.getFlightInformation(), this.getTargetDefinition());
        McuWaypoint ingressWaypoint = ScrambleOpposingEntryWaypointFactory.createScrambleEntryWaypoints(this, ingressDistanceFromTarget);

        ScrambleOpposingDiveBombWaypointFactory missionWaypointFactory = new ScrambleOpposingDiveBombWaypointFactory(this);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints(ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
        
        IMissionPointSet flightEnd = MissionPointSetFactory.createFlightEndAtHomeField(this);
        this.getWaypointPackage().addMissionPointSet(flightEnd);        
    }
}
