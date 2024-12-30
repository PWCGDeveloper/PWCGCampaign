package pwcg.mission.flight.recon;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class PlayerReconFlight extends ReconFlight
{
    private ReconFlightTypes reconFlightType = ReconFlightTypes.RECON_FLIGHT_FRONT;

    public PlayerReconFlight(FlightInformation flightInformation, TargetDefinition targetDefinition)
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
        McuWaypoint ingressWaypoint = IngressWaypointFactory.createIngressWaypoint(IngressWaypointPattern.INGRESS_NEAR_FRONT, this);

        IMissionPointSet flightActivate = MissionPointSetFactory.createFlightActivate(this);
        this.getWaypointPackage().addMissionPointSet(flightActivate);

        IMissionPointSet flightBegin = MissionPointSetFactory.createFlightBegin(this, flightActivate, AirStartPattern.AIR_START_NEAR_WAYPOINT, ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(flightBegin);

        IMissionPointSet missionWaypoints = createPlayerReconMissionPointSet(ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
        
        IMissionPointSet flightEnd = MissionPointSetFactory.createFlightEndAtHomeField(this);
        this.getWaypointPackage().addMissionPointSet(flightEnd);        
    }

    public IMissionPointSet createPlayerReconMissionPointSet(McuWaypoint ingressWaypoint) throws PWCGException
    {
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(this, ingressWaypoint.getPosition());

        if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_TRANSPORT)
        {
            ReconTransportWaypointsFactory waypoints = new ReconTransportWaypointsFactory(this);
            return waypoints.createPlayerWaypoints(ingressWaypoint, egressWaypoint);
        }
        else if (reconFlightType == ReconFlightTypes.RECON_FLIGHT_AIRFIELD)
        {
        	ReconAirfieldWaypointsFactory waypoints = new ReconAirfieldWaypointsFactory(this);
            return waypoints.createPlayerWaypoints(ingressWaypoint, egressWaypoint);
        }
        else
        {
            ReconFrontWaypointsFactory waypoints = new ReconFrontWaypointsFactory(this);
            return waypoints.createPlayerWaypoints(ingressWaypoint, egressWaypoint);
        }       
    }

    public ReconFlightTypes getReconFlightType()
    {
        return reconFlightType;
    }
	
}
