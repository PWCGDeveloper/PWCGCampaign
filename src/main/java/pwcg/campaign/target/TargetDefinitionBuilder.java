package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;

public class TargetDefinitionBuilder
{
    private TargetDefinition targetDefinition = new TargetDefinition();

    public TargetDefinition buildTargetDefinitionForTacticalFlight (
            Campaign campaign, 
            Squadron squadron, 
            FlightTypes flightType, 
            TacticalTarget targetType, 
            Coordinate targetGeneralLocation) throws PWCGException
    {
                
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        targetDefinition.setTargetType(targetType);
        targetDefinition.setTargetCategory(targetType.getTargetCategory());
        targetDefinition.setTargetName(buildTargetName(campaign.determineCountry(), targetType));

        if (flightType == FlightTypes.BALLOON_DEFENSE)
        {
            targetDefinition.setAttackingCountry(squadron.determineEnemyCountry(campaign, campaign.getDate()));
            targetDefinition.setTargetCountry(squadron.determineSquadronCountry(campaign.getDate()));
        }
        else if (targetType == TacticalTarget.TARGET_ASSAULT)
        {
            chooseSides(campaign, squadron);
        }
        else
        {
            targetDefinition.setAttackingCountry(squadron.determineSquadronCountry(campaign.getDate()));
            targetDefinition.setTargetCountry(squadron.determineEnemyCountry(campaign, campaign.getDate()));
        }
        
        targetDefinition.setDate(campaign.getDate());
        targetDefinition.setPlayerTarget((campaign.getSquadronId() == squadron.getSquadronId()));
        
        targetDefinition.setPreferredRadius(productSpecific.getInitialTargetRadiusFromGeneralTargetLocation(flightType));
        targetDefinition.setMaximumRadius(productSpecific.getMaxTargetRadiusFromGeneralTargetLocation(flightType));

        targetDefinition.setTargetGeneralPosition(targetGeneralLocation);
        
        TargetLocator targetLocator = new TargetLocator(targetDefinition);
        targetLocator.locateTarget();
        targetDefinition.setTargetPosition(targetLocator.getTargetLocation());
        targetDefinition.setTargetOrientation(targetLocator.getTargetOrientation());

        return targetDefinition;
    }
    
    private void chooseSides(Campaign campaign,Squadron squadron) throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 60)
        {
            targetDefinition.setAttackingCountry(squadron.determineEnemyCountry(campaign, campaign.getDate()));
            targetDefinition.setTargetCountry(squadron.determineSquadronCountry(campaign.getDate()));
        }
        else
        {
            targetDefinition.setTargetCountry(squadron.determineEnemyCountry(campaign, campaign.getDate()));
            targetDefinition.setAttackingCountry(squadron.determineSquadronCountry(campaign.getDate()));
        }
    }

    public TargetDefinition buildTargetDefinitionForStrategicFlight (
            Campaign campaign, 
            Squadron squadron, 
            FlightTypes flightType, 
            Coordinate targetGeneralLocation) throws PWCGException
    {
                
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        ICountry targetCountry = squadron.determineEnemyCountry(campaign, campaign.getDate());
        StrategicTargetTypeGenerator strategicTargetTypeGenerator = new StrategicTargetTypeGenerator(targetCountry.getSide(), campaign.getDate(), targetGeneralLocation);
        TacticalTarget targetType = strategicTargetTypeGenerator.createTargetType();

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int strategicBombingRadius = productSpecificConfiguration.getInitialTargetRadiusFromGeneralTargetLocation(FlightTypes.STRATEGIC_BOMB);

        StrategicTargetLocator strategicTargetLocator = new StrategicTargetLocator(
                strategicBombingRadius, 
                targetCountry.getSide(), 
                campaign.getDate(), 
                targetGeneralLocation);
        IFixedPosition place = strategicTargetLocator.getStrategicTargetLocation(targetType);

        targetDefinition.setTargetType(targetType);
        targetDefinition.setTargetCategory(targetType.getTargetCategory());
        targetDefinition.setTargetName(buildTargetName(targetCountry, targetType));

        targetDefinition.setAttackingCountry(squadron.determineSquadronCountry(campaign.getDate()));
        targetDefinition.setTargetCountry(targetCountry);
        targetDefinition.setDate(campaign.getDate());
        targetDefinition.setPlayerTarget((campaign.getSquadronId() == squadron.getSquadronId()));
        
        targetDefinition.setPreferredRadius(productSpecific.getInitialTargetRadiusFromGeneralTargetLocation(flightType));
        targetDefinition.setMaximumRadius(productSpecific.getMaxTargetRadiusFromGeneralTargetLocation(flightType));

        targetDefinition.setTargetGeneralPosition(targetGeneralLocation);
        targetDefinition.setTargetPosition(place.getPosition());
        targetDefinition.setTargetOrientation(new Orientation());

        return targetDefinition;
    }

    public TargetDefinition buildTargetDefinitionAssault (
            Campaign campaign, 
            ICountry attackingCountry, 
            ICountry targetCountry, 
            TacticalTarget targetType, 
            Coordinate targetPosition,
            boolean isPlayerTarget) throws PWCGException
    {
        targetDefinition.setTargetType(targetType);
        targetDefinition.setTargetCategory(targetType.getTargetCategory());
        targetDefinition.setTargetName(buildTargetName(targetCountry, targetType));

        targetDefinition.setAttackingCountry(attackingCountry);
        targetDefinition.setTargetCountry(targetCountry);
        targetDefinition.setDate(campaign.getDate());
        
        targetDefinition.setPreferredRadius(5000);
        targetDefinition.setMaximumRadius(10000);

        targetDefinition.setTargetGeneralPosition(targetPosition);
        targetDefinition.setTargetPosition(targetPosition);
        targetDefinition.setTargetOrientation(new Orientation());
        targetDefinition.setPlayerTarget(isPlayerTarget);

        return targetDefinition;
    }

    public TargetDefinition buildTargetDefinitionNoFlight (
            Campaign campaign, 
            ICountry targetCountry, 
            TacticalTarget targetType, 
            Coordinate targetPosition,
            boolean isPlayerTarget) throws PWCGException
    {
        targetDefinition.setTargetType(targetType);
        targetDefinition.setTargetCategory(targetType.getTargetCategory());
        targetDefinition.setTargetName(buildTargetName(targetCountry, targetType));

        targetDefinition.setAttackingCountry(targetCountry.getOppositeSideCountry());
        targetDefinition.setTargetCountry(targetCountry);
        targetDefinition.setDate(campaign.getDate());
        
        targetDefinition.setPreferredRadius(5000);
        targetDefinition.setMaximumRadius(10000);

        targetDefinition.setTargetGeneralPosition(targetPosition);
        targetDefinition.setTargetPosition(targetPosition);
        targetDefinition.setTargetOrientation(new Orientation());
        targetDefinition.setPlayerTarget(isPlayerTarget);

        return targetDefinition;
    }

    private String buildTargetName(ICountry targetCountry, TacticalTarget targetType)
    {
        String nationality = targetCountry.getNationality();
        String name = nationality + " " + targetType.getTargetCategory();
        return name;
    }

}
