 package pwcg.mission.flight.balloondefense;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.GroundUnitType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.balloonBust.BalloonBustFlight;
import pwcg.mission.ground.GroundUnitCollection;

public class BalloonDefensePackage extends FlightPackage
{
    public BalloonDefensePackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
	{
		super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.BALLOON_DEFENSE;
	}
	
	public Flight createPackage () throws PWCGException 
	{
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        GroundUnitCollection groundUnits = createGroundUnitsForFlight();
        Flight balloonDefenseFlight = createFlight(startCoords, groundUnits);
		return balloonDefenseFlight;
	}

    private Flight createFlight(Coordinate startCoords, GroundUnitCollection groundUnits) throws PWCGException
    {
        BalloonDefenseGroup balloonUnit = createBalloonUnit(groundUnits);

		// Create a balloon cover flight
		Flight balloonDefenseFlight = createBalloonDefenseFlight(startCoords, balloonUnit, groundUnits);
		balloonDefenseFlight.linkGroundUnitsToFlight(groundUnits);

        addEnemyScoutSquadronToAttackBaloon(balloonUnit, balloonDefenseFlight);

		balloonDefenseFlight.createUnitMission();
        return balloonDefenseFlight;
    }

    private BalloonDefenseGroup createBalloonUnit(GroundUnitCollection groundUnits) throws PWCGException
    {
        Side campaignSide = campaign.determineCountry().getSide();
        BalloonDefenseGroup balloonUnit = (BalloonDefenseGroup)groundUnits.getGroundUnitByType(GroundUnitType.BALLOON_UNIT, campaignSide);
        return balloonUnit;
    }

    private Flight createBalloonDefenseFlight(Coordinate startCoords, BalloonDefenseGroup balloonUnit, GroundUnitCollection groundUnits) throws PWCGException
    {
        Flight balloonDefenseFlight = null;
		if(isPlayerFlight)
		{
	        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
	        missionBeginUnit.initialize(startCoords.copy());
	        
	        FlightInformation flightInformation = createFlightInformation(balloonUnit.getPwcgGroundUnitInformation().getPosition());
            PlayerBalloonDefenseFlight playerCoverUnit = new PlayerBalloonDefenseFlight(flightInformation, missionBeginUnit, balloonUnit);          
			balloonDefenseFlight = playerCoverUnit;
		}
		else
		{
            MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
            missionBeginUnit.initialize(startCoords.copy());
            
            FlightInformation flightInformation = createFlightInformation(balloonUnit.getPwcgGroundUnitInformation().getPosition());
			AiBalloonDefenseFlight nonPlayerCoverUnit = new AiBalloonDefenseFlight(flightInformation, missionBeginUnit, balloonUnit);
			balloonDefenseFlight = nonPlayerCoverUnit;
		}

        return balloonDefenseFlight;
    }

    private void addEnemyScoutSquadronToAttackBaloon(BalloonDefenseGroup balloonUnit, Flight balloonDefenseFlight) throws PWCGException
    {
        if(isPlayerFlight)
        {    
            Squadron enemyScoutSquadron = PWCGContextManager.getInstance().getSquadronManager().getEnemySquadronByRole(squadron.determineSquadronCountry(campaign.getDate()), Role.ROLE_FIGHTER, campaign.getDate());
            if (enemyScoutSquadron != null)
            {
                MissionBeginUnit missionBeginUnitBust = new MissionBeginUnit();
                missionBeginUnitBust.initialize(balloonUnit.getPwcgGroundUnitInformation().getPosition().copy());
    
                FlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(enemyScoutSquadron, mission, FlightTypes.BALLOON_BUST, balloonUnit.getPwcgGroundUnitInformation().getPosition());
                BalloonBustFlight enemyBustFlight = new BalloonBustFlight(opposingFlightInformation, missionBeginUnitBust);
                enemyBustFlight.createUnitMission();       
                balloonDefenseFlight.addLinkedUnit(enemyBustFlight);
            }
        }
    }

}
