package pwcg.campaign.target.locator.targettype;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightTypes;

public class TargetTypeAvailabilityInputGenerator
{
    private TargetTypeAvailabilityInputs targetTypeAvailabilityInputs = new TargetTypeAvailabilityInputs();

    public TargetTypeAvailabilityInputs createTargetAvailabilityInputs(Campaign campaign, FlightTypes flightType, Side side, Coordinate targetGeneralLocation) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        targetTypeAvailabilityInputs.setPreferredDistance(productSpecific.getInitialTargetRadiusFromGeneralTargetLocation(flightType));
        targetTypeAvailabilityInputs.setMaxDistance(productSpecific.getMaxTargetRadiusFromGeneralTargetLocation(flightType));
        targetTypeAvailabilityInputs.setDate(campaign.getDate());
        targetTypeAvailabilityInputs.setTargetGeneralLocation(targetGeneralLocation);
        targetTypeAvailabilityInputs.setSide(side);
        
        return targetTypeAvailabilityInputs;
    }
}
