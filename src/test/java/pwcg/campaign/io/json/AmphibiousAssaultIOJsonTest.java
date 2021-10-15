package pwcg.campaign.io.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.battle.AmphibiousAssaults;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
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
