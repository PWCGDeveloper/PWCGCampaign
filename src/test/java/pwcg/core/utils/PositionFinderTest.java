package pwcg.core.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightTypes;

@ExtendWith(MockitoExtension.class)
public class PositionFinderTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PositionFinder<Airfield> positionFinder = new PositionFinder<Airfield>();
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        double radius = productSpecific.getAdditionalInitialTargetRadius(FlightTypes.GROUND_ATTACK);
        double maxDistance = productSpecific.getAdditionalMaxTargetRadius(FlightTypes.GROUND_ATTACK);
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        Airfield airfield = positionFinder.selectPositionWithinExpandingRadius(airfieldManager.getAirFieldsForSide(DateUtils.getDateYYYYMMDD("19420701"), Side.AXIS), new Coordinate(10000, 0, 10000), radius, maxDistance);
        assert(airfield != null);
    }
}
