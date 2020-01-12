package pwcg.mission.target;

import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.locator.targettype.TargetTypeAttackGenerator;
import pwcg.campaign.target.locator.targettype.TargetTypeAvailabilityInputGenerator;
import pwcg.campaign.target.locator.targettype.TargetTypeAvailabilityInputs;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.locator.TargetLocatorAttack;

public class TargetDefinitionBuilderAirToGround implements ITargetDefinitionBuilder
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition = new TargetDefinition();

    public TargetDefinitionBuilderAirToGround (IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    @Override
    public TargetDefinition buildTargetDefinition () throws PWCGException
    {        
        TargetType targetType = determinePredefinedTacticalTarget(flightInformation.getFlightType());
        if (targetType == TargetType.TARGET_ANY)
        {
            Coordinate targetSearchStartLocation = flightInformation.getTargetSearchStartLocation();
            TargetTypeAvailabilityInputs targetTypeAvailabilityInputs = createTargetingInputs(targetSearchStartLocation);
            targetType = createTargetType(targetTypeAvailabilityInputs);
        }
        buildTargetDefinitionForTacticalFlight(targetType);          
        
        return targetDefinition;
    }

    @Override
    public TargetDefinition buildSpecificTargetDefinition(TargetType targetType) throws PWCGException
    {
        buildTargetDefinitionForTacticalFlight(targetType);          
        Coordinate targetLocation = flightInformation.getTargetPosition();
        targetDefinition.setTargetPosition(targetLocation);
        targetDefinition.setTargetOrientation(new Orientation());
        return targetDefinition;
    }

    private TargetType determinePredefinedTacticalTarget(FlightTypes flightType) 
    {
        if (flightType == FlightTypes.LOW_ALT_BOMB || flightType == FlightTypes.LOW_ALT_CAP || 
            flightType == FlightTypes.LOW_ALT_PATROL || flightType == FlightTypes.CONTACT_PATROL ||
            flightType == FlightTypes.PARATROOP_DROP)
        {
            return TargetType.TARGET_ASSAULT;
        }
        else if (flightType == FlightTypes.BALLOON_BUST || flightType == FlightTypes.BALLOON_DEFENSE)
        {
            return TargetType.TARGET_BALLOON;
        }
        else if (flightType == FlightTypes.ANTI_SHIPPING_BOMB || flightType == FlightTypes.ANTI_SHIPPING_ATTACK || flightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB)
        {
            return TargetType.TARGET_SHIPPING;
        }
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            return TargetType.TARGET_AAA;
        }
        else
        {
            return TargetType.TARGET_ANY;
        }
    }    

    private void buildTargetDefinitionForTacticalFlight (TargetType targetType) throws PWCGException
    {
        targetDefinition.setTargetType(targetType);
        targetDefinition.setAttackingSquadron(flightInformation.getSquadron());
        targetDefinition.setTargetName(TargetDefinitionBuilderUtils.buildTargetName(
                flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()), targetType));

        determineAttackingAndDefendingCountries(targetType);
        
        targetDefinition.setDate(flightInformation.getCampaign().getDate());
        targetDefinition.setPlayerTarget((Squadron.isPlayerSquadron(flightInformation.getCampaign(), flightInformation.getSquadron().getSquadronId())));
        
        TargetRadius targetRadius = new TargetRadius();
        targetRadius.calculateTargetRadius(flightInformation.getFlightType(), flightInformation.getMission().getMissionBorders().getAreaRadius());
        targetDefinition.setPreferredRadius(Double.valueOf(targetRadius.getInitialTargetRadius()).intValue());
        targetDefinition.setMaximumRadius(Double.valueOf(targetRadius.getMaxTargetRadius()).intValue());
        
        TargetLocatorAttack targetLocator = new TargetLocatorAttack(targetDefinition,flightInformation.getMission().getMissionBorders().getCenter());
        targetLocator.locateTarget();
        targetDefinition.setTargetPosition(targetLocator.getTargetLocation());
        targetDefinition.setTargetOrientation(targetLocator.getTargetOrientation());
    }

    private void determineAttackingAndDefendingCountries(TargetType targetType) throws PWCGException
    {
        targetDefinition.setAttackingCountry(flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()));
        targetDefinition.setTargetCountry(flightInformation.getSquadron().determineEnemyCountry(flightInformation.getCampaign(), flightInformation.getCampaign().getDate()));

        if (flightInformation.getFlightType() == FlightTypes.BALLOON_DEFENSE)
        {
            targetDefinition.setAttackingCountry(flightInformation.getSquadron().determineEnemyCountry(flightInformation.getCampaign(), flightInformation.getCampaign().getDate()));
            targetDefinition.setTargetCountry(flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()));
        }
        else if (targetType == TargetType.TARGET_ASSAULT)
        {
            TargetDefinitionBuilderUtils.chooseSides(flightInformation);
        }
        else
        {
            targetDefinition.setAttackingCountry(flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()));
            targetDefinition.setTargetCountry(flightInformation.getSquadron().determineEnemyCountry(flightInformation.getCampaign(), flightInformation.getCampaign().getDate()));
        }
    }

    private TargetTypeAvailabilityInputs createTargetingInputs(Coordinate targetSearchStartLocation) throws PWCGException
    {
        TargetTypeAvailabilityInputGenerator targetTypeAvailabilityInputGenerator = new TargetTypeAvailabilityInputGenerator();
        TargetTypeAvailabilityInputs targetTypeAvailabilityInputs = targetTypeAvailabilityInputGenerator.createTargetAvailabilityInputs(flightInformation);
        return targetTypeAvailabilityInputs;
    }

    private TargetType createTargetType(TargetTypeAvailabilityInputs targetTypeAvailabilityInputs) throws PWCGException
    {
        TargetTypeAttackGenerator targetTypeGenerator = new TargetTypeAttackGenerator(targetTypeAvailabilityInputs);
        TargetType targetType = targetTypeGenerator.createTargetType();
        return targetType;
    }
}
