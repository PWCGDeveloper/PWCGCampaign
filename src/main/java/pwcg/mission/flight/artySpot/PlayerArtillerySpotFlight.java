package pwcg.mission.flight.artySpot;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.artySpot.grid.ArtillerySpotGrid;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class PlayerArtillerySpotFlight extends Flight implements IFlight
{
	private ArtillerySpotGrid artySpotGrid = null;
	private ArtillerySpotArtilleryGroup friendlyArtillery;
	
    public PlayerArtillerySpotFlight(FlightInformation flightInformation, TargetDefinition targetDefinition)
    {
        super(flightInformation, targetDefinition);
    }

    public void createFlight() throws PWCGException
    {
    	friendlyArtillery = new ArtillerySpotArtilleryGroup(this.getFlightInformation(), this);
    	friendlyArtillery.build();
    	
		artySpotGrid = new ArtillerySpotGrid(this.getFlightInformation());
		artySpotGrid.create(friendlyArtillery, this.getTargetDefinition().getPosition().copy());

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

        IMissionPointSet flightRendezvous = MissionPointSetFactory.createFlightRendezvous(this, ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(flightRendezvous);

        ArtillerySpotWaypointFactory missionWaypointFactory = new ArtillerySpotWaypointFactory(this);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints(ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
        
        IMissionPointSet flightEnd = MissionPointSetFactory.createFlightEndAtHomeField(this);
        this.getWaypointPackage().addMissionPointSet(flightEnd);        
    }
    

    public void write(BufferedWriter writer) throws PWCGException 
    {
    	friendlyArtillery.write(writer);
    	artySpotGrid.write(writer);
    }
}
