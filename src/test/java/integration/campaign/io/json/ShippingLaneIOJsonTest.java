package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.ShippingLaneIOJson;
import pwcg.campaign.shipping.ShippingLanes;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class ShippingLaneIOJsonTest
{
    @Test
    public void readJsonChannelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Arras");
        Assertions.assertTrue (shippingLanes.getAlliedShippingLanes().size() == 0);
        Assertions.assertTrue (shippingLanes.getAxisShippingLanes().size() == 0);
    }

    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Moscow");
        Assertions.assertTrue (shippingLanes.getAlliedShippingLanes().size() == 0);
        Assertions.assertTrue (shippingLanes.getAxisShippingLanes().size() == 0);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Stalingrad");
        Assertions.assertTrue (shippingLanes.getAlliedShippingLanes().size() == 0);
        Assertions.assertTrue (shippingLanes.getAxisShippingLanes().size() == 0);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Kuban");
        Assertions.assertTrue (shippingLanes.getAlliedShippingLanes().size() > 0);
        Assertions.assertTrue (shippingLanes.getAxisShippingLanes().size() > 0);
    }

    @Test
    public void readJsonBodenplatteTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Bodenplatte");
        Assertions.assertTrue (shippingLanes.getAlliedShippingLanes().size() == 0);
        Assertions.assertTrue (shippingLanes.getAxisShippingLanes().size() == 0);
    }

}
