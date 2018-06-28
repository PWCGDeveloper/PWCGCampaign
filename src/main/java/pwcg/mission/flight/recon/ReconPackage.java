package pwcg.mission.flight.recon;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.GeneralTargetLocationGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.recon.ReconFlight.ReconFlightTypes;

public class ReconPackage extends FlightPackage
{
    public ReconPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.RECON;
    }

    public Flight createPackage () throws PWCGException 
    {
        // Now the actual mission
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());

        ReconFlight recon = null;
        
        if (isPlayerFlight)
        {
            PlayerReconFlight playerRecon = new  PlayerReconFlight();
            recon = playerRecon;
        }
        else
        {
            recon = new ReconFlight ();
        }

        ReconFlightTypes reconFlightType = getReconFlightType();
        recon.setReconFlightType(reconFlightType);

        // Initial target coordinates will be something near the target area
        Coordinate targetGeneralLocation = GeneralTargetLocationGenerator.createTargetGeneralLocation(campaign, mission, squadron);
        Coordinate initialTargetCoordinates = getInitialTargetCoordinates(recon, squadron, targetGeneralLocation);
        recon.initialize(mission, campaign, initialTargetCoordinates, squadron, missionBeginUnit, isPlayerFlight);

        recon.createUnitMission();

        addPossibleEscort(recon);        
        
        return recon;
    }

    private Coordinate getInitialTargetCoordinates(ReconFlight flight, Squadron squadron, Coordinate targetGeneralLocation) throws PWCGException 
    {
        Coordinate targetCoordinates = null;

        // For transports we put in trucks and trains
        if (flight.getReconFlightType() == ReconFlightTypes.RECON_FLIGHT_TRANSPORT)
        {
            return getTargetTransportCoordinates(squadron, targetGeneralLocation);
        }
        // For airfields there might be a scramble
        else if (flight.getReconFlightType() == ReconFlightTypes.RECON_FLIGHT_AIRFIELD)
        {
            return getTargetAirfieldCoordinates(squadron, targetGeneralLocation);
        }
        else
        {
            targetCoordinates = getFrontPosition(squadron, targetGeneralLocation);
        }

        return targetCoordinates;
    }

    private Coordinate getFrontPosition(Squadron squadron, Coordinate targetGeneralLocation) throws PWCGException 
    {
        Side enemySide = squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide();
        ICountry enemyCountry = CountryFactory.makeMapReferenceCountry(enemySide);

        FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate frontCoordinates = frontLinesForMap.findClosestFrontCoordinateForSide(targetGeneralLocation, enemyCountry.getSide());

        return frontCoordinates;
    }

    private Coordinate getTargetAirfieldCoordinates(Squadron squadron, Coordinate targetGeneralLocation) throws PWCGException 
    {
        Side enemySide = squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide();

        List <IAirfield> enemyAirfields = new ArrayList<IAirfield>();
        double maxRadius = 20000.0;

        Coordinate nearbyPosition = getFrontPosition(squadron, targetGeneralLocation);
        while (enemyAirfields.size() <= 0)
        {
            enemyAirfields = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().getAirfieldsWithinRadiusBySide(nearbyPosition, campaign.getDate(), maxRadius, enemySide);

            maxRadius += 10000.0;
        }

        return enemyAirfields.get(0).getPosition();
    }

    private Coordinate getTargetTransportCoordinates(Squadron squadron, Coordinate targetGeneralLocation) throws PWCGException 
    {
        Side enemySide = squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide();
        ICountry enemyCountry = CountryFactory.makeMapReferenceCountry(enemySide);

        int maxGroundTargetDistance = 50000;
        GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();

        Bridge startBridge = null;
        while (startBridge == null)
        {
            startBridge = groupData.getBridgeFinder().findBridgeForSideWithinRadius(enemyCountry.getSide(), campaign.getDate(), targetGeneralLocation, maxGroundTargetDistance);
            
            maxGroundTargetDistance += 10000.0;
        }

        return startBridge.getPosition();
    }

    private ReconFlightTypes getReconFlightType()
    {
        ReconFlightTypes reconFlightType = ReconFlightTypes.RECON_FLIGHT_FRONT;

        int roll = RandomNumberGenerator.getRandom(100);

        if (roll < 50)
        {
            reconFlightType = ReconFlightTypes.RECON_FLIGHT_FRONT;
        }
        else if (roll < 85)
        {
            reconFlightType = ReconFlightTypes.RECON_FLIGHT_TRANSPORT;
        }
        else 
        {
            reconFlightType = ReconFlightTypes.RECON_FLIGHT_AIRFIELD;
        }

        return reconFlightType;
    }
}
