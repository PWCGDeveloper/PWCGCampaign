package pwcg.mission.flight.escort;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class EscortForPlayerFlight extends Flight
{
    private McuCover cover = null;
    private McuTimer coverTimer  = null;

    private McuTimer forceCompleteTimer = null;
    private McuForceComplete forceCompleteEntity = null;

    private Coordinate rendevousCoord;
    
    private Flight playerFlight;


    public EscortForPlayerFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, Flight playerFlight) 
    {
        super(flightInformation, missionBeginUnit);
        this.playerFlight = playerFlight;                
    }

    @Override
    public void createUnitMission() throws PWCGException  
    {
        createWaypointPackage();
        createEscortPlaneInitialPosition();
        super.createWaypoints();
        super.createActivation();
        createFormation();
        setFlightPayload();
        createCover();
        createForceComplete();
        linkToPlayerFlightWaypoints();
    }

    @Override
    protected List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException
    {
        EscortForPlayerWaypoints escortForPlayerWaypoints = new EscortForPlayerWaypoints(this, playerFlight);
        return escortForPlayerWaypoints.createWaypoints(mission, startPosition);
    }

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        linkWPToPlane(getLeadPlane(), waypointPackage.getWaypointsForLeadPlane());

        getMissionBeginUnit().linkToMissionBegin(getActivationTimer().getIndex());
        getActivationTimer().setTarget(getFormationTimer().getIndex());
        
        McuWaypoint airStartWP = this.getWaypointPackage().getWaypointByType(WaypointType.AIR_START_WAYPOINT);
        McuWaypoint ingressWP = this.getWaypointPackage().getWaypointByType(WaypointType.INGRESS_WAYPOINT);
        
        airStartWP.setTarget(ingressWP.getIndex());
        ingressWP.setTarget(getCoverTimer().getIndex());

        McuWaypoint rtbWP = this.getWaypointPackage().getWaypointByType(WaypointType.RETURN_TO_BASE_WAYPOINT);
        forceCompleteTimer.setTarget(forceCompleteEntity.getIndex());
        forceCompleteTimer.setTarget(rtbWP.getIndex());
    }

    public void createCover() throws PWCGException 
    {
        Coordinate coverPosition = getCoverPosition();
        
        cover  = new McuCover();
        cover.setPosition(coverPosition);
        cover.setObject(planes.get(0).getEntity().getIndex());
        cover.setTarget(playerFlight.getPlanes().get(0).getEntity().getIndex());

        coverTimer  = new McuTimer();
        coverTimer.setName("Cover Timer for " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        coverTimer.setDesc("Cover " + playerFlight.getFlightInformation().getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        coverTimer.setPosition(coverPosition);
        coverTimer.setTarget(cover.getIndex());        
    }

    private void createForceComplete()
    {
        // Deactivate the cover entity
        forceCompleteEntity = new McuForceComplete();
        forceCompleteEntity.setName("Escort Cover Force Complete");
        forceCompleteEntity.setDesc("Escort Cover Force Complete");
        forceCompleteEntity.setOrientation(new Orientation());
        forceCompleteEntity.setPosition(flightInformation.getTargetPosition().copy());
        forceCompleteEntity.setObject(planes.get(0).getEntity().getIndex());
        
        forceCompleteTimer  = new McuTimer();
        forceCompleteTimer.setName("Escort Cover Force Complete Timer");
        forceCompleteTimer.setDesc("Escort Cover Force Complete Timer");
        forceCompleteTimer.setOrientation(new Orientation());
        forceCompleteTimer.setPosition(flightInformation.getTargetPosition().copy());
        forceCompleteTimer.setTimer(2);
    }

    private void createEscortPlaneInitialPosition() throws PWCGException 
    {
        PlaneMCU flightLeader = getFlightLeader();

        Coordinate coverPosition = getCoverPosition();
        Orientation orient = getCoverOrientation();

        int i = 0;
        for (PlaneMCU plane : planes)
        {
            Coordinate planeCoords = coverPosition.copy();
            
            IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int AircraftSpacingHorizontal = productSpecificConfiguration.getAircraftSpacingHorizontal();
            planeCoords.setXPos(coverPosition.getXPos() - (i * AircraftSpacingHorizontal));
            planeCoords.setZPos(coverPosition.getZPos() - (i * AircraftSpacingHorizontal));

            int AircraftSpacingVertical = productSpecificConfiguration.getAircraftSpacingVertical();            
            planeCoords.setYPos(coverPosition.getYPos() + (i * AircraftSpacingVertical));
            plane.setPosition(planeCoords);

            plane.setOrientation(orient);

            // This must be done last
            plane.populateEntity(this, flightLeader);
            ++i;
        }
    }

    private Coordinate getCoverPosition()
    {
        McuWaypoint ingressWP = WaypointGeneratorUtils.findWaypointByType(playerFlight.getAllFlightWaypoints(), 
                WaypointType.INGRESS_WAYPOINT.getName());

        Coordinate coverPosition = ingressWP.getPosition().copy();
        coverPosition.setYPos(coverPosition.getYPos() + 400);
        return coverPosition;
    }

    private Orientation getCoverOrientation() throws PWCGException
    {
        McuWaypoint ingressWP = WaypointGeneratorUtils.findWaypointByType(playerFlight.getAllFlightWaypoints(), 
                WaypointType.INGRESS_WAYPOINT.getName());

        Orientation orient = new Orientation();
        orient.setyOri(ingressWP.getOrientation().getyOri());
        return orient;
    }

    private void linkToPlayerFlightWaypoints() throws PWCGException
    {
        McuWaypoint playerIngressWP = playerFlight.getWaypointPackage().getWaypointByType(WaypointType.INGRESS_WAYPOINT);
        playerIngressWP.setTarget(coverTimer.getIndex());
        
        McuWaypoint playerEgressWP = playerFlight.getWaypointPackage().getWaypointByType(WaypointType.EGRESS_WAYPOINT);
        playerEgressWP.setTarget(forceCompleteTimer.getIndex());
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);
        
        coverTimer.write(writer);
        cover.write(writer);
        
        forceCompleteTimer.write(writer);
        forceCompleteEntity.write(writer);
    }


    public McuCover getCover()
    {
        return cover;
    }

    public McuTimer getCoverTimer()
    {
        return coverTimer;
    }

    public McuTimer getForceCompleteTimer()
    {
        return forceCompleteTimer;
    }

    public McuForceComplete getForceCompleteEntity()
    {
        return forceCompleteEntity;
    }

    public Coordinate getRendevousCoord()
    {
        return rendevousCoord;
    }

    public Flight getPlayerFlight()
    {
        return playerFlight;
    }
}
