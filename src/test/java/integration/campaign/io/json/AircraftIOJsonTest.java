package integration.campaign.io.json;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.AircraftIOJson;
import pwcg.campaign.plane.TankType;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class AircraftIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Map<String, TankType> aircraft = AircraftIOJson.readJson();
        validate(aircraft);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Map<String, TankType> aircraft = AircraftIOJson.readJson();
        validate(aircraft);
    }

    private void validate(Map<String, TankType> aircraft)
    {
        Assertions.assertTrue (aircraft.size() > 0);
        for (TankType planeType : aircraft.values())
        {
            assert(planeType.getRoleCategories().size() > 0);
        }
    }
}
