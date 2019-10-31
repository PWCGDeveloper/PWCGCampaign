package pwcg.campaign.target;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.locator.TargetLocatorAir;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.intercept.InterceptPlayerCoordinateGenerator;

public class TargetDefinitionBuilderAirToAir implements ITargetDefinitionBuilder
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition = new TargetDefinition();

    public TargetDefinitionBuilderAirToAir (FlightInformation flightInformation)
    {
    	this.flightInformation = flightInformation;
    }

    public TargetDefinition buildTargetDefinition () throws PWCGException
    {
        createBasicTargetDefinition();
        createTargetRadius();        
        createTargetLocation();
        return targetDefinition;
    }

    private void createTargetRadius() throws PWCGException
    {
        TargetRadius targetRadius = new TargetRadius();
        targetRadius.calculateTargetRadius(flightInformation.getFlightType(), flightInformation.getMission().getMissionBorders().getAreaRadius());
        targetDefinition.setPreferredRadius(new Double(targetRadius.getInitialTargetRadius()).intValue());
        targetDefinition.setMaximumRadius(new Double(targetRadius.getMaxTargetRadius()).intValue());
    }

    private void createBasicTargetDefinition() throws PWCGException
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_AIR);
        targetDefinition.setAttackingSquadron(flightInformation.getSquadron());
        targetDefinition.setTargetName(buildTargetName(flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()), TacticalTarget.TARGET_AIR));

        targetDefinition.setAttackingCountry(flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()));
        targetDefinition.setTargetCountry(flightInformation.getSquadron().determineEnemyCountry(flightInformation.getCampaign(), flightInformation.getCampaign().getDate()));
        
        targetDefinition.setDate(flightInformation.getCampaign().getDate());
        targetDefinition.setPlayerTarget((Squadron.isPlayerSquadron(flightInformation.getCampaign(), flightInformation.getSquadron().getSquadronId())));
    }

    private void createTargetLocation() throws PWCGException
    {
        Coordinate targetLocation = getTargetLocationForFlightType();
        targetDefinition.setTargetPosition(targetLocation);
        targetDefinition.setTargetOrientation(new Orientation());
        
        if (flightInformation.getFlightType() == FlightTypes.INTERCEPT || 
            flightInformation.getFlightType() == FlightTypes.HOME_DEFENSE || 
            flightInformation.getFlightType() == FlightTypes.SCRAMBLE)
        {
            buildLinkedFlightTagetDefinition();
        }
    }

    private void buildLinkedFlightTagetDefinition() throws PWCGException
    {
        InterceptPlayerCoordinateGenerator coordinateGenerator = new InterceptPlayerCoordinateGenerator(flightInformation);
        TargetDefinition linkedFlightTargetDefinition = coordinateGenerator.createTargetCoordinates();
        targetDefinition.setLinkedFlightTargetDefinition(linkedFlightTargetDefinition);
    }

    private Coordinate getTargetLocationForFlightType() throws PWCGException
    {
        TargetLocatorAir targetLocatorAir = new TargetLocatorAir(flightInformation);
        
        if (flightInformation.getFlightType() == FlightTypes.PATROL || flightInformation.getFlightType() == FlightTypes.LONE_WOLF)
        {
            return targetLocatorAir.getFrontCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.INTERCEPT || flightInformation.getFlightType() == FlightTypes.HOME_DEFENSE ||
                 flightInformation.getFlightType() == FlightTypes.RECON)
        {
            return targetLocatorAir.getInterceptCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.OFFENSIVE || flightInformation.getFlightType() == FlightTypes.SPY_EXTRACT)
        {
            return targetLocatorAir.getEnemyTerritoryPatrolCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.TRANSPORT)
        {
            return targetLocatorAir.getTransportAirfieldCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.FERRY)
        {
            return targetLocatorAir.getFerryAirfieldCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.SEA_PATROL)
        {
            return targetLocatorAir.getSeaLaneCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.SCRAMBLE)
        {
            return targetLocatorAir.getScrambleCoordinate();
        }
        else if (flightInformation.getFlightType() == FlightTypes.ESCORT)
        {
            if (flightInformation.isPlayerFlight())
            {
                return targetLocatorAir.getPlayerEscortRendezvousCoordinate();
            }
            else
            {
                return targetLocatorAir.getEscortForPlayerRendezvousCoordinate();
            }
        }

        throw new PWCGException("No target locations for flight type " + flightInformation.getFlightType());
    }

    private String buildTargetName(ICountry targetCountry, TacticalTarget targetType)
    {
        String nationality = targetCountry.getNationality();
        String name = nationality + " " + targetType.getTargetName();
        return name;
    }

}
