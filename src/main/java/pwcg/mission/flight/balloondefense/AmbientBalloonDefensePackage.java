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
import pwcg.campaign.target.TargetDefinitionBuilderGround;
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
    private Mission mission;

    public AmbientBalloonDefensePackage()
    {
    }


	public BalloonDefenseGroup createPackage (Side balloonSide, Coordinate referenceLocation) throws PWCGException 
	{
		// Create a balloon defense package
		Coordinate positionCoordinate = getNearestPositionCoordinate(balloonSide);
		Coordinate balloonPosition = getBalloonCoordinate(positionCoordinate, balloonSide);
		
		
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
		Coordinate balloonPosition = getBalloonCoordinate(balloonCoordinate, side);
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone(balloonPosition.copy(), 12000);
        missionBeginUnit.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalitions(Coalition.getAllCoalitions());
        
        TargetDefinitionBuilderGround targetDefinitionBuilder = new TargetDefinitionBuilderGround(campaign);
        boolean isPlayerTarget = false;
        TargetDefinition ambientBalloonTargetDefinition = targetDefinitionBuilder.buildTargetDefinitionAmbient(balloonCountry, TacticalTarget.TARGET_BALLOON, balloonPosition, isPlayerTarget);
        GroundUnitBalloonFactory balloonFactory = new GroundUnitBalloonFactory(campaign, ambientBalloonTargetDefinition);
        BalloonDefenseGroup balloonUnit = balloonFactory.createBalloonUnit();
		return balloonUnit;
	}

	private Coordinate getNearestPositionCoordinate(Side side) throws PWCGException 
	{
        CoordinateBox missionBorders = mission.getMissionBorders().expandBox(5000);
		PositionsManager positionsManager = new PositionsManager(mission.getCampaign().getDate());
	
		// Make the ambient balloon positions
		// Get the balloon position
		Coordinate balloonPosition = null;
        List<Coordinate> balloonPositions = positionsManager.getPositions(CountryFactory.makeMapReferenceCountry(side), missionBorders);
        int index = RandomNumberGenerator.getRandom(balloonPositions.size());
        balloonPosition = balloonPositions.get(index);
		
		return balloonPosition;
	}

	private Squadron getBalloonSquadronForClosestCountry(Side side, Coordinate balloonPosition) throws PWCGException 
	{		
		Squadron squad = null;
		List<Squadron> squadrons = new ArrayList<Squadron>();
		squadrons =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsBySide(campaign, 
		        balloonPosition, 1, 200000.0, side, campaign.getDate());
		if (squadrons.size() > 0)
		{
			squad = squadrons.get(0);
		}

		return squad;
	}

	private Coordinate getBalloonCoordinate(Coordinate referenceCoordinate, Side side) 
	{
		Coordinate targetWaypoint = null;
		
		PositionsManager positionsManager = new PositionsManager(campaign.getDate());

		targetWaypoint = positionsManager.getClosestDefinitePosition(side, referenceCoordinate.copy());

		return targetWaypoint;
	}

}
