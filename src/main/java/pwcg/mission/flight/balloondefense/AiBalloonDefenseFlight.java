package pwcg.mission.flight.balloondefense;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Balloon;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class AiBalloonDefenseFlight extends Flight
{
	protected BalloonDefenseGroup balloonUnit = null;

	public AiBalloonDefenseFlight() 
	{
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Squadron squad, 
				MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight,
				BalloonDefenseGroup balloonUnit) throws PWCGException 
	{
		super.initialize (mission, campaign, FlightTypes.BALLOON_DEFENSE, targetCoords, squad, missionBeginUnit, isPlayerFlight);

		this.balloonUnit = balloonUnit;		
		airstart = true;
	}

	@Override
	public void createUnitMission() throws PWCGException  
	{
	    super.createUnitMission();
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

	public Coordinate getCoordinatesToIntersectWithPlayer() 
	{
 		return targetCoords;
	}

	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
		
		int BalloonBustMinimum = configManager.getIntConfigParam(ConfigItemKeys.BalloonDefenseMinimumKey);
		int BalloonBustAdditional = configManager.getIntConfigParam(ConfigItemKeys.BalloonDefenseAdditionalKey) + 1;
		numPlanesInFlight = BalloonBustMinimum + RandomNumberGenerator.getRandom(BalloonBustAdditional) + 1;
		
		return numPlanesInFlight;

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

	public String getMissionObjective() throws PWCGException 
	{
        String objective = "Defend our balloon" + formMissionObjectiveLocation(targetCoords.copy()) + ".";       

        return objective;
	}

    public Coordinate getBalloonPosition()
    {
        if (balloonUnit != null)
        {
            return this.balloonUnit.getBalloon().getPosition();
        }
        
        return null;
    }

    public void setBalloonCheckZoneForPlayer(int index)
    {
        balloonUnit.setBalloonCheckZoneForPlayer(index);
    }

    public Balloon getBalloon()
    {
        return this.balloonUnit.getBalloon();
    }


}	

