package pwcg.mission.flight.escort;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPositionHelperPlayerStart;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

/**
 * Three escort flights: player is the escort, escort for the player, virtual escort.
 * This is the player escorting another flight
 * 
 * @author Patrick Wilson
 *
 */
public class PlayerEscortFlight extends Flight
{
	protected McuCover cover = null;
	protected McuTimer coverTimer  = null;

	protected McuTimer forceCompleteTimer = null;
	protected McuForceComplete forceCompleteEntity = null;

    protected McuTimer escortedFlightWaypointTimer = null;
    protected McuTimer egressTimer = null;

	protected Flight escortedFlight = null;

	public void setEscortingFlight(Flight escortingFlight) {
		this.escortedFlight = escortingFlight;
	}

    public PlayerEscortFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, Flight escortedFlight)
    {
        super (flightInformation, missionBeginUnit);
        this.escortedFlight = escortedFlight;
    }

    @Override
    public void createUnitMission() throws PWCGException 
    {
        super.createUnitMission();
        createUnitMissionEscortSpecific();
    }
    

	public void createUnitMissionEscortSpecific() throws PWCGException  
	{
		createTimers();
		createCover();
		createForceComplete();
		createActivation();

        FlightPositionHelperPlayerStart flightPositionHelperPlayerStart = new FlightPositionHelperPlayerStart(getCampaign(), this);
        flightPositionHelperPlayerStart.createPlayerPlanePosition();
	}

	@Override
    protected void createFlightSpecificTargetAssociations()
    {        
        linkWPToPlane(getLeadPlane(), waypointPackage.getWaypointsForLeadPlane());        
        createRendezvousTargetAssociations();
        createSeparationTargetAssociations();
    }
	
	public void createRendezvousTargetAssociations()
	{
        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : waypointPackage.getWaypointsForLeadPlane())
        {
            // Player Start -> Player ingress
            // Player Ingress -> Rendezvous
            // Rendezvous -> Cover Timer -> Cover
            // Cover Timer -> Ingress Timer -> Bomber Ingress
        	
            if (nextWP.getName().equals(WaypointType.RENDEZVOUS_WAYPOINT.getName()))
            {
                linkRendezvousWPToCover(nextWP);
                prevWP.setTarget(nextWP.getIndex());
                break;
            }
            
            if (prevWP != null)
            {  
                prevWP.setTarget(nextWP.getIndex());
            }
            
            prevWP = nextWP;
        }
	}

	private void linkRendezvousWPToCover(McuWaypoint rendezvousWP)
	{
		McuWaypoint escortedIngressWP = getEscortedFlight().getAllWaypoints().get(0);

		rendezvousWP.setTarget(coverTimer.getIndex());
		coverTimer.setTarget(escortedFlightWaypointTimer.getIndex());
		escortedFlightWaypointTimer.setTarget(escortedIngressWP.getIndex());
	}
	
	public void createSeparationTargetAssociations()
	{
		boolean separationStarted = false;
		
        // Cover Flight Egress -> Cover Deactivate
        // Cover Deactivate -> cover
        // Cover Deactivate -> Player Egress
        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : waypointPackage.getWaypointsForLeadPlane())
        {
	        if (nextWP.getName().equals(WaypointType.EGRESS_WAYPOINT.getName()))
	        {
	        	linkSeparationToEgressWP(nextWP);
	        	separationStarted = true;
	        }
	        else if (separationStarted)
	        {
	            prevWP.setTarget(nextWP.getIndex());
	        }

	        prevWP = nextWP;
        }
	}

	private void linkSeparationToEgressWP(McuWaypoint nextWP)
	{
		List<McuWaypoint> escortedWaypoints = getEscortedFlight().getAllWaypoints();
		McuWaypoint escortedEgress = WaypointGeneratorBase.findWaypointByType(escortedWaypoints, WaypointType.EGRESS_WAYPOINT.getName());
		escortedEgress.setTarget(forceCompleteTimer.getIndex());

		forceCompleteTimer.setTarget(egressTimer.getIndex());
		egressTimer.setTarget(nextWP.getIndex());
	}


	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		if (escortedFlight == null)
		{
			throw new PWCGMissionGenerationException ("No linked flight for a escort");
		}
		
		Coordinate rendevousPoint = escortedFlight.getPlanes().get(0).getPosition();
		
		PlayerEscortWaypoints waypointGenerator = new PlayerEscortWaypoints(startPosition.copy(), 
		                                                         rendevousPoint, 
				   												 this,
				   												 mission);
		
        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
          
        return waypointList;
	}

	public void createCover() throws PWCGException 
	{
		Coordinate referencePoint = escortedFlight.getAllWaypoints().get(0).getPosition();

		// Cover the escorted flight
		cover  = new McuCover();
		cover.setPosition(referencePoint);
		cover.setObject(planes.get(0).getEntity().getIndex());
		cover.setTarget(escortedFlight.getPlanes().get(0).getEntity().getIndex());

		// The cover timer.
		// Activate the cover command
		// Activate the escorted squadron
		coverTimer  = new McuTimer();
		coverTimer.setName("Cover Timer for " + getSquadron().determineDisplayName(getCampaign().getDate()));
		coverTimer.setDesc("Cover " + escortedFlight.getSquadron().determineDisplayName(getCampaign().getDate()));
		coverTimer.setPosition(referencePoint);
		coverTimer.setTarget(cover.getIndex());
	}

	protected void createForceComplete() throws PWCGException
	{
		// Deactivate the cover entity
		forceCompleteEntity = new McuForceComplete();
		forceCompleteEntity.setName("Escort Cover Force Complete");
		forceCompleteEntity.setDesc("Escort Cover Force Complete");
		forceCompleteEntity.setOrientation(new Orientation());
		forceCompleteEntity.setPosition(getTargetCoords().copy());
		forceCompleteEntity.setObject(planes.get(0).getEntity().getIndex());
		
		forceCompleteTimer  = new McuTimer();
		forceCompleteTimer.setName("Escort Cover Force Complete Timer");
		forceCompleteTimer.setDesc("Escort Cover Force Complete Timer");
		forceCompleteTimer.setOrientation(new Orientation());
		forceCompleteTimer.setPosition(getTargetCoords().copy());
		forceCompleteTimer.setTimer(2);
		forceCompleteTimer.setTarget(forceCompleteEntity.getIndex());
	}

    protected void createTimers() throws PWCGException 
    {
        escortedFlightWaypointTimer = new McuTimer();
        escortedFlightWaypointTimer.setName("Escort Cover Ingress Timer");
        escortedFlightWaypointTimer.setDesc("Escort Cover Ingress Timer");
        escortedFlightWaypointTimer.setOrientation(new Orientation());
        escortedFlightWaypointTimer.setPosition(getTargetCoords().copy());              
        escortedFlightWaypointTimer.setTimer(2);                
        
        egressTimer = new McuTimer();
        egressTimer.setName("Escort Cover Egress Timer");
        egressTimer.setDesc("Escort Cover Egress Timer");
        egressTimer.setOrientation(new Orientation());
        egressTimer.setPosition(getTargetCoords().copy());              
        egressTimer.setTimer(2);                
    }

	@Override
	protected void createActivation() throws PWCGException 
	{
		// Do this again to compensate for the movement of the escorted flight
	    FlightPositionHelperPlayerStart flightPositionHelperPlayerStart = new FlightPositionHelperPlayerStart(getCampaign(), this);
	    flightPositionHelperPlayerStart.createPlayerPlanePosition();
		super.createActivation();
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		super.write(writer);
		
		coverTimer.write(writer);
		cover.write(writer);
		escortedFlightWaypointTimer.write(writer);
		
		forceCompleteTimer.write(writer);
		forceCompleteEntity.write(writer);
		egressTimer.write(writer);
	}

	public String getMissionObjective() throws PWCGException 
	{
		String objective = "Escort our flight to the specified location and accompany them until they cross our lines.";
        String objectiveName =  formMissionObjectiveLocation(getTargetCoords().copy());
        if (!objectiveName.isEmpty())
		{
			objective = "Escort our flight to the location" + objectiveName + 
					".  Accompany them until they cross our lines.";
		}
		
		return objective;
	}

	public Flight getEscortedFlight() 
	{
		return escortedFlight;
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

	public McuTimer getEscortedFlightWaypointTimer() 
	{
		return escortedFlightWaypointTimer;
	}

	public McuTimer getEgressTimer() 
	{
		return egressTimer;
	}
	
	
}	

