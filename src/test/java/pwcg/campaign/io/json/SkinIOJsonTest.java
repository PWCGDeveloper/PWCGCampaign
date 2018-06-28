package pwcg.campaign.io.json;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.skin.SkinSet;
import pwcg.campaign.skin.SkinSetType;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class SkinIOJsonTest
{
    @Test
    public void readJsonRoFSkinsConfiguredTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_CONFIGURED.getSkinSetName());
        assert (skins.size() > 0);
    }
    
    @Test
    public void readJsonRoFSkinsDoNotUseTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_DO_NOT_USE.getSkinSetName());
        assert (skins.size() > 0);
    }
    
    @Test
    public void readJsonBoSSkinsConfiguredTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_CONFIGURED.getSkinSetName());
        assert (skins.size() > 0);
    }
    
    @Test
    public void readJsonBoSSkinsDoNotUseTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_DO_NOT_USE.getSkinSetName());
        assert (skins.size() > 0);
    }

}
