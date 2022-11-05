package pwcg.core.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
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
        AirfieldManager airfieldManager = PWCGContext.getInstance().getMap(FrontMapIdentifier.KUBAN_MAP).getAirfieldManager();
        Airfield referenceAirfield = airfieldManager.getAirfield("Kerch-2");
        Coordinate lookupLocation = MathUtils.calcNextCoord(FrontMapIdentifier.KUBAN_MAP, referenceAirfield.getPosition(), 90, 15000);
        Airfield airfield = positionFinder.selectPositionWithinExpandingRadius(airfieldManager.getAirFieldsForSide(
                FrontMapIdentifier.KUBAN_MAP, DateUtils.getDateYYYYMMDD("19420701"), Side.AXIS), lookupLocation, radius, maxDistance);
        assert(airfield != null);
    }
}
