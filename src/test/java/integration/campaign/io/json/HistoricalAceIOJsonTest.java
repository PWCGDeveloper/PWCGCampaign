package integration.campaign.io.json;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.HistoricalAce;
import pwcg.campaign.io.json.HistoricalAceIOJson;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class HistoricalAceIOJsonTest
{
    @Test
    public void readJsonRoFTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        Assertions.assertTrue (aces.size() > 0);
    }

    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        Assertions.assertTrue (aces.size() > 0);
    }
}
