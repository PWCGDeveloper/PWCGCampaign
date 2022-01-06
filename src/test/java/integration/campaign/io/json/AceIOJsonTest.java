package integration.campaign.io.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class AceIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        Assertions.assertTrue (aces.size() == 186);        
        verifyNoDuplicateSerialNumbers(aces);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<HistoricalAce> aces = HistoricalAceIOJson.readJson();
        Assertions.assertTrue (aces.size() == 17);        
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
