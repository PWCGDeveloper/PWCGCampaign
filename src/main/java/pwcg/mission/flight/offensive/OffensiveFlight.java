package pwcg.mission.flight.offensive;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPayloadBuilder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class OffensiveFlight extends Flight implements IFlight
{
    private OffensiveFlightTypes offensiveFlightType =  OffensiveFlightTypes.OFFENSIVE_FLIGHT_FRONT;
    public static enum OffensiveFlightTypes
    {
        OFFENSIVE_FLIGHT_FRONT,
        OFFENSIVE_FLIGHT_TRANSPORT,
        OFFENSIVE_FLIGHT_AIRFIELD,
    }

    public OffensiveFlight(FlightInformation flightInformation, TargetDefinition targetDefinition)
    {
        super(flightInformation, targetDefinition);
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
        McuWaypoint ingressWaypoint = IngressWaypointFactory.createIngressWaypoint(IngressWaypointPattern.INGRESS_NEAR_FRONT, this);

        IMissionPointSet flightActivate = MissionPointSetFactory.createFlightActivate(this);
        this.getWaypointPackage().addMissionPointSet(flightActivate);

        IMissionPointSet flightBegin = MissionPointSetFactory.createFlightBegin(this, flightActivate, AirStartPattern.AIR_START_NEAR_WAYPOINT, ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(flightBegin);

        IMissionPointSet missionWaypoints = createOffensivePatrolMissionPointSet(ingressWaypoint);
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
        
        IMissionPointSet flightEnd = MissionPointSetFactory.createFlightEndAtHomeField(this);
        this.getWaypointPackage().addMissionPointSet(flightEnd);        
    }

    public IMissionPointSet createOffensivePatrolMissionPointSet(McuWaypoint ingressWaypoint) throws PWCGException
    {
        createOffensiveFlightType();
        if (offensiveFlightType == OffensiveFlightTypes.OFFENSIVE_FLIGHT_TRANSPORT)
        {
            OffensiveWaypointsTransportFactory missionWaypointFactory = new OffensiveWaypointsTransportFactory(this);
            return  missionWaypointFactory.createWaypoints(ingressWaypoint);
        }
        else if (offensiveFlightType == OffensiveFlightTypes.OFFENSIVE_FLIGHT_AIRFIELD)
        {
            OffensiveWaypointsAirfieldFactory missionWaypointFactory = new OffensiveWaypointsAirfieldFactory(this);
            return  missionWaypointFactory.createWaypoints(ingressWaypoint);
        }
        else
        {
            OffensiveWaypointsFrontFactory missionWaypointFactory = new OffensiveWaypointsFrontFactory(this);
            return  missionWaypointFactory.createWaypoints(ingressWaypoint);
        }
    }

    private void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder flightPayloadHelper = new FlightPayloadBuilder(this);
        flightPayloadHelper.setFlightPayload();
    }

    private void createOffensiveFlightType()
    {
        int roll = RandomNumberGenerator.getRandom(100);

        if (roll < 45)
        {
            offensiveFlightType = OffensiveFlightTypes.OFFENSIVE_FLIGHT_TRANSPORT;
        }
        else if (roll < 55)
        {
            offensiveFlightType = OffensiveFlightTypes.OFFENSIVE_FLIGHT_AIRFIELD;
        }
        else
        {
            offensiveFlightType = OffensiveFlightTypes.OFFENSIVE_FLIGHT_FRONT;
        }
    }

    public OffensiveFlightTypes getOffensiveFlightType()
    {
        return offensiveFlightType;
    }
}
