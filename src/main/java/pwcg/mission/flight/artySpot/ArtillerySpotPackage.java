package pwcg.mission.flight.artySpot;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.unittypes.GroundUnit;

public class ArtillerySpotPackage extends FlightPackage
{
    public ArtillerySpotPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.ARTILLERY_SPOT;
    }

    public Flight createPackage () throws PWCGException 
    {
        Flight artySpot = createFlight();
        addPossibleEscort(artySpot);        
		return artySpot;
	}

    private Flight createFlight() throws PWCGException
    {        
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide());
		Flight artySpot = null;
		if (isPlayerFlight)
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

    private Flight createPlayerFlight(GroundUnitCollection groundUnits, Coordinate targetCoordinates, Coordinate startCoords) throws PWCGException
    {
        Flight artySpot;
        Side squadronSide = squadron.determineSide();
        ArtillerySpotArtilleryGroup friendlyArtillery = getFriendlyArtillery(groundUnits, squadronSide);
        
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
        
        FlightInformation flightInformation = createFlightInformation(targetCoordinates);
        PlayerArtillerySpotFlight artySpotPlayer = new PlayerArtillerySpotFlight (flightInformation, missionBeginUnit);
        artySpotPlayer.createUnitMission();
        artySpotPlayer.createArtyGrid(friendlyArtillery);
        
        artySpot = artySpotPlayer;
        return artySpot;
    }

    private Flight createAiFlight(Coordinate targetCoordinates, Coordinate startCoords) throws PWCGException
    {
        Flight artySpot;
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
        
        FlightInformation flightInformation = createFlightInformation(targetCoordinates);
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
