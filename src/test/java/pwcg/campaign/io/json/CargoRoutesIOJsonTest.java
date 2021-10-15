package pwcg.campaign.io.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.shipping.CargoRoutes;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
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
