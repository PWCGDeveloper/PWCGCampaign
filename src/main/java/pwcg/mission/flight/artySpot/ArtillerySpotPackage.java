package pwcg.mission.flight.artySpot;

import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.unittypes.GroundUnit;

public class ArtillerySpotPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public ArtillerySpotPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
    {
        Flight artySpot = createFlight();
		return artySpot;
	}

    private Flight createFlight() throws PWCGException
    {        
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());
		Flight artySpot = null;
		if (flightInformation.isPlayerFlight())
		{
		    artySpot = createPlayerFlight(groundUnitCollection, targetCoordinates, startCoords);
		}
		else
		{
            artySpot = createAiFlight(targetCoordinates, startCoords);
		}

        artySpot.linkGroundUnitsToFlight(groundUnitCollection);
        return artySpot;
    }

    private GroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

    private Flight createPlayerFlight(GroundUnitCollection groundUnits, Coordinate targetCoordinates, Coordinate startCoords) throws PWCGException
    {
        Flight artySpot;
        Side squadronSide = flightInformation.getSquadron().determineSide();
        ArtillerySpotArtilleryGroup friendlyArtillery = getFriendlyArtillery(groundUnits, squadronSide);
        
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
        
        PlayerArtillerySpotFlight artySpotPlayer = new PlayerArtillerySpotFlight (flightInformation, missionBeginUnit);
        artySpotPlayer.createUnitMission();
        artySpotPlayer.createArtyGrid(friendlyArtillery);
        
        artySpot = artySpotPlayer;
        return artySpot;
    }

    private Flight createAiFlight(Coordinate targetCoordinates, Coordinate startCoords) throws PWCGException
    {
        Flight artySpot;
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
        
        ArtillerySpotFlight artySpotAI = new ArtillerySpotFlight (flightInformation, missionBeginUnit);
        artySpotAI.createUnitMission();
        artySpot = artySpotAI;
        return artySpot;
    }

    private ArtillerySpotArtilleryGroup getFriendlyArtillery(GroundUnitCollection groundUnits, Side campaignSide) throws PWCGException
    {
        List<GroundUnit> allGroundUnits = groundUnits.getAllGroundUnits();
        for (GroundUnit groundUnit : allGroundUnits)
        {
            if (groundUnit instanceof ArtillerySpotArtilleryGroup)
            {
                return (ArtillerySpotArtilleryGroup)groundUnit;
            }
        }

        throw new PWCGException("Unable to find friendly artillery unit for artillery spot");
    }
}
