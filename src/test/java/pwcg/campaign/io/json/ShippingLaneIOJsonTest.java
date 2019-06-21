package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.locator.ShippingLanes;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class ShippingLaneIOJsonTest
{
    @Test
    public void readJsonFranceTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Channel");
        assert (shippingLanes.getAlliedShippingLanes().size() > 0);
        assert (shippingLanes.getAxisShippingLanes().size() > 0);
    }

    @Test
    public void readJsonChannelTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("France");
        assert (shippingLanes.getAlliedShippingLanes().size() == 0);
        assert (shippingLanes.getAxisShippingLanes().size() == 0);
    }

    @Test
    public void readJsonGaliciaTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Galicia");
        assert (shippingLanes.getAlliedShippingLanes().size() == 0);
        assert (shippingLanes.getAxisShippingLanes().size() == 0);
    }
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Moscow");
        assert (shippingLanes.getAlliedShippingLanes().size() == 0);
        assert (shippingLanes.getAxisShippingLanes().size() == 0);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Stalingrad");
        assert (shippingLanes.getAlliedShippingLanes().size() == 0);
        assert (shippingLanes.getAxisShippingLanes().size() == 0);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        ShippingLanes shippingLanes = ShippingLaneIOJson.readJson("Kuban");
        assert (shippingLanes.getAlliedShippingLanes().size() > 0);
        assert (shippingLanes.getAxisShippingLanes().size() > 0);
    }

}
