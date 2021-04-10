package pwcg.mission.flight.opposing;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.bomb.BombingWaypointFactory;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class ScrambleOpposingBombFlight extends Flight implements IFlight
{	
    public ScrambleOpposingBombFlight(FlightInformation flightInformation, TargetDefinition targetDefinition)
    {
        super(flightInformation, targetDefinition);
    }

    public void createFlight() throws PWCGException
    {
        initialize(this);
        createWaypoints();
        WaypointPriority.setWaypointsNonFighterPriority(this);
        createFlightCommonPostBuild();
    }

    private void createWaypoints() throws PWCGException
    {
        McuWaypoint ingressWaypoint =ScrambleOpposingEntryWaypointFactory.createScrambleEntryWaypoints(this);

        ScrambleOpposingBombWaypointFactory missionWaypointFactory = new ScrambleOpposingBombWaypointFactory(this, BombingWaypointFactory.BOMB_ATTACK_TIME);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints(ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
        
        IMissionPointSet flightEnd = MissionPointSetFactory.createFlightEndAtHomeField(this);
        this.getWaypointPackage().addMissionPointSet(flightEnd);        
    }
}
