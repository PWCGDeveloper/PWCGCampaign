package pwcg.mission.flight.escort;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
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
import pwcg.mission.flight.FlightPositionHelperPlayerStart;
import pwcg.mission.flight.FlightTypes;
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

	public PlayerEscortFlight() 
	{
		super();
	}

	public void initialize(
			Mission mission, 
			Campaign campaign, 
			Coordinate targetCoords, 
			Squadron squad, 
            MissionBeginUnit missionBeginUnit,
			boolean isPlayerFlight,
			Flight escortedFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, FlightTypes.ESCORT, targetCoords, squad, missionBeginUnit, isPlayerFlight);

		this.escortedFlight = escortedFlight;
		this.position = escortedFlight.getPosition();
	}

	@Override
	public void createUnitMission() throws PWCGException  
	{
		super.setName(squadron.determineDisplayName(campaign.getDate()));
		super.setPosition(departureAirfield.getPosition().copy());

		createPlanes();
	    List<McuWaypoint> waypointList = createWaypoints(mission, departureAirfield.getPosition());
	    waypointPackage.setWaypoints(waypointList);

		if (playerFlight && !(airstart))
		{
			// Players flight takes off
			createTakeoff();
            createLanding();
		}

		createActivation();
		createFormation();

		// Set the payload for the escort
		setFlightPayload();

		// These are the escort specific things
		createTimers();
		createCover();
		createDeactivate();
		createActivation();

        FlightPositionHelperPlayerStart flightPositionHelperPlayerStart = new FlightPositionHelperPlayerStart(campaign, this);
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
		coverTimer.setName("Cover Timer for " + getSquadron().determineDisplayName(campaign.getDate()));
		coverTimer.setDesc("Cover " + escortedFlight.getSquadron().determineDisplayName(campaign.getDate()));
		coverTimer.setPosition(referencePoint);
		coverTimer.setTarget(cover.getIndex());
	}
	
	/**
	 * @
	 */
	protected void createDeactivate() 
	{
		// Deactivate the cover entity
		deactivateCoverEntity = new McuDeactivate();
		deactivateCoverEntity.setName("Escort Cover Deactivate Cover");
		deactivateCoverEntity.setDesc("Escort Cover Deactivate Cover");
		deactivateCoverEntity.setOrientation(new Orientation());
		deactivateCoverEntity.setPosition(targetCoords.copy());				
		deactivateCoverEntity.setTarget(cover.getIndex());
		
		deactivateCoverTimer  = new McuTimer();
		deactivateCoverTimer.setName("Escort Cover Deactivate Cover Timer");
		deactivateCoverTimer.setDesc("Escort Cover Deactivate Cover Timer");
		deactivateCoverTimer.setOrientation(new Orientation());
		deactivateCoverTimer.setPosition(targetCoords.copy());				
		deactivateCoverTimer.setTimer(2);				
		deactivateCoverTimer.setTarget(deactivateCoverEntity.getIndex());
	}
	
	   
    /**
     * @
     */
    protected void createTimers() 
    {
        escortedFlightWaypointTimer = new McuTimer();
        escortedFlightWaypointTimer.setName("Escort Cover Ingress Timer");
        escortedFlightWaypointTimer.setDesc("Escort Cover Ingress Timer");
        escortedFlightWaypointTimer.setOrientation(new Orientation());
        escortedFlightWaypointTimer.setPosition(targetCoords.copy());              
        escortedFlightWaypointTimer.setTimer(2);                
        
        egressTimer = new McuTimer();
        egressTimer.setName("Escort Cover Egress Timer");
        egressTimer.setDesc("Escort Cover Egress Timer");
        egressTimer.setOrientation(new Orientation());
        egressTimer.setPosition(targetCoords.copy());              
        egressTimer.setTimer(2);                
    }

	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
		
		int GroundAttackMinimum = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey);
		int GroundAttackAdditional = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey) + 1;
		numPlanesInFlight = GroundAttackMinimum + RandomNumberGenerator.getRandom(GroundAttackAdditional);
		
        return modifyNumPlanes(numPlanesInFlight);

	}

	@Override
	protected void createActivation() throws PWCGException 
	{
		// Do this again to compensate for the movement of the escorted flight
	    FlightPositionHelperPlayerStart flightPositionHelperPlayerStart = new FlightPositionHelperPlayerStart(campaign, this);
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
        String objectiveName =  formMissionObjectiveLocation(targetCoords.copy());
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

