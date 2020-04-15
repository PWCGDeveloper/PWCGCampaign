package pwcg.campaign.io.json;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class HistoricalAceIOJsonTest
{
    @Test
    public void readJsonRoFTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        assert (aces.size() > 0);
    }

    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        assert (aces.size() > 0);
    }
}
