package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.shipping.CargoRoutes;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class CargoRoutesIOJsonTest
{
    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        CargoRoutes cargoRoutes = CargoRoutesIOJson.readJson("Kuban");
        assert (cargoRoutes.getRouteDefinitions().size() > 0);
    }
}
