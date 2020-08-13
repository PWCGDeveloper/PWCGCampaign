package pwcg.campaign.io.json;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skin.SkinSet;
import pwcg.campaign.skin.SkinSetType;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class SkinIOJsonTest
{
    @Test
    public void readJsonRoFSkinsConfiguredTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_CONFIGURED.getSkinSetName());
        assert (skins.size() > 0);
    }
    
    @Test
    public void readJsonRoFSkinsDoNotUseTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_DO_NOT_USE.getSkinSetName());
        assert (skins.size() == 0);
    }
    
    @Test
    public void readJsonBoSSkinsConfiguredTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_CONFIGURED.getSkinSetName());
        assert (skins.size() > 0);
    }
    
    @Test
    public void readJsonBoSSkinsDoNotUseTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_DO_NOT_USE.getSkinSetName());
        assert (skins.size() > 0);
    }

}
