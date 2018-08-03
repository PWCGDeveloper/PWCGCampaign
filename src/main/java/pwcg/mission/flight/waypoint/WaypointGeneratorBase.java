package pwcg.mission.flight.waypoint;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.MissionAltitudeGeneratorFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public abstract class WaypointGeneratorBase 
{
    public static int INGRESS_DISTANCE_FROM_FRONT = 10000;
	
    protected int waypointSpeed = 250;
	protected List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();
	protected Coordinate startCoords;
	protected Coordinate targetCoords;
	protected double flightAlt = 2000.0;
	protected Flight flight;
	protected Mission mission;
	protected Campaign campaign;

	protected abstract void createTargetWaypoints(Coordinate startPosition) throws PWCGException ;

	public WaypointGeneratorBase(Coordinate startCoords, 
			  		   Coordinate targetCoords, 
			  		   Flight flight,
			  		   Mission mission) throws PWCGException 
	{
		campaign = mission.getCampaign();
		
		this.startCoords = startCoords.copy();
		this.targetCoords = targetCoords.copy();
		this.flight = flight;
		this.mission = mission;
		this.waypointSpeed = flight.getPlanes().get(0).getCruisingSpeed();
		
		this.flightAlt = determineFlightAltitude();
	}

    protected int determineFlightAltitude() throws PWCGException 
    {
        IMissionAltitudeGenerator missionAltitudeGenerator = MissionAltitudeGeneratorFactory.createMissionAltitudeGeneratorFactory();
        int altitude = missionAltitudeGenerator.flightAltitude(campaign);

        return altitude;
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException 
    {
    	Coordinate ingressTransitionCoordinate;
                        
        if (flight.isPlayerFlight())
        {
            ClimbWaypointGenerator climbWaypointGenerator = new ClimbWaypointGenerator(campaign, flight);
            List<McuWaypoint> climbWPs = climbWaypointGenerator.createClimbWaypoints(flightAlt);
    
            waypoints.addAll(climbWPs);
    
            McuWaypoint lastClimbWP = climbWPs.get(climbWPs.size()-1);

            ingressTransitionCoordinate = lastClimbWP.getPosition();
        }
        else
        {
        	ingressTransitionCoordinate = flight.getSquadron().determineCurrentPosition(campaign.getDate()).copy();
        }

        McuWaypoint ingressWP = createIngressWaypoint (flight, ingressTransitionCoordinate);
		createTargetWaypoints(ingressWP.getPosition());
        createEgressWaypoint(waypoints.get(waypoints.size()-1).getPosition());
		createApproachWaypoint(flight.getAirfield());		

        return waypoints;
    }

	protected McuWaypoint createIngressWaypoint(Flight flight, Coordinate lastPosition) throws PWCGException  
	{
	    IIngressWaypoint ingressWaypointGenerator = IngressWaypointFactory.getIngressGenerator(flight, lastPosition, targetCoords, waypointSpeed, new Double(flightAlt).intValue());
	    McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        waypoints.add(ingressWaypoint);
		return ingressWaypoint;
	}

    protected double getWaypointAltitude(Coordinate previousPosition, Coordinate nextPosition, double desiredAltitude) 
                    throws PWCGException 
    {
        double distanceBetweenWP =  MathUtils.calcDist(previousPosition, nextPosition);
        // Number of minutes from on WP to another
        double minutesToNextWP = (distanceBetweenWP / 1000.0 / waypointSpeed) * 60;
        // Assume 1400 feet (230 meters) per minute
        double maxAcheivableAltitudeGain = minutesToNextWP * 400.0;

        double maxAcheivableAltitude = previousPosition.getYPos() + maxAcheivableAltitudeGain;
        if (maxAcheivableAltitude < (desiredAltitude * .8))
        {
            maxAcheivableAltitude = desiredAltitude * .8;
        }

        double wpAltitude = desiredAltitude;
        if (maxAcheivableAltitude < desiredAltitude)
        {
            wpAltitude = maxAcheivableAltitude;
        }

        // Never too low
        int InitialWaypointAltitude = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointAltitudeKey);
        if (wpAltitude < InitialWaypointAltitude)
        {
            wpAltitude = InitialWaypointAltitude;
        }
        
        return wpAltitude;
    }

	protected void createEgressWaypoint(Coordinate lastWaypointCoord) throws PWCGException  
	{
	    Coordinate egressCoord = createEgressCoordinates(lastWaypointCoord);
	    
	    egressCoord.setYPos(getFlightAlt());

	    McuWaypoint egressWP = WaypointFactory.createEgressWaypointType();
		egressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		egressWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), WaypointType.EGRESS_WAYPOINT.getName());
		egressWP.setSpeed(waypointSpeed);
		egressWP.setPosition(egressCoord);
		
		waypoints.add(egressWP);
	}

    private Coordinate createEgressCoordinates(Coordinate lastWaypointCoord) throws PWCGException
    {
        Side side = flight.getSquadron().determineSquadronCountry(campaign.getDate()).getSide();
        FrontLinesForMap frontLines =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate closestFrontToLastWaypoint = frontLines.findClosestFrontCoordinateForSide(lastWaypointCoord, side);

	    IAirfield airfield = flight.getSquadron().determineCurrentAirfieldCurrentMap(campaign.getDate());
	    double angleFromFieldToFront = MathUtils.calcAngle(airfield.getPosition(), closestFrontToLastWaypoint);
	    
        Coordinate closestFrontToAirfield = frontLines.findClosestFrontCoordinateForSide(airfield.getPosition(), side);
	    double distanceFromAirfieldToFront = MathUtils.calcDist(airfield.getPosition(), closestFrontToAirfield);
	    double distanceEgressFromAirfield = INGRESS_DISTANCE_FROM_FRONT;
	    if (distanceFromAirfieldToFront < INGRESS_DISTANCE_FROM_FRONT)
	    {
	        distanceEgressFromAirfield = distanceFromAirfieldToFront;
	    }
	    Coordinate egressCoord = MathUtils.calcNextCoord(airfield.getPosition(), angleFromFieldToFront, distanceEgressFromAirfield);
        return egressCoord;
    }

	protected void createApproachWaypoint(IAirfield airfield) throws PWCGException  
	{
		PWCGLocation landingLocation = airfield.getLandingLocation();
        int initialWaypointDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointDistanceKey);
	    Coordinate approachCoords = MathUtils.calcNextCoord(landingLocation.getPosition(), landingLocation.getOrientation().getyOri() - 180, initialWaypointDistance);
		
		int ApproachWaypointAltitude = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.ApproachWaypointAltitudeKey);
		approachCoords.setYPos(airfield.getPosition().getYPos() + ApproachWaypointAltitude);

		Orientation orient = new Orientation();
		orient.setyOri(landingLocation.getOrientation().getyOri());

		McuWaypoint approachWP = WaypointFactory.createLandingApproachWaypointType();
		approachWP.setTriggerArea(McuWaypoint.LAND_AREA);
		approachWP.setSpeed(220);
		approachWP.setPosition(approachCoords);
		approachWP.setOrientation(orient);
		
		waypoints.add(approachWP);
	}

	public void dumpWaypoints()
	{
		McuWaypoint lastWP = null;
		
		NumberFormat numberFormat = new DecimalFormat("###.0");

		for (int i = 0; i < waypoints.size(); ++i)
		{
			McuWaypoint wp = waypoints.get(i);
			StringBuffer buffer = new StringBuffer("");
			buffer.append("Waypoint #" + i);
			buffer.append("     " + wp.getName());
			buffer.append("     " + numberFormat.format(wp.getPosition().getXPos()) );
			buffer.append("     " + numberFormat.format(wp.getPosition().getZPos()) );
			buffer.append("     " + numberFormat.format(wp.getPosition().getYPos()) );
			
			if (i > 1)
			{
				double distance = MathUtils.calcDist(lastWP.getPosition(), wp.getPosition());		
				buffer.append("     " + numberFormat.format(distance));
			}
			
			Logger.log(LogLevel.DEBUG, buffer.toString());
			
			lastWP = wp;
		}
		
		Logger.log(LogLevel.DEBUG, "");
	}

	public static McuWaypoint findWaypointByType(List<McuWaypoint> waypointList, String type)
	{
		McuWaypoint theWP = null;
		// we have to link deactivation to the escorted flights egress
		for (McuWaypoint waypoint : waypointList)
		{
			if (waypoint.getName().equals(type))
			{
				theWP = waypoint;
				break;
			}
		}
		
		return theWP;
	}

    protected int correctStartFrontIndex(int startFrontIndex, List<FrontLinePoint> frontLines)
    {
        final int closestToEdge = 3;

        if (startFrontIndex < closestToEdge)
        {
            startFrontIndex = closestToEdge;
        }
        // at southern edge - go north
        else if (startFrontIndex > (frontLines.size() - closestToEdge) )
        {
            startFrontIndex = frontLines.size() - closestToEdge;
        }
        
        return startFrontIndex;
    }

    protected boolean goNorth(int startFrontIndex, Side side) throws PWCGException 
    {
        final int closestToEdge = 10;

        boolean goNorth = true;
        
        // At northern edge - go south
        FrontLinesForMap frontLineMarker =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(side);
        if (startFrontIndex < closestToEdge)
        {
            goNorth = false;
        }
        // at southern edge - go north
        else if (startFrontIndex > (frontLines.size() - closestToEdge) )
        {
            goNorth = true;
        }
        // in the middle - either direction
        else
        {
            int goNorthInt = RandomNumberGenerator.getRandom(100);
            if (goNorthInt < 50)
            {
                goNorth = false;
            }
        }
        
        return goNorth;
    }

    protected int getNextFrontIndex(int startFrontIndex, boolean goNorth, int numToAdvance, Side side) throws PWCGException
    {
        int frontIndex;
        if (goNorth)
        {
            frontIndex = startFrontIndex - numToAdvance;
        }
        else
        {
            frontIndex = startFrontIndex + numToAdvance;
        }
        
        // Don't go too far North
        if (frontIndex < 3)
        {
            frontIndex = 2;
        }
        
        // Don't go too far South
        FrontLinesForMap frontLineMarker =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(side);
        if (frontIndex > frontLines.size())
        {
            frontIndex = frontLines.size()-2;
        }

        
        return frontIndex;
    }

    protected boolean isEdgeOfMap(int frontIndex, boolean goNorth, Side side) throws PWCGException
    {
        if (goNorth && frontIndex < 4)
        {
            return true;
        }
        
        FrontLinesForMap frontLineMarker =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(side);
        if (!goNorth && (frontIndex > (frontLines.size() - 4)))
        {
            return true;
        }
        
        return false;
    }

	protected void setWaypointsNonFighterPriority()
	{
		for (McuWaypoint waypoint : waypoints)
		{
			if (waypoint.getPriority().getPriorityValue() < WaypointPriority.PRIORITY_MED.getPriorityValue())
			{
				waypoint.setPriority(WaypointPriority.PRIORITY_MED);
			}
		}
	}

	public double getFlightAlt() 
	{
		return flightAlt;
	}

    protected List<McuWaypoint> getTargetWaypoints(List<McuWaypoint> playerWaypoints)
    {
        List<McuWaypoint> selectedWaypoints = new ArrayList <McuWaypoint>();
        
        for (int i = 0; i < playerWaypoints.size(); ++i)
        {
            McuWaypoint playerWaypoint = playerWaypoints.get(i);
            if (playerWaypoint.getWpAction() == WaypointAction.WP_ACTION_PATROL ||
                playerWaypoint.getWpAction() == WaypointAction.WP_ACTION_RECON)
                            
            {
                selectedWaypoints.add(playerWaypoint);
            }
        }

        return selectedWaypoints;
    }
}
