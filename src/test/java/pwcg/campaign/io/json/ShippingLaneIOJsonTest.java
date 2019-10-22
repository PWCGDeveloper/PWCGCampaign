package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.target.locator.ShippingLanes;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class ShippingLaneIOJsonTest
{
    @Test
    public void readJsonChannelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Arras");
        assert (shippingLanes.getAlliedShippingLanes().size() == 0);
        assert (shippingLanes.getAxisShippingLanes().size() == 0);
    }

    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Moscow");
        assert (shippingLanes.getAlliedShippingLanes().size() == 0);
        assert (shippingLanes.getAxisShippingLanes().size() == 0);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Stalingrad");
        assert (shippingLanes.getAlliedShippingLanes().size() == 0);
        assert (shippingLanes.getAxisShippingLanes().size() == 0);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Kuban");
        assert (shippingLanes.getAlliedShippingLanes().size() > 0);
        assert (shippingLanes.getAxisShippingLanes().size() > 0);
    }

    @Test
    public void readJsonBodenplatteTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Bodenplatte");
        assert (shippingLanes.getAlliedShippingLanes().size() == 0);
        assert (shippingLanes.getAxisShippingLanes().size() == 0);
    }

}
