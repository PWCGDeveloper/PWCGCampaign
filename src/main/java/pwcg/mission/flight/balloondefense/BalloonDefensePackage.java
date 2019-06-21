 package pwcg.mission.flight.balloondefense;

import pwcg.campaign.api.Side;
import pwcg.campaign.target.unit.GroundUnitType;
import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitCollection;

public class BalloonDefensePackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public BalloonDefensePackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

	public Flight createPackage () throws PWCGException 
	{
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        GroundUnitCollection groundUnits = createGroundUnitsForFlight();
        Flight balloonDefenseFlight = createFlight(startCoords, groundUnits);
		return balloonDefenseFlight;
	}

    private Flight createFlight(Coordinate startCoords, GroundUnitCollection groundUnits) throws PWCGException
    {
        BalloonDefenseGroup balloonUnit = createBalloonUnit(groundUnits);
		Flight balloonDefenseFlight = createBalloonDefenseFlight(startCoords, balloonUnit, groundUnits);
		balloonDefenseFlight.linkGroundUnitsToFlight(groundUnits);
		balloonDefenseFlight.createUnitMission();
        return balloonDefenseFlight;
    }

    private BalloonDefenseGroup createBalloonUnit(GroundUnitCollection groundUnits) throws PWCGException
    {
        Side campaignSide = flightInformation.getSquadron().determineSide();
        BalloonDefenseGroup balloonUnit = (BalloonDefenseGroup)groundUnits.getGroundUnitByType(GroundUnitType.BALLOON_UNIT, campaignSide);
        return balloonUnit;
    }

    private Flight createBalloonDefenseFlight(Coordinate startCoords, BalloonDefenseGroup balloonUnit, GroundUnitCollection groundUnits) throws PWCGException
    {
        Flight balloonDefenseFlight = null;
		if(flightInformation.isPlayerFlight())
		{
	        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
            PlayerBalloonDefenseFlight playerCoverUnit = new PlayerBalloonDefenseFlight(flightInformation, missionBeginUnit, balloonUnit);          
			balloonDefenseFlight = playerCoverUnit;
		}
		else
		{
	        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
			AiBalloonDefenseFlight nonPlayerCoverUnit = new AiBalloonDefenseFlight(flightInformation, missionBeginUnit, balloonUnit);
			balloonDefenseFlight = nonPlayerCoverUnit;
		}

        return balloonDefenseFlight;
    }

    private GroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

}
