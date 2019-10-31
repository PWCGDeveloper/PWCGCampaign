package pwcg.campaign.target;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.mission.flight.FlightTypes;

public class TargetRadius
{
    private double initialTargetRadius = 10000.0;
    private double maxTargetRadius = 20000.0;
    
    public void calculateTargetRadius(FlightTypes flightType, double missionBoxRadius)
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        double productRadiusInitialAdditional = productSpecific.getAdditionalInitialTargetRadius(flightType);
        initialTargetRadius = missionBoxRadius + productRadiusInitialAdditional;
        
        double productRadiusMaxAdditional = productSpecific.getAdditionalMaxTargetRadius(flightType);
        maxTargetRadius = missionBoxRadius + productRadiusMaxAdditional;
    }

    public double getInitialTargetRadius()
    {
        return initialTargetRadius;
    }

    public double getMaxTargetRadius()
    {
        return maxTargetRadius;
    }
}
