package pwcg.mission.flight.scramble;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPayloadBuilder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.flight.waypoint.begin.FlightWaypointGroupFactory;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;

public class ScrambleOpposingDiveBombFlight extends Flight implements IFlight
{	
    public ScrambleOpposingDiveBombFlight(IFlightInformation flightInformation)
    {
        super(flightInformation);
    }

    public void createFlight() throws PWCGException
    {
        flightData.initialize(this);
        createWaypoints();
        FlightPositionSetter.setFlightInitialPosition(this);
        setFlightPayload();
    }

    private void createWaypoints() throws PWCGException
    {
        ScrambleOpposingDiveBombWaypointFactory missionWaypointFactory = new ScrambleOpposingDiveBombWaypointFactory(this);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints();
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_MISSION_PATROL, missionWaypoints);
        
        IMissionPointSet flightEnd = FlightWaypointGroupFactory.createFlightEndAtHomeField(this);
        flightData.getWaypointPackage().addMissionPointSet(MissionPointSetType.MISSION_POINT_SET_FLIGHT_END, flightEnd);
    }

    private void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder flightPayloadHelper = new FlightPayloadBuilder(this);
        flightPayloadHelper.setFlightPayload();
    }

    @Override
    public void finalize() throws PWCGException
    {
        flightData.getWaypointPackage().finalize();
    }
}
