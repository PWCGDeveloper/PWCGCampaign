package pwcg.mission.flight.escort;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

/**
 * Three escort flights: player is the escort, escort for the player, virtual escort.
 * This version of escort flight covers the player's flight
 * 
 * @author Patrick Wilson
 *
 */
public class EscortForPlayerFlight extends Flight
{
    protected McuCover cover = null;
    protected McuTimer coverTimer  = null;

    protected McuTimer deactivateCoverTimer = null;
    protected McuDeactivate deactivateCoverEntity = null;

    protected Coordinate rendevousCoord;
    
    protected Flight playerFlight;


    public EscortForPlayerFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, Flight playerFlight) 
    {
        super(flightInformation, missionBeginUnit);
        this.playerFlight = playerFlight;                
    }

    @Override
    public void createUnitMission() throws PWCGException  
    {
        calcPlanesInFlight();
        createWaypointPackage();
        createPlanes();
        createPlaneInitialPosition();
        createWaypoints();
        createActivation();
        createFormation();
        setFlightPayload();
        createCover();
        createDeactivate();
    }

    @Override
    protected void createWaypoints() throws PWCGException
    {
        List<McuWaypoint> waypointList = createWaypoints(flightInformation.getMission(), playerFlight.getFlightInformation().getDepartureAirfield().getPosition());
        waypointPackage.setWaypoints(waypointList);
    }

    @Override
    protected void createFlightSpecificTargetAssociations()
    {
        linkWPToPlane(getLeadPlane(), waypointPackage.getWaypointsForLeadPlane());

        getMissionBeginUnit().linkToMissionBegin(getActivationTimer().getIndex());
        getActivationTimer().setTarget(getFormationTimer().getIndex());
        
        McuWaypoint ingressWP = WaypointGeneratorBase.findWaypointByType(getPlayerFlight().getAllWaypoints(), 
                        WaypointType.INGRESS_WAYPOINT.getName());

        ingressWP.setTarget(getCoverTimer().getIndex());

        McuWaypoint egressWP = WaypointGeneratorBase.findWaypointByType(getPlayerFlight().getAllWaypoints(), 
                        WaypointType.EGRESS_WAYPOINT.getName());

        egressWP.setTarget(getDeactivateCoverTimer().getIndex());

        getDeactivateCoverTimer().setTarget(getWaypointPackage().getWaypointsForLeadPlane().get(0).getIndex());
    }

    public void createCover() throws PWCGException 
    {
        McuWaypoint ingressWP = WaypointGeneratorBase.findWaypointByType(playerFlight.getAllWaypoints(), 
                        WaypointType.INGRESS_WAYPOINT.getName());

        Coordinate coverPosition = ingressWP.getPosition().copy();
        coverPosition.setYPos(coverPosition.getYPos() + 400);
        
        // Cover the escorted flight
        cover  = new McuCover();
        cover.setPosition(coverPosition);
        cover.setObject(planes.get(0).getEntity().getIndex());
        cover.setTarget(playerFlight.getPlanes().get(0).getEntity().getIndex());

        // Activate the cover command
        coverTimer  = new McuTimer();
        coverTimer.setName("Cover Timer for " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        coverTimer.setDesc("Cover " + playerFlight.getFlightInformation().getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        coverTimer.setPosition(coverPosition);
        coverTimer.setTarget(cover.getIndex());
        
    }

    protected void createDeactivate() 
    {
        // Deactivate the cover entity
        deactivateCoverEntity = new McuDeactivate();
        deactivateCoverEntity.setName("Escort Cover Deactivate Cover");
        deactivateCoverEntity.setDesc("Escort Cover Deactivate Cover");
        deactivateCoverEntity.setOrientation(new Orientation());
        deactivateCoverEntity.setPosition(flightInformation.getTargetCoords().copy());                
        deactivateCoverEntity.setTarget(cover.getIndex());
        
        deactivateCoverTimer  = new McuTimer();
        deactivateCoverTimer.setName("Escort Cover Deactivate Cover Timer");
        deactivateCoverTimer.setDesc("Escort Cover Deactivate Cover Timer");
        deactivateCoverTimer.setOrientation(new Orientation());
        deactivateCoverTimer.setPosition(flightInformation.getTargetCoords().copy());             
        deactivateCoverTimer.setTimer(2);               
        deactivateCoverTimer.setTarget(deactivateCoverEntity.getIndex());
    }

    protected void createPlaneInitialPosition() throws PWCGException 
    {
        PlaneMCU flightLeader = getFlightLeader();

        // Initial position is at the ingress WP
        McuWaypoint ingressWP = WaypointGeneratorBase.findWaypointByType(playerFlight.getAllWaypoints(), 
                        WaypointType.INGRESS_WAYPOINT.getName());

        Coordinate coverPosition = ingressWP.getPosition().copy();
        coverPosition.setYPos(coverPosition.getYPos() + 400);

        int i = 0;
        for (PlaneMCU plane : planes)
        {
            Coordinate planeCoords = coverPosition.copy();

            ConfigManagerCampaign configManager = flightInformation.getCampaign().getCampaignConfigManager();
            int AircraftSpacingHorizontal = configManager.getIntConfigParam(ConfigItemKeys.AircraftSpacingHorizontalKey);
            
            planeCoords.setXPos(coverPosition.getXPos() - (i * AircraftSpacingHorizontal));
            planeCoords.setZPos(coverPosition.getZPos() - (i * AircraftSpacingHorizontal));

            int AircraftSpacingVertical = configManager.getIntConfigParam(ConfigItemKeys.AircraftSpacingVerticalKey);
            
            planeCoords.setYPos(coverPosition.getYPos() + (i * AircraftSpacingVertical));
            plane.setPosition(planeCoords);

            Orientation orient = new Orientation();
            orient.setyOri(ingressWP.getOrientation().getyOri());
            plane.setOrientation(orient);

            // This must be done last
            plane.populateEntity(this, flightLeader);
            ++i;
        }
    }

    @Override
    public int calcNumPlanes() throws PWCGException 
    {
        ConfigManagerCampaign configManager = flightInformation.getCampaign().getCampaignConfigManager();
        
        int GroundAttackMinimum = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey);
        int GroundAttackAdditional = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey) + 1;
        numPlanesInFlight = GroundAttackMinimum + RandomNumberGenerator.getRandom(GroundAttackAdditional);
        
        return modifyNumPlanes(numPlanesInFlight);

    }

    public String getMissionObjective() throws PWCGException 
    {
        String objective = "Escort to the specified location and accompany them until they cross our lines.";
        String objectiveName =  formMissionObjectiveLocation(flightInformation.getTargetCoords().copy());
        if (!objectiveName.isEmpty())
        {
            objective = "Escort to the location" + objectiveName + 
                    ".  Accompany them until they cross our lines.";
        }
        
        return objective;
    }

    @Override
    protected List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException
    {
        Coordinate rtbCoords = flightInformation.getDepartureAirfield().getPosition().copy();
        rtbCoords.setYPos(4000.0);

        Orientation orient = new Orientation();
        orient.setyOri(flightInformation.getDepartureAirfield().getOrientation().getyOri());

        List<McuWaypoint> waypoints = new ArrayList<>();
        
		McuWaypoint rtbWP = WaypointFactory.createReturnToBaseWaypointType();
        rtbWP.setTriggerArea(McuWaypoint.START_AREA);
        rtbWP.setSpeed(250);
        rtbWP.setPosition(rtbCoords);
        rtbWP.setOrientation(orient);
        
        waypoints.add(rtbWP);
        
        return waypoints;
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);
        
        coverTimer.write(writer);
        cover.write(writer);
        
        deactivateCoverTimer.write(writer);
        deactivateCoverEntity.write(writer);
    }


    public McuCover getCover()
    {
        return cover;
    }


    public void setCover(McuCover cover)
    {
        this.cover = cover;
    }


    public McuTimer getCoverTimer()
    {
        return coverTimer;
    }


    public void setCoverTimer(McuTimer coverTimer)
    {
        this.coverTimer = coverTimer;
    }


    public McuTimer getDeactivateCoverTimer()
    {
        return deactivateCoverTimer;
    }


    public void setDeactivateCoverTimer(McuTimer deactivateCoverTimer)
    {
        this.deactivateCoverTimer = deactivateCoverTimer;
    }


    public McuDeactivate getDeactivateCoverEntity()
    {
        return deactivateCoverEntity;
    }


    public void setDeactivateCoverEntity(McuDeactivate deactivateCoverEntity)
    {
        this.deactivateCoverEntity = deactivateCoverEntity;
    }


    public Coordinate getRendevousCoord()
    {
        return rendevousCoord;
    }


    public void setRendevousCoord(Coordinate rendevousCoord)
    {
        this.rendevousCoord = rendevousCoord;
    }


    public Flight getPlayerFlight()
    {
        return playerFlight;
    }


    public void setPlayerFlight(Flight playerFlight)
    {
        this.playerFlight = playerFlight;
    }

    
}
