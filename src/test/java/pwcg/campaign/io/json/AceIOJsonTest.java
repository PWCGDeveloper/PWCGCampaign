package pwcg.campaign.io.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AceIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        assert (aces.size() > 0);        
        verifyNoDuplicateSerialNumbers(aces);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        assert (aces.size() > 0);
        verifyNoDuplicateSerialNumbers(aces);
    }

    private void verifyNoDuplicateSerialNumbers(List<HistoricalAce> aces)
    {
        Map<Integer, HistoricalAce> aceMap = new HashMap<>();
        for (HistoricalAce ace : aces)
        {
            assert(aceMap.containsKey(ace.getSerialNumber()) == false);
            aceMap.put(ace.getSerialNumber(), ace);
        }
    }
}
