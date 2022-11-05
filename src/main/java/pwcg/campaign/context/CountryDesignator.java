package pwcg.campaign.context;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class CountryDesignator
{
    
    public static ICountry determineCountry(FrontMapIdentifier mapIdentifier, Coordinate objectCoordinate, Date date) throws PWCGException
    {
        ICountry country = CountryFactory.makeNeutralCountry();
        
        FrontLinesForMap frontLines = PWCGContext.getInstance().getMap(mapIdentifier).getFrontLinesForMap(date);
        
        Coordinate closestAllied = frontLines.findClosestFrontCoordinateForSide(objectCoordinate, Side.ALLIED);
        Coordinate closestAxis = frontLines.findClosestFrontCoordinateForSide(objectCoordinate, Side.AXIS);
        
        double distanceToAllied = MathUtils.calcDist(objectCoordinate, closestAllied);
        double distanceToAxis = MathUtils.calcDist(objectCoordinate, closestAxis);
        
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int neutralZone = productSpecific.geNeutralZone();

        if (distanceToAllied > neutralZone && distanceToAxis > neutralZone)
        {
            if (distanceToAxis < distanceToAllied)
            {
                country = CountryFactory.makeMapReferenceCountry(mapIdentifier, Side.AXIS);
            }
            else
            {
                country = CountryFactory.makeMapReferenceCountry(mapIdentifier, Side.ALLIED);
            }
        }

        return country;
    }
}
