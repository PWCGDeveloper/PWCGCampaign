package pwcg.mission.flight.balloondefense;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.mcu.McuWaypoint;

public class AiBalloonDefenseFlight extends Flight
{
	protected IGroundUnitCollection balloonUnit = null;

    public AiBalloonDefenseFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, IGroundUnitCollection balloonUnit)
    {
        super (flightInformation, missionBeginUnit);
        this.balloonUnit = balloonUnit;     
    }

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
        int waypointSpeed = getPlanes().get(0).getCruisingSpeed();

        List<McuWaypoint> wpList = new ArrayList<McuWaypoint>();

        McuWaypoint balloonDefenseIngressWP = makeBalloonConverIngressWP(startPosition, waypointSpeed);
        wpList.add(balloonDefenseIngressWP);

        McuWaypoint balloonDefenseWP = makeBalloonCoverWP(waypointSpeed);
        wpList.add(balloonDefenseWP);

		return wpList;
	}

    private McuWaypoint makeBalloonConverIngressWP(Coordinate startPosition,  int waypointSpeed) throws PWCGException
    {
        double balloonCoverApproachAngle = MathUtils.calcAngle(balloonUnit.getPosition(), startPosition);               
        Coordinate balloonCoverIngressPosition = MathUtils.calcNextCoord(balloonUnit.getPosition(), balloonCoverApproachAngle, 3000.0);
        balloonCoverIngressPosition.setYPos(balloonUnit.getPosition().getYPos() + 500);

        McuWaypoint balloonDefenseIngressWP = WaypointFactory.createIngressWaypointType();
        balloonDefenseIngressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        balloonDefenseIngressWP.setSpeed(waypointSpeed);
        balloonDefenseIngressWP.setPosition(balloonCoverIngressPosition);
        
        return balloonDefenseIngressWP;
    }

    private McuWaypoint makeBalloonCoverWP(int waypointSpeed) throws PWCGException
    {
        Coordinate balloonCoverPosition = balloonUnit.getPosition().copy();
		balloonCoverPosition.setYPos(balloonUnit.getPosition().getYPos() + 500);

		McuWaypoint balloonDefenseWP = WaypointFactory.createBalloonDefenseWaypointType();
		balloonDefenseWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		balloonDefenseWP.setSpeed(waypointSpeed);
		balloonDefenseWP.setPosition(balloonCoverPosition);
		
		return balloonDefenseWP;
    }

	public Coordinate getCoordinatesToIntersectWithPlayer() throws PWCGException 
	{
 		return getTargetCoords();
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }

	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		super.write(writer);
	}

    public Coordinate getBalloonPosition() throws PWCGException
    {
        if (balloonUnit != null)
        {
            return this.balloonUnit.getPosition();
        }
        
        return null;
    }
}	

