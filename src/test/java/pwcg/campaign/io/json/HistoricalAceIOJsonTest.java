package pwcg.campaign.io.json;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class HistoricalAceIOJsonTest
{
    @Test
    public void readJsonRoFTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        assert (aces.size() > 0);
    }

    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        assert (aces.size() > 0);
    }
}
