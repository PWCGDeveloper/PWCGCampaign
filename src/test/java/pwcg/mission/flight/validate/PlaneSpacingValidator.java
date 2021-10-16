package pwcg.mission.flight.validate;

import java.util.List;

import org.junit.jupiter.api.Assertions;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.plane.PlaneMcu;

public class PlaneSpacingValidator
{
    
    public static void verifySpacing(List<PlaneMcu> planes)
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int horizontalSpacing = productSpecific.getFormationHorizontalSpacing() - 10;

        for (int i = 0; i < planes.size(); ++i)
        {
            PlaneMcu  thisPlane = planes.get(i);
            Assertions.assertTrue (thisPlane.getNumberInFormation() == (i+1));
            
            for (int j = 0; j < planes.size(); ++j)
            {
                if (i == j)
                {
                    continue;
                }
                
                PlaneMcu  thatPlane = planes.get(j);
                double distanceBetweenPlanes = MathUtils.calcDist(thisPlane.getPosition(), thatPlane.getPosition());
                Assertions.assertTrue (distanceBetweenPlanes > horizontalSpacing);
            }
        }
    }

}
