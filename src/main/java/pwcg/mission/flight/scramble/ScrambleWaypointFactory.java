package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class ScrambleWaypointFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();
    
    public ScrambleWaypointFactory(IFlight flight)
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> waypoints = createTargetWaypoints();
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createTargetWaypoints() throws PWCGException  
	{
        List<McuWaypoint> waypoints = new ArrayList<>();

		//McuWaypoint startWP = createScrambleStartWaypoint();
       // waypoints.add(startWP);

		McuWaypoint scrambleTargetWP = createTargetScrambleWaypoint();
		waypoints.add(scrambleTargetWP);

		//McuWaypoint scrambleReturnWP = createReturnScrambleWaypoint(scrambleTargetWP);
		//waypoints.add(scrambleReturnWP);
		
        return waypoints;
	}

    private McuWaypoint createScrambleStartWaypoint() throws PWCGException
    {
        double takeoffOrientation = flight.getFlightInformation().getAirfield().getTakeoffLocation().getOrientation().getyOri();
		int InitialWaypointDistance = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointDistanceKey);
		Coordinate startCoords = MathUtils.calcNextCoord(flight.getFlightInformation().getAirfield().getTakeoffLocation().getPosition(), takeoffOrientation, InitialWaypointDistance);

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int initialWaypointAltitude = productSpecificConfiguration.getInitialWaypointAltitude();
        startCoords.setYPos(initialWaypointAltitude);

		McuWaypoint startWP = WaypointFactory.createPatrolWaypointType();
		startWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		startWP.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
		startWP.setPosition(startCoords);
        return startWP;
    }

    private McuWaypoint createTargetScrambleWaypoint() throws PWCGException
    {
        double angleToTarget = MathUtils.calcAngle(flight.getFlightHomePosition(), flight.getFlightInformation().getTargetPosition());
        Orientation wpOrientation = new Orientation();
        wpOrientation.setyOri(angleToTarget);
        
        Coordinate scrambleTargetCoords =  flight.getFlightInformation().getTargetPosition();
        scrambleTargetCoords.setYPos(flight.getFlightInformation().getAltitude());
         
        McuWaypoint scrambleTargetWP = WaypointFactory.createPatrolWaypointType();
		scrambleTargetWP.setPosition(scrambleTargetCoords);	
		scrambleTargetWP.setOrientation(wpOrientation.copy());
		scrambleTargetWP.setTargetWaypoint(true);
		scrambleTargetWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		scrambleTargetWP.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
        return scrambleTargetWP;
    }

    private McuWaypoint createReturnScrambleWaypoint(McuWaypoint scrambleTargetWP) throws PWCGException
    {
        double returnAngle = MathUtils.adjustAngle(scrambleTargetWP.getOrientation().getyOri(), 180.0);
		Coordinate scrambleSecondaryCoords =  MathUtils.calcNextCoord(scrambleTargetWP.getPosition(), returnAngle, 3000.0);
		scrambleSecondaryCoords.setYPos(flight.getFlightInformation().getAltitude());

		McuWaypoint scrambleSecondaryWP = WaypointFactory.createPatrolWaypointType();
		scrambleSecondaryWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		scrambleSecondaryWP.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
		scrambleSecondaryWP.setPosition(scrambleSecondaryCoords);    
		scrambleSecondaryWP.setTargetWaypoint(true);
		
        Orientation wpOrientation = new Orientation();
        wpOrientation.setyOri(returnAngle);
		scrambleSecondaryWP.setOrientation(wpOrientation.copy());

		return scrambleSecondaryWP;
    }
}
