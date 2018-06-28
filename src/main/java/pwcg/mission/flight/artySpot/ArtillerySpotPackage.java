package pwcg.mission.flight.artySpot;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
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
        GroundUnitCollection groundUnits = createGroundUnitsForFlight();
        Flight artySpot = createFlight(groundUnits);
        addPossibleEscort(artySpot);        
		return artySpot;
	}

    private Flight createFlight(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide(campaign.getDate()));
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        
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
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        Side campaignSide = campaign.determineCountry().getSide();
        ArtillerySpotArtilleryGroup friendlyArtillery = getFriendlyArtillery(groundUnits, campaignSide);
        
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
        
        PlayerArtillerySpotFlight artySpotPlayer = new PlayerArtillerySpotFlight ();
        artySpotPlayer.initialize(mission, campaign, targetCoordinates, squadron, missionBeginUnit);
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
        
        ArtillerySpotFlight artySpotAI = new ArtillerySpotFlight ();
        artySpotAI.initialize(mission, campaign, targetCoordinates, squadron, missionBeginUnit, false);
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
