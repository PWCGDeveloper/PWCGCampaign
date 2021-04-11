package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.battle.AmphibiousAssaults;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AmphibiousAssaultIOJsonTest
{
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance();
        AmphibiousAssaults amphibiousAssault = AmphibiousAssaultIOJson.readJson(FrontMapIdentifier.KUBAN_MAP.getMapName());
        assert (amphibiousAssault.getAmphibiousAssaults().size() > 0);
    }
}
