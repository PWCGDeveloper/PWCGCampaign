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
        targetDefinition.setTargetName(targetType.getTargetName());

        targetDefinition.setAttackingCountry(squadron.determineSquadronCountry(campaign.getDate()));
        targetDefinition.setTargetCountry(squadron.determineEnemyCountry(campaign.getDate()));
        targetDefinition.setDate(campaign.getDate());
        targetDefinition.setPlayerTarget((campaign.getSquadronId() == squadron.getSquadronId()));
        
        targetDefinition.setPreferredRadius(productSpecific.getInitialTargetRadiusFromGeneralTargetLocation(flightType));
        targetDefinition.setMaximumRadius(productSpecific.getMaxTargetRadiusFromGeneralTargetLocation(flightType));

        targetDefinition.setTargetGeneralLocation(targetGeneralLocation);
        
        TargetLocator targetLocator = new TargetLocator(targetDefinition);
        targetLocator.locateTarget();
        targetDefinition.setTargetLocation(targetLocator.getTargetLocation());
        targetDefinition.setTargetOrientation(targetLocator.getTargetOrientation());

        return targetDefinition;
    }

    public TargetDefinition buildTargetDefinitionForStrategicFlight (
            Campaign campaign, 
            Squadron squadron, 
            FlightTypes flightType, 
            Coordinate targetGeneralLocation) throws PWCGException
    {
                
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        ICountry targetCountry = squadron.determineEnemyCountry(campaign.getDate());
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
        targetDefinition.setTargetName(targetType.getTargetName());

        targetDefinition.setAttackingCountry(squadron.determineSquadronCountry(campaign.getDate()));
        targetDefinition.setTargetCountry(targetCountry);
        targetDefinition.setDate(campaign.getDate());
        targetDefinition.setPlayerTarget((campaign.getSquadronId() == squadron.getSquadronId()));
        
        targetDefinition.setPreferredRadius(productSpecific.getInitialTargetRadiusFromGeneralTargetLocation(flightType));
        targetDefinition.setMaximumRadius(productSpecific.getMaxTargetRadiusFromGeneralTargetLocation(flightType));

        targetDefinition.setTargetGeneralLocation(targetGeneralLocation);
        targetDefinition.setTargetLocation(place.getPosition());
        targetDefinition.setTargetOrientation(new Orientation());

        return targetDefinition;
    }

    public TargetDefinition buildTargetDefinitionForAmbient (
            Campaign campaign, 
            ICountry attackingCountry, 
            ICountry targetCountry, 
            TacticalTarget targetType, 
            Coordinate targetPosition) throws PWCGException
    {
        targetDefinition.setTargetType(targetType);
        targetDefinition.setTargetCategory(targetType.getTargetCategory());
        targetDefinition.setTargetName(targetType.getTargetName());

        targetDefinition.setAttackingCountry(attackingCountry);
        targetDefinition.setTargetCountry(targetCountry);
        targetDefinition.setDate(campaign.getDate());
        targetDefinition.setPlayerTarget(false);
        
        targetDefinition.setPreferredRadius(5000);
        targetDefinition.setMaximumRadius(10000);

        targetDefinition.setTargetGeneralLocation(targetPosition);
        targetDefinition.setTargetLocation(targetPosition);
        targetDefinition.setTargetOrientation(new Orientation());

        return targetDefinition;
    }
}
