package pwcg.mission.flight.artySpot;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class ArtillerySpotWaypointFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public ArtillerySpotWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        createTargetWaypoints(ingressWaypoint.getPosition());
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);
        return missionPointSet;
    }

	private void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
		McuWaypoint artillerySpotInitialWaypoint = WaypointFactory.createArtillerySpotWaypointType();

		artillerySpotInitialWaypoint.setTriggerArea(McuWaypoint.TARGET_AREA);
		artillerySpotInitialWaypoint.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
        artillerySpotInitialWaypoint.setTargetWaypoint(true);

		Coordinate initialArtillerySpotCoord = flight.getFlightInformation().getTargetPosition();
		initialArtillerySpotCoord.setYPos(flight.getFlightInformation().getAltitude());
		artillerySpotInitialWaypoint.setPosition(initialArtillerySpotCoord);	

		missionPointSet.addWaypoint(artillerySpotInitialWaypoint);		
		
		int iterNum = 1;
		double angle = RandomNumberGenerator.getRandom(360);	
		createNextWaypoint(artillerySpotInitialWaypoint, iterNum, angle);
	}

	private void createNextWaypoint(McuWaypoint lastWP, int iterNum, double angle) throws PWCGException  
	{
		McuWaypoint artillerySpotAdditionalWaypoint = WaypointFactory.createArtillerySpotWaypointType();
		++ iterNum;

		artillerySpotAdditionalWaypoint.setTriggerArea(McuWaypoint.TARGET_AREA);
		artillerySpotAdditionalWaypoint.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());

		double distance = 2000.0;
		Coordinate nextArtillerySpotCoordinate = MathUtils.calcNextCoord(lastWP.getPosition(), angle, distance);
        nextArtillerySpotCoordinate.setYPos(flight.getFlightInformation().getAltitude());
		artillerySpotAdditionalWaypoint.setPosition(nextArtillerySpotCoordinate);	
		artillerySpotAdditionalWaypoint.setTargetWaypoint(true);

		missionPointSet.addWaypoint(artillerySpotAdditionalWaypoint);
		
		if (iterNum == 20)
		{
			return;
		}

		angle = MathUtils.adjustAngle (angle, 45);		
		
		createNextWaypoint(artillerySpotAdditionalWaypoint, iterNum, angle);
	}
}
