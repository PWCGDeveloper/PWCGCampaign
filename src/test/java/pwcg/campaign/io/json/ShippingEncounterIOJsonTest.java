package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.shipping.ShipEncounterZones;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class ShippingEncounterIOJsonTest
{
    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        ShipEncounterZones shipEncounterZones = ShipEncounterZonesIOJson.readJson("Kuban");
        assert (shipEncounterZones.getShipEncounterZones().size() > 0);
    }
}
