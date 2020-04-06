package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPayloadBuilder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.target.TargetDefinition;

public class VirtualEscortFlight extends Flight implements IFlight
{
    private IFlight escortedFlight = null;


    public VirtualEscortFlight(IFlightInformation flightInformation, TargetDefinition targetDefinition, IFlight escortedFlight)
    {
        super(flightInformation, targetDefinition);
        this.escortedFlight = escortedFlight;
    }

    public void createFlight() throws PWCGException
    {
        initialize(this);
        createWaypoints();
        FlightPositionSetter.setFlightInitialPosition(this);
        setFlightPayload();
    }
    
    private void createWaypoints() throws PWCGException
    {
        int altitudeOffset = 600;
        int horizontalOffset = 300;

        IMissionPointSet flightActivate = MissionPointSetFactory.createFlightActivate(this);
        this.getWaypointPackage().addMissionPointSet(flightActivate);

        IMissionPointSet duplicatedWaypointsWithOffset = MissionPointSetFactory.duplicateWaypointsWithOffset(escortedFlight, altitudeOffset, horizontalOffset);
        this.getWaypointPackage().addMissionPointSet(duplicatedWaypointsWithOffset);
    }

    private void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder flightPayloadHelper = new FlightPayloadBuilder(this);
        flightPayloadHelper.setFlightPayload();
    }
}	

