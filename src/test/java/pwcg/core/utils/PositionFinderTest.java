package pwcg.core.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightTypes;

@RunWith(MockitoJUnitRunner.class)
public class PositionFinderTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PositionFinder<IAirfield> positionFinder = new PositionFinder<IAirfield>();
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        double radius = productSpecific.getAdditionalInitialTargetRadius(FlightTypes.GROUND_ATTACK);
        double maxDistance = productSpecific.getAdditionalMaxTargetRadius(FlightTypes.GROUND_ATTACK);
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        IAirfield airfield = positionFinder.selectPositionWithinExpandingRadius(airfieldManager.getAirFieldsForSide(DateUtils.getDateYYYYMMDD("19420701"), Side.AXIS), new Coordinate(10000, 0, 10000), radius, maxDistance);
        assert(airfield != null);
    }
}
