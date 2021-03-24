package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Battles;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class BattleIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Battles battles = BattleIOJson.readJson(FrontMapIdentifier.ARRAS_MAP.getMapName());
        assert (battles.getBattles().size() > 0);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Battles battles = BattleIOJson.readJson(FrontMapIdentifier.STALINGRAD_MAP.getMapName());
        assert (battles.getBattles().size() > 0);
    }
}
