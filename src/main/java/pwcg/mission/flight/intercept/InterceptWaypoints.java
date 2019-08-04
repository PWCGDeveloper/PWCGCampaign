package pwcg.mission.flight.intercept;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.MissionAltitudeGeneratorFactory;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointPatternFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptWaypoints extends WaypointGeneratorBase
{
    private InterceptSearchPattern pattern = InterceptSearchPattern.INTERCEPT_CROSS;
                    
    private enum InterceptSearchPattern
    {
        INTERCEPT_CIRCLE,
        INTERCEPT_CREEP,
        INTERCEPT_CROSS
    }
    
    private static final int NUM_LEGS_IN_INTERCEPT_CIRCLE = 6;
    private static final int NUM_SEGMENTS_IN_INTERCEPT_CREEP = 2;

	public InterceptWaypoints(Coordinate startCoords, 
					  Coordinate targetCoords, 
			    	  Flight flight,
			    	  Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);

		pattern = selectSearchPattern();
	}

	protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
	    
		McuWaypoint startWP = createInterceptFirstWP(startPosition);
		waypoints.add(startWP);		
        
        List<McuWaypoint> interceptWPs = this.createSearchPatternWaypoints(startWP);
        waypoints.addAll(interceptWPs);        
	}

    private McuWaypoint createInterceptFirstWP(Coordinate startPosition) throws PWCGException
    {
		Coordinate coord = createInterceptFirstWPCoordinates(startPosition);

		McuWaypoint innerLoopFirstWP = WaypointFactory.createPatrolWaypointType();
        innerLoopFirstWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		innerLoopFirstWP.setSpeed(waypointSpeed);
		innerLoopFirstWP.setPosition(coord);	
		innerLoopFirstWP.setTargetWaypoint(true);
		
		double initialAngle = MathUtils.calcAngle(startPosition, targetCoords);
		innerLoopFirstWP.getOrientation().setyOri(initialAngle);
		
		return innerLoopFirstWP;
    }

    private Coordinate createInterceptFirstWPCoordinates(Coordinate startPosition) throws PWCGException
    {
        // This puts the WP right in the middle of the pattern
        double angleToMovePattern = getPatternMoveAngleForPattern(targetCoords, startPosition);
        Coordinate coordinatesAfterFixedMove = getPatternMoveDistanceForPattern(angleToMovePattern);

        // Move the WP a bit so it is not so precise
        double randomOffsetAngle = RandomNumberGenerator.getRandom(360);
        double randomOffsetDistance = RandomNumberGenerator.getRandom(5000);
        Coordinate coordinatesAfterRandomMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, randomOffsetAngle, randomOffsetDistance);

        coordinatesAfterRandomMove.setYPos(getFlightAlt());

        return coordinatesAfterRandomMove;
    }
    

    
    private double getPatternMoveAngleForPattern(Coordinate targetCoords, Coordinate startPosition) throws PWCGException
    {
        double angleToMovePattern = 0.0;
        
        if (pattern  == InterceptSearchPattern.INTERCEPT_CROSS)
        {
            angleToMovePattern = MathUtils.calcAngle(targetCoords, startPosition);
        }
        else if (pattern  == InterceptSearchPattern.INTERCEPT_CREEP)
        {
            angleToMovePattern = MathUtils.calcAngle(targetCoords, startPosition);
        }
        else
        {
            angleToMovePattern = MathUtils.calcAngle(targetCoords, startPosition);
        }
        
        return angleToMovePattern;
    }

    private Coordinate getPatternMoveDistanceForPattern (double angleToMovePattern) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        Coordinate coordinatesAfterFixedMove = null;
        double distanceToMovePattern = 1000.0;
        
        if (pattern  == InterceptSearchPattern.INTERCEPT_CROSS)
        {
            distanceToMovePattern = productSpecific.getInterceptCrossDiameterDistance() / 2;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(targetCoords, angleToMovePattern, distanceToMovePattern);
        }
        else if (pattern  == InterceptSearchPattern.INTERCEPT_CREEP)
        {
            distanceToMovePattern = productSpecific.getInterceptCreepCrossDistance() * 4;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(targetCoords, angleToMovePattern, distanceToMovePattern);
            
            double shiftAngle = MathUtils.adjustAngle(angleToMovePattern, 90);
            double distanceToShiftPattern = productSpecific.getInterceptCreepLegDistance() * .5;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle, distanceToShiftPattern);
        }
        else
        {
            // This is all based on trial and error
            coordinatesAfterFixedMove = targetCoords.copy();
            
            double shiftAngle = MathUtils.adjustAngle(angleToMovePattern, 90);
            double distanceToShiftPattern = productSpecific.getInterceptInnerLoopDistance() / 1.5;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle, distanceToShiftPattern);
            
            double shiftAngle2 = MathUtils.adjustAngle(angleToMovePattern, 180);
            double distanceToShiftPattern2 = productSpecific.getInterceptInnerLoopDistance() / 3;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle2, distanceToShiftPattern2);
        }
        
        return coordinatesAfterFixedMove;
    }

    private InterceptSearchPattern selectSearchPattern () 
    {
        int searchPatternRoll = RandomNumberGenerator.getRandom(100);
        if (searchPatternRoll < 40)
        {
            return  InterceptSearchPattern.INTERCEPT_CROSS;
        }
        else if (searchPatternRoll < 75)
        {
            return  InterceptSearchPattern.INTERCEPT_CREEP;
        }
        else
        {          
            return  InterceptSearchPattern.INTERCEPT_CIRCLE;
        }
    }

    private List<McuWaypoint> createSearchPatternWaypoints (McuWaypoint lastWP) throws PWCGException
    {
        if (pattern  == InterceptSearchPattern.INTERCEPT_CROSS)
        {
            return  createCrossPattern (lastWP);
        }
        else if (pattern  == InterceptSearchPattern.INTERCEPT_CREEP)
        {
            return createCreepingPattern (lastWP);
        }
        else
        {
            return createCirclePattern(lastWP);
        }
    }

    private List<McuWaypoint> createCirclePattern (McuWaypoint innerLoopFirstWP) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int innerLoopDistance = productSpecific.getInterceptInnerLoopDistance();
        
        // Inner loop
        List<McuWaypoint> interceptWPs = WaypointPatternFactory.generateCirclePattern(
        		campaign,
        		flight, 
        		WaypointType.INTERCEPT_WAYPOINT, 
        		WaypointAction.WP_ACTION_PATROL, 
        		McuWaypoint.FLIGHT_AREA,
        		NUM_LEGS_IN_INTERCEPT_CIRCLE,
        		innerLoopFirstWP,
        		flightAlt,
        		innerLoopDistance);
                        
        return interceptWPs;
    }

    private List<McuWaypoint> createCreepingPattern (McuWaypoint lastWP) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int creepLegDistance = productSpecific.getInterceptCreepLegDistance();
        int creepCrossDistance = productSpecific.getInterceptCreepCrossDistance();
        
        // Inner loop
        List<McuWaypoint> interceptWPs = WaypointPatternFactory.generateCreepingPattern(
        		campaign, 
        		flight, 
        		WaypointType.INTERCEPT_WAYPOINT, 
        		WaypointAction.WP_ACTION_PATROL, 
        		McuWaypoint.FLIGHT_AREA,
        		NUM_SEGMENTS_IN_INTERCEPT_CREEP,
        		lastWP,
        		creepLegDistance,
        		creepCrossDistance);
                        
        return interceptWPs;
    }

    private List<McuWaypoint> createCrossPattern (McuWaypoint lastWP) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int crossDistance = productSpecific.getInterceptCrossDiameterDistance();
        
        // Inner loop
        List<McuWaypoint> interceptWPs = WaypointPatternFactory.generateCrossPattern(
        		campaign, 
        		flight, 
        		WaypointType.INTERCEPT_WAYPOINT, 
        		WaypointAction.WP_ACTION_PATROL, 
        		McuWaypoint.FLIGHT_AREA,
        		lastWP,
        		crossDistance);
                        
        return interceptWPs;
    }

    @Override
    protected void createEgressWaypoint(Coordinate lastWaypointCoord) throws PWCGException  
    {
        IAirfield airfield = flight.getSquadron().determineCurrentAirfieldCurrentMap(campaign.getDate());
        if (airfield == null)
        {
            throw new PWCGException("No airfield found for squadron " + flight.getSquadron().getSquadronId() + ".  Should not have been included in mission");
        }
        
        double angleFromFrontToField = MathUtils.calcAngle(lastWaypointCoord, airfield.getPosition());
        double distanceFromFrontToField = MathUtils.calcDist(lastWaypointCoord, airfield.getPosition());
        Coordinate egressCoord = MathUtils.calcNextCoord(lastWaypointCoord, angleFromFrontToField, (distanceFromFrontToField / 2));
        egressCoord.setYPos(getFlightAlt());

        McuWaypoint egressWP = WaypointFactory.createEgressWaypointType();
        egressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        egressWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), WaypointType.EGRESS_WAYPOINT.getName());
        egressWP.setSpeed(waypointSpeed);
        egressWP.setPosition(egressCoord);
        
        waypoints.add(egressWP);
    }
    
    @Override
    protected int determineFlightAltitude() throws PWCGException 
    {
        if (flight.getFlightType().equals(FlightTypes.LOW_ALT_CAP))
        {
            IMissionAltitudeGenerator missionAltitudeGenerator = MissionAltitudeGeneratorFactory.createMissionAltitudeGeneratorFactory();
            return missionAltitudeGenerator.getLowAltitudePatrolAltitude();
        }
        else
        {
            return super.determineFlightAltitude();
        }
    }
}
