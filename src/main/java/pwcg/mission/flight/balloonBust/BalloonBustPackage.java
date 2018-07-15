package pwcg.mission.flight.balloonBust;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PositionsManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.balloondefense.AiBalloonDefenseFlight;
import pwcg.mission.flight.balloondefense.BalloonDefenseGroup;
import pwcg.mission.ground.GroundUnitBalloonFactory;

public class BalloonBustPackage extends FlightPackage
{
    public BalloonBustPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.BALLOON_BUST;
    }

    public Flight createPackage () throws PWCGException 
    {
        Side enemySide = campaign.determineCountry().getSide().getOppositeSide();

        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
		Coordinate balloonPosition = getTargetWaypoint(mission, startCoords, enemySide);
        Squadron enemyScoutSquadron = PWCGContextManager.getInstance().getSquadronManager().getEnemySquadronByRole(squadron.determineSquadronCountry(campaign.getDate()), Role.ROLE_FIGHTER, campaign.getDate());
        ICountry balloonCountry = determineBalloonCountry(enemySide, enemyScoutSquadron);
        BalloonDefenseGroup balloonUnit = createBalloonUnit(balloonPosition, balloonCountry);
		BalloonBustFlight balloonBust = createFlight(startCoords, balloonUnit);
		createBalloonCoverFlight(enemyScoutSquadron, balloonUnit, balloonBust);

		return balloonBust;
	}

    private BalloonBustFlight createFlight(Coordinate startCoords, BalloonDefenseGroup balloonUnit) throws PWCGException
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());

        BalloonBustFlight balloonBust = new BalloonBustFlight ();
		balloonBust.initialize(mission, campaign, balloonUnit.getPwcgGroundUnitInformation().getPosition(), squadron, missionBeginUnit, isPlayerFlight);

		balloonBust.addLinkedUnit(balloonUnit);
		
		balloonBust.createUnitMission();
        return balloonBust;
    }

    private BalloonDefenseGroup createBalloonUnit(Coordinate balloonPosition, ICountry balloonCountry) throws PWCGException
    {
        GroundUnitBalloonFactory balloonFactory = new GroundUnitBalloonFactory(campaign, balloonPosition, balloonCountry);
        return balloonFactory.createBalloonUnit();
    }

    private ICountry determineBalloonCountry(Side enemySide, Squadron enemyScoutSquadron) throws PWCGException
    {
        ICountry balloonCountry;
        if (enemyScoutSquadron != null)
        {
            int enemyCountryCode = enemyScoutSquadron.determineSquadronCountry(campaign.getDate()).getCountryCode();
            balloonCountry = CountryFactory.makeCountryByCode(enemyCountryCode);
        }
        else
        {
            balloonCountry = CountryFactory.makeMapReferenceCountry(enemySide);
        }
        return balloonCountry;
    }

    private void createBalloonCoverFlight(Squadron enemyScoutSquadron, BalloonDefenseGroup balloonUnit, BalloonBustFlight balloonBust) throws PWCGException
    {
        if(isPlayerFlight)
		{
            MissionBeginUnit missionBeginUnitCover = new MissionBeginUnit();
            missionBeginUnitCover.initialize(balloonUnit.getPwcgGroundUnitInformation().getPosition().copy());

            if (enemyScoutSquadron != null)
            {
                AiBalloonDefenseFlight enemyCoverUnit = new AiBalloonDefenseFlight();
                enemyCoverUnit.initialize(mission, campaign, balloonUnit.getPwcgGroundUnitInformation().getPosition(), enemyScoutSquadron, 
                                missionBeginUnitCover, false, balloonUnit);
                enemyCoverUnit.createUnitMission();
    
                balloonBust.addLinkedUnit(enemyCoverUnit);
            }
		}
    }

	protected Coordinate getTargetWaypoint(Mission mission, Coordinate referenceCoordinate, Side enemySide) throws PWCGMissionGenerationException 
	{
		Coordinate targetWaypoint = null;
		PositionsManager positionsManager = new PositionsManager(campaign.getDate());
        targetWaypoint = positionsManager.getClosestDefinitePosition(enemySide, referenceCoordinate.copy());
		return targetWaypoint;
	}

}
