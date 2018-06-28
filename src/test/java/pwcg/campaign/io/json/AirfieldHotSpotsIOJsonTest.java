package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.airfield.AirfieldHotSpotCollection;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AirfieldHotSpotsIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        AirfieldHotSpotCollection airfieldHotSpotCollection = AirfieldHotSpotsIOJson.readJson();
        assert (airfieldHotSpotCollection.getAirfieldHotSpots().size() > 0);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        AirfieldHotSpotCollection airfieldHotSpotCollection = AirfieldHotSpotsIOJson.readJson();
        assert (airfieldHotSpotCollection.getAirfieldHotSpots().size() > 0);
    }
}
