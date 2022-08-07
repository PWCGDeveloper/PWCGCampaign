package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.begin.ClimbWaypointBuilder;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuFactory;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuMessage;
import pwcg.mission.mcu.McuTakeoff;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightBeginTakeoff extends MissionPointSetSingleWaypointSet implements IMissionPointSet
{
    private IFlight flight;
    private IMissionPointSet flightActivate;
    private McuTakeoff takeoffMcu = null;
    private McuTimer formationTimer = null;
    private McuTimer attackTimer = null;
    private McuFormation formationEntity = null;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;

    public MissionPointFlightBeginTakeoff(IFlight flight, IMissionPointSet flightActivate)
    {
        this.flight = flight;
        this.flightActivate = flightActivate;
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_BEGIN_TAKEOFF;
    }
    
    public void createFlightBegin() throws PWCGException 
    {
        createTakeoff();  
        createFormation();
        createAttack();
        createTakeOffWaypoints();
        linkTakeOffToActivate();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        takeoffMcu.write(writer);
        formationTimer.write(writer);
        formationEntity.write(writer);
        attackTimer.write(writer);
        
        super.write(writer);
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        super.getLastWaypoint().setTarget(nextTargetIndex);
    }    

    @Override
    public List<MissionPoint> getFlightMissionPoints()
    {
        List<MissionPoint> missionPoints = new ArrayList<>();
        MissionPoint takeoffPoint = new MissionPoint(takeoffMcu.getPosition(), WaypointAction.WP_ACTION_TAKEOFF);
        missionPoints.add(takeoffPoint);
        
        List<MissionPoint> postTakeoffWaypoints =  super.getWaypointsAsMissionPoints();
        missionPoints.addAll(postTakeoffWaypoints);

        return missionPoints;
    }

    @Override
    public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException
    {
        super.finalizeMissionPointSet(flightPlanes);
        createTargetAssociations();
        createObjectAssociations(flightPlanes.getFlightLeader());        
    }

    @Override
    public void disableLinkToNextTarget()
    {
        linkToNextTarget = false;        
    }

    @Override
    public boolean isLinkToNextTarget()
    {
        return linkToNextTarget;
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return formationTimer.getIndex();
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        allFlightPoints.addAll(waypoints.getWaypoints());
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }

    private void createTakeoff() throws PWCGException
    {
        takeoffMcu = null;
        if (!flight.getFlightInformation().isAirStart())
        {
            takeoffMcu = new McuTakeoff();
            takeoffMcu.setPosition(flight.getFlightInformation().getDepartureAirfield().getTakeoffLocation(flight.getMission()).getPosition().copy());
            takeoffMcu.setOrientation(flight.getFlightInformation().getDepartureAirfield().getTakeoffLocation(flight.getMission()).getOrientation().copy());
        }
    }
    
    private void createFormation() throws PWCGException
    {
        FlightInformation flightInformation = flight.getFlightInformation();

        formationEntity = new McuFormation(flight.getFlightInformation().getFormationType(), McuFormation.FORMATION_DENSITY_LOOSE);
        formationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition());

        int takeoffDelay = getStartDelay();
        formationTimer = McuFactory.createTimer(flight, "Formation", takeoffDelay);
    }
    
    private void createAttack() throws PWCGException
    {
        attackTimer = McuFactory.createTimer(flight, "Attack", 1);
    }

    private void createTakeOffWaypoints() throws PWCGException
    {
        McuWaypoint takeoffWP = clearTheDeck();
        createClimbWaypoints(takeoffWP);
    }  


    private McuWaypoint clearTheDeck() throws PWCGException 
    {
        Coordinate initialClimbCoords = createTakeOffCoords();

        McuWaypoint takeoffWP = WaypointFactory.createTakeOffWaypointType();
        takeoffWP.setTriggerArea(McuWaypoint.INITIAL_CLIMB_AREA);
        takeoffWP.setDesc(flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()), WaypointType.TAKEOFF_WAYPOINT.getName());
        takeoffWP.setSpeed(flight.getFlightCruisingSpeed());
        takeoffWP.setPosition(initialClimbCoords);
        takeoffWP.setOrientation(flight.getFlightInformation().getAirfield().getTakeoffLocation(flight.getMission()).getOrientation().copy());

        super.addWaypoint(takeoffWP);
        return takeoffWP;
    }
    
    private Coordinate createTakeOffCoords() throws PWCGException, PWCGException
    {
        int takeoffWaypointDistance = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.TakeoffWaypointDistanceKey);
        int takeoffWaypointAltitude = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.TakeoffWaypointAltitudeKey);

        double takeoffOrientation = flight.getFlightInformation().getAirfield().getTakeoffLocation(flight.getMission()).getOrientation().getyOri();
        Coordinate initialClimbCoords = MathUtils.calcNextCoord(flight.getFlightInformation().getAirfield().getTakeoffLocation(flight.getMission()).getPosition().copy(), takeoffOrientation, takeoffWaypointDistance);
        initialClimbCoords.setYPos(takeoffWaypointAltitude);

        return initialClimbCoords;
    }

    private void createClimbWaypoints(McuWaypoint takeoffWP) throws PWCGException
    {
        ClimbWaypointBuilder initialWaypointGenerator = new ClimbWaypointBuilder(flight);
        List<McuWaypoint> climbWaypoints = initialWaypointGenerator.createClimbWaypointsForPlayerFlight(takeoffWP);
        super.addWaypoints(climbWaypoints);
    }  

    private void linkTakeOffToActivate() throws PWCGException
    {
        flightActivate.setLinkToNextTarget(takeoffMcu.getIndex());        
    }

    private void createTargetAssociations() throws PWCGException
    {
        formationTimer.setTimerTarget(formationEntity.getIndex());
        triggerAttackMcuForPlanes();

        flight.getFlightPlanes().getFlightLeader().setOnMessages(
                McuMessage.ONTAKEOFF,
                takeoffMcu.getIndex(),
                super.getFirstWaypoint().getIndex());
    }

    private void triggerAttackMcuForPlanes() throws PWCGException
    {
        waypoints.getFirstWaypoint().setTarget(attackTimer.getIndex());
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            attackTimer.setTimerTarget(plane.getFighterAttackTarget());
        }
    }

    private void createObjectAssociations(PlaneMcu plane)
    {
        int flightLeaderIndex = plane.getLinkTrId();
        takeoffMcu.setObject(flightLeaderIndex);
        formationEntity.setObject(flightLeaderIndex);
    }

    private int getStartDelay() throws PWCGException
    {
        int startDelay = 1;
        if (flight.getFlightInformation().isAirStart() == false) 
        {
            ConfigManagerCampaign configManager = flight.getCampaign().getCampaignConfigManager();                        
            startDelay = configManager.getIntConfigParam(ConfigItemKeys.TakeoffTimeKey);
            if (startDelay > 30)
            {
                startDelay = 30;
            }
        }
        return startDelay;
    }
}
