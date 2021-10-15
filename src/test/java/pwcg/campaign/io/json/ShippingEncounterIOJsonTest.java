package pwcg.campaign.io.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.shipping.ShipEncounterZones;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
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
