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
import pwcg.mission.ground.GroundUnitBalloonFactory;
import pwcg.mission.mcu.Coalition;

public class AmbientBalloonDefensePackage
{
    private Campaign campaign;
    private Coordinate referenceLocation;
    private Mission mission;
	
	public BalloonDefenseGroup createPackage (Campaign campaign, Mission mission, Side balloonSide, Coordinate referenceLocation) throws PWCGException 
	{
        this.campaign = campaign;
        this.mission = mission;
        this.referenceLocation = referenceLocation;

		// Create a balloon defense package
		Coordinate positionCoordinate = getNearestPositionCoordinate(balloonSide);
		Coordinate balloonPosition = getBalloonCoordinate(mission, positionCoordinate, balloonSide);
		
		
		ICountry country = getAmbientBalloonCountry(positionCoordinate, balloonSide);
		BalloonDefenseGroup balloonUnit = createBalloon (balloonPosition, country);
		
		List<Integer> playerPlaneIds = mission.getMissionFlightBuilder().determinePlayerPlaneIds();
		balloonUnit.setBalloonCheckZoneForPlayer(playerPlaneIds);

		return balloonUnit;
	}

	private ICountry getAmbientBalloonCountry(Coordinate positionCoordinate, Side balloonSide) throws PWCGException 
	{
	    ICountry country = CountryFactory.makeMapReferenceCountry(balloonSide);
	    
        Squadron squad = getBalloonSquadronForClosestCountry(balloonSide, positionCoordinate);
        if (squad != null)
        {
            Campaign campaign = PWCGContextManager.getInstance().getCampaign();
            country = squad.determineSquadronCountry(campaign.getDate());
        }
        
        return country;
	}

	private BalloonDefenseGroup createBalloon (Coordinate balloonCoordinate, ICountry balloonCountry) throws PWCGException 
	{
		Side side = balloonCountry.getSide();
		Coordinate balloonPosition = getBalloonCoordinate(mission, balloonCoordinate, side);

        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone(balloonPosition.copy(), 12000);
        missionBeginUnit.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalitions(Coalition.getAllCoalitions());
        
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
            balloonPosition = positionsManager.getClosestDefinitePosition(side, referenceLocation);
        }
		
		return balloonPosition;
	}

	private Squadron getBalloonSquadronForClosestCountry(Side side, Coordinate balloonPosition) throws PWCGException 
	{		
		Squadron squad = null;
		List<Squadron> squadrons = new ArrayList<Squadron>();
		squadrons =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsBySide(campaign, balloonPosition, 1, 200000.0, side, mission.getCampaign().getDate());
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
