package pwcg.mission.flight.escort;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPositionHelperPlayerStart;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuDeactivate;
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

	protected McuTimer deactivateCoverTimer = null;
	protected McuDeactivate deactivateCoverEntity = null;

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
		createDeactivate();
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
		List<McuWaypoint> escortedWaypoints = getEscortedFlight().getAllWaypoints();
		McuWaypoint escortedIngressWP = WaypointGeneratorBase.findWaypointByType(escortedWaypoints,  WaypointType.INGRESS_WAYPOINT.getName());

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
		escortedEgress.setTarget(deactivateCoverTimer.getIndex());

		deactivateCoverTimer.setTarget(egressTimer.getIndex());
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

	protected void createDeactivate() throws PWCGException 
	{
		// Deactivate the cover entity
		deactivateCoverEntity = new McuDeactivate();
		deactivateCoverEntity.setName("Escort Cover Deactivate Cover");
		deactivateCoverEntity.setDesc("Escort Cover Deactivate Cover");
		deactivateCoverEntity.setOrientation(new Orientation());
		deactivateCoverEntity.setPosition(getTargetCoords().copy());				
		deactivateCoverEntity.setTarget(cover.getIndex());
		
		deactivateCoverTimer  = new McuTimer();
		deactivateCoverTimer.setName("Escort Cover Deactivate Cover Timer");
		deactivateCoverTimer.setDesc("Escort Cover Deactivate Cover Timer");
		deactivateCoverTimer.setOrientation(new Orientation());
		deactivateCoverTimer.setPosition(getTargetCoords().copy());				
		deactivateCoverTimer.setTimer(2);				
		deactivateCoverTimer.setTarget(deactivateCoverEntity.getIndex());
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
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = getCampaign().getCampaignConfigManager();
		
		int GroundAttackMinimum = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey);
		int GroundAttackAdditional = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey) + 1;
		numPlanesInFlight = GroundAttackMinimum + RandomNumberGenerator.getRandom(GroundAttackAdditional);
		
        return modifyNumPlanes(numPlanesInFlight);

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
		
		deactivateCoverTimer.write(writer);
		deactivateCoverEntity.write(writer);
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

	public McuTimer getDeactivateCoverTimer() 
	{
		return deactivateCoverTimer;
	}

	public McuDeactivate getDeactivateCoverEntity() 
	{
		return deactivateCoverEntity;
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

