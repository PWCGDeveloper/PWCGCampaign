package pwcg.campaign.io.json;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AircraftIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        Map<String, PlaneType> aircraft = AircraftIOJson.readJson();
        assert (aircraft.size() > 0);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Map<String, PlaneType> aircraft = AircraftIOJson.readJson();
        assert (aircraft.size() > 0);
    }
}
