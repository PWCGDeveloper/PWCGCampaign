package pwcg.mission.flight.waypoint.begin;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.TargetDefinition;

public class IngressBackoffUsingConfigsCalculator
{
    public static final int MINIMUM_BACKOFF = 5000;


    public static double calculateIngressDistanceFromTarget(FlightInformation flightBuildInformation, TargetDefinition targetDefinition) throws PWCGException
    {
        if (flightBuildInformation.isPlayerFlight())
        {
            return IngressBackoffUsingConfigsCalculator.calculateDistanbceToTargetFromConfigs();
        }
        else
        {
            return flightBuildInformation.getMission().getPlayerDistanceToPosition(targetDefinition.getPosition());
        }
    }

    public static int calculateDistanbceToTargetFromConfigs()
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int minDistanceToTarget = productSpecific.getIngressAtTargetMinDistance();
        int maxDistanceToTarget = productSpecific.getIngressAtTargetMaxDistance();
        int randomDistanceToTarget = RandomNumberGenerator.getRandom(maxDistanceToTarget - minDistanceToTarget);
        int distanceToTarget = minDistanceToTarget + randomDistanceToTarget;
        
        if (distanceToTarget < MINIMUM_BACKOFF)
        {
            distanceToTarget = MINIMUM_BACKOFF;
        }
        return distanceToTarget;
    }

}
