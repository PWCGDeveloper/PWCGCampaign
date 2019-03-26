package pwcg.mission.flight.attackhunt;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.GeneralTargetLocationGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;

public class GroundAttackHuntPackage extends FlightPackage
{
    public GroundAttackHuntPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.PATROL;
    }

    public Flight createPackage () throws PWCGException 
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());

        Coordinate targetGeneralLocation = GeneralTargetLocationGenerator.createTargetGeneralLocation(campaign, mission, squadron);
        Coordinate initialTargetCoordinates = getFrontPosition(squadron, targetGeneralLocation);

        FlightInformation flightInformation = createFlightInformation(initialTargetCoordinates);
        GroundAttackHuntFlight groundAttackHunt = new GroundAttackHuntFlight (flightInformation, missionBeginUnit);
        groundAttackHunt.createUnitMission();
        
        addPossibleEscort(groundAttackHunt);        
        
        return groundAttackHunt;
    }

    private Coordinate getFrontPosition(Squadron squadron, Coordinate approximatePosition) throws PWCGException 
    {
        Side enemySide = squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide();
        ICountry enemyCountry = CountryFactory.makeMapReferenceCountry(enemySide);

        FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate frontCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(approximatePosition, enemyCountry.getSide());

        return frontCoordinates;
    }

}
