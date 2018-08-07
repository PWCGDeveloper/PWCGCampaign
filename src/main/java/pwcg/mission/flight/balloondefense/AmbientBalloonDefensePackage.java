 package pwcg.mission.flight.balloondefense;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PositionsManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.TargetDefinitionBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.ground.GroundUnitBalloonFactory;
import pwcg.mission.mcu.Coalition;

public class AmbientBalloonDefensePackage
{
    private Campaign campaign;
    private Mission mission;
	
	public BalloonDefenseGroup createPackage (Campaign campaign, Mission mission, Side side) throws PWCGException 
	{
        this.campaign = campaign;
        this.mission = mission;

		// Create a balloon defense package
		Coordinate positionCoordinate = getNearestPositionCoordinate(side);
		Coordinate balloonPosition = getBalloonCoordinate(mission, positionCoordinate, side);
		
		
		ICountry country = getAmbientBalloonCountry(positionCoordinate, side);
		BalloonDefenseGroup balloonUnit = createBalloon (balloonPosition, country);
		
		PlaneMCU playerPlane = mission.getMissionFlightBuilder().getPlayerFlight().getPlayerPlanes().get(0);
		balloonUnit.setBalloonCheckZoneForPlayer(playerPlane.getEntity().getIndex());

		return balloonUnit;
	}

	private ICountry getAmbientBalloonCountry(Coordinate positionCoordinate, Side side) throws PWCGException 
	{
	    ICountry country = CountryFactory.makeMapReferenceCountry(side);
	    
        Squadron squad = getBalloonSquadronForClosestCountry(side, positionCoordinate);
        if (squad != null)
        {
            Campaign campaign = PWCGContextManager.getInstance().getCampaign();
            country = squad.determineSquadronCountry(campaign.getDate());
        }
        
        return country;
	}

	private BalloonDefenseGroup createBalloon (Coordinate balloonCoordinate, ICountry balloonCountry) throws PWCGException 
	{
		// Get the target waypoint - this will be near a friendly infantry position
		Side side = balloonCountry.getSide();
		Coordinate balloonPosition = getBalloonCoordinate(mission, balloonCoordinate, side);

		// No Coalition for a balloon.  Trigger on proximity to player instead
        Coalition enemyCoalition = Coalition.getEnemyCoalition(balloonCountry);
	
		// Create a balloon defense package
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(balloonPosition.copy(), 10000, enemyCoalition);
        
        boolean isPlayerTarget = true;
        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder();
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionNoFlight(campaign, balloonCountry, TacticalTarget.TARGET_BALLOON, balloonPosition, isPlayerTarget);

        GroundUnitBalloonFactory balloonFactory = new GroundUnitBalloonFactory(campaign, targetDefinition);
        BalloonDefenseGroup balloonUnit = balloonFactory.createBalloonUnit();

		return balloonUnit;
	}

	private Coordinate getNearestPositionCoordinate(Side side) throws PWCGException 
	{
		// Positions in the mission box
		// The mission box
	    CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(5000);
		PositionsManager positionsManager = new PositionsManager(mission.getCampaign().getDate());
	
		// Make the ambient balloon positions
		// Get the balloon position
		Coordinate balloonPosition = null;
        List<Coordinate> balloonPositions = positionsManager.getPositions(CountryFactory.makeMapReferenceCountry(side), missionBorders);
        if (balloonPositions.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(balloonPositions.size());
            balloonPosition = balloonPositions.get(index);
        }
        else
        {
            balloonPosition = positionsManager.getClosestDefinitePosition(side, mission.getMissionFlightBuilder().getPlayerFlight().getPlanes().get(0).getPosition());
        }
		
		return balloonPosition;
	}

	private Squadron getBalloonSquadronForClosestCountry(Side side, Coordinate balloonPosition) throws PWCGException 
	{		
		Squadron squad = null;
		List<Squadron> squadrons = new ArrayList<Squadron>();
		squadrons =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsBySide(balloonPosition, 1, 200000.0, side, mission.getCampaign().getDate());
		if (squadrons.size() > 0)
		{
			squad = squadrons.get(0);
		}

		return squad;
	}

	protected Coordinate getBalloonCoordinate(Mission mission, Coordinate referenceCoordinate, Side side) 
	{
		Coordinate targetWaypoint = null;
		
		PositionsManager positionsManager = new PositionsManager(mission.getCampaign().getDate());

		targetWaypoint = positionsManager.getClosestDefinitePosition(side, referenceCoordinate.copy());

		return targetWaypoint;
	}

}
