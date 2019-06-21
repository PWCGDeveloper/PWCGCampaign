package pwcg.mission.flight.artySpot;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.artySpot.grid.ArtillerySpotGrid;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerArtillerySpotFlight extends ArtillerySpotFlight
{
	private McuTimer startSpotTimer = new McuTimer();
	private McuTimer loiterCompleteTimer = new McuTimer();
	private McuTimer egressWPTimer = new McuTimer();
	private ArtillerySpotGrid artySpotGrid = null;

    public PlayerArtillerySpotFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
        artySpotGrid = new ArtillerySpotGrid();
    }
	
	public void createArtyGrid(ArtillerySpotArtilleryGroup friendlyArtillery) throws PWCGException 
	{
	    ICountry squadronCountry = flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate());
	    artySpotGrid.create(friendlyArtillery, getTargetCoords(), squadronCountry);
		
		// Timer values
		startSpotTimer.setTimer(1);
		
		ConfigManagerCampaign configManager = getCampaign().getCampaignConfigManager();
		int timeOnStation = configManager.getIntConfigParam(ConfigItemKeys.TimeOnArtillerySpotKey) * 60;
		loiterCompleteTimer.setTimer(timeOnStation); // loiter
		
		egressWPTimer.setTimer(1);

		// MCU Names
		startSpotTimer.setName("Spot Wp  Arrived Timer");
		loiterCompleteTimer.setName("Spot Loiter WP Timer");
		egressWPTimer.setName("Spot Egress WP Timer");
		
		// Position
		Coordinate mcuCoords = MathUtils.calcNextCoord(getTargetCoords(), 0, 1200);
		mcuCoords = MathUtils.calcNextCoord(mcuCoords, 270, 1200);
		startSpotTimer.setPosition(mcuCoords);
		loiterCompleteTimer.setPosition(mcuCoords);
		egressWPTimer.setPosition(mcuCoords);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		PlayerArtySpotWaypoints am = new PlayerArtySpotWaypoints(startPosition, 
													 getTargetCoords(),
													 this,
												 	 getMission());
		return am.createWaypoints();
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		super.write(writer);

		startSpotTimer.write(writer);
		loiterCompleteTimer.write(writer);
		egressWPTimer.write(writer);

		artySpotGrid.write(writer);
	}

	@Override
    protected void createFlightSpecificTargetAssociations()
	{
        linkWPToPlane(getLeadPlane(), getWaypointPackage().getWaypointsForLeadPlane());

		if (waypointPackage.getWaypointsForLeadPlane().size() > 0)
		{
			McuWaypoint prevWP = null;
			
            for (McuWaypoint waypoint : waypointPackage.getWaypointsForLeadPlane())
			{
			    if (prevWP != null)
			    {
    				if (prevWP.getName().contains(WaypointType.ARTILLERY_SPOT_WAYPOINT.getName()))
    				{
    				    createArtyGridTrigger(prevWP, waypoint);
    				}
    				else
    				{
    					prevWP.setTarget(waypoint.getIndex());
    				}
			    }
			    
				prevWP = waypoint;
			}
		}
	}

    public void createArtyGridTrigger(McuWaypoint artillerySpotWaypoint, McuWaypoint egressWaypoint)
    {
    	artillerySpotWaypoint.setTarget(startSpotTimer.getIndex());
    	
        // The arrival timer fans out and triggers several events
        startSpotTimer.setTarget(artySpotGrid.getArtillerySpotMedia().getStartMediaTimer().getIndex());
        startSpotTimer.setTarget(loiterCompleteTimer.getIndex());
                            
        // Once this timer expires ...
        // enable the next WP
        loiterCompleteTimer.setTarget(egressWaypoint.getIndex());
        
        // Stop the media
        loiterCompleteTimer.setTarget(artySpotGrid.getArtillerySpotMedia().getStopMediaTimer().getIndex());

        // Continue on to egress WP
        loiterCompleteTimer.setTarget(egressWPTimer.getIndex());
        egressWPTimer.setTarget(egressWaypoint.getIndex());
        
        // Deactivate the artillery
        // loiterCompleteTimer.setTarget(friendlyArtillery.getDeactivateTimer().getIndex());
    }

	public McuTimer getStartSpotTimer() 
	{
		return startSpotTimer;
	}

	public McuTimer getLoiterCompleteTimer() 
	{
		return loiterCompleteTimer;
	}

	public McuTimer getEgressWPTimer() 
	{
		return egressWPTimer;
	}

	public ArtillerySpotGrid getArtySpotGrid() 
	{
		return artySpotGrid;
	}
}
