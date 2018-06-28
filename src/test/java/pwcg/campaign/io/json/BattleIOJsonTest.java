package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Battles;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class BattleIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        Battles battles = BattleIOJson.readJson();
        assert (battles.getBattles().size() > 0);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Battles battles = BattleIOJson.readJson();
        assert (battles.getBattles().size() > 0);
    }
}
