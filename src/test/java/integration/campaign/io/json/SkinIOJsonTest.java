package integration.campaign.io.json;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.SkinIOJson;
import pwcg.campaign.skin.SkinSet;
import pwcg.campaign.skin.SkinSetType;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class SkinIOJsonTest
{
    @Test
    public void readJsonRoFSkinsConfiguredTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_CONFIGURED.getSkinSetName());
        Assertions.assertTrue (skins.size() > 0);
    }
    
    @Test
    public void readJsonRoFSkinsDoNotUseTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_DO_NOT_USE.getSkinSetName());
        Assertions.assertTrue (skins.size() == 0);
    }
    
    @Test
    public void readJsonBoSSkinsConfiguredTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_CONFIGURED.getSkinSetName());
        Assertions.assertTrue (skins.size() > 0);
    }
    
    @Test
    public void readJsonBoSSkinsDoNotUseTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Map<String, SkinSet> skins = SkinIOJson.readSkinSet(SkinSetType.SKIN_DO_NOT_USE.getSkinSetName());
        Assertions.assertTrue (skins.size() > 0);
    }

}
