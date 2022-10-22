package pwcg.mission.flight.bomb;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class BombingFlight extends Flight
{          
    protected IngressWaypointPattern ingressWaypointPosition;
    protected double distanceToIngress = 0;

    public BombingFlight(FlightInformation flightInformation, TargetDefinition targetDefinition)
    {
        super(flightInformation, targetDefinition);
        ingressWaypointPosition = IngressWaypointPattern.INGRESS_NEAR_FRONT;
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
        McuWaypoint ingressWaypoint;
        if (ingressWaypointPosition == IngressWaypointPattern.INGRESS_AT_TARGET)
        {
            ingressWaypoint = IngressWaypointFactory.createIngressWaypointAtTarget(this, distanceToIngress);
        }
        else
        {
            ingressWaypoint = IngressWaypointFactory.createIngressWaypoint(ingressWaypointPosition, this);
        }
        
        IMissionPointSet flightActivate = MissionPointSetFactory.createFlightActivate(this);
        this.getWaypointPackage().addMissionPointSet(flightActivate);

        IMissionPointSet flightBegin = MissionPointSetFactory.createFlightBegin(this, flightActivate, AirStartPattern.AIR_START_NEAR_WAYPOINT, ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(flightBegin);

        IMissionPointSet flightRendezvous = MissionPointSetFactory.createFlightRendezvous(this, ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(flightRendezvous);

        BombingWaypointFactory missionWaypointFactory = new BombingWaypointFactory(this);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints(ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
        
        IMissionPointSet flightEnd = MissionPointSetFactory.createFlightEndAtHomeField(this);
        this.getWaypointPackage().addMissionPointSet(flightEnd);        
    }
}
