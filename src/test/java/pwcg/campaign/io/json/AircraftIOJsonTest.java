package pwcg.campaign.io.json;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class AircraftIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Map<String, PlaneType> aircraft = AircraftIOJson.readJson();
        validate(aircraft);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Map<String, PlaneType> aircraft = AircraftIOJson.readJson();
        validate(aircraft);
    }

    private void validate(Map<String, PlaneType> aircraft)
    {
        assert (aircraft.size() > 0);
        for (PlaneType planeType : aircraft.values())
        {
            assert(planeType.getRoleCategories().size() > 0);
        }
    }
}
