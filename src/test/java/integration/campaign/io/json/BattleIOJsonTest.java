package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.battle.Battles;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.BattleIOJson;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class BattleIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Battles battles = BattleIOJson.readJson(FrontMapIdentifier.STALINGRAD_MAP.getMapName());
        Assertions.assertTrue (battles.getBattles().size() > 0);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Battles battles = BattleIOJson.readJson(FrontMapIdentifier.STALINGRAD_MAP.getMapName());
        Assertions.assertTrue (battles.getBattles().size() > 0);
    }
}
