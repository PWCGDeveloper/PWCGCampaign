package pwcg.campaign.skin;

import java.util.Map;

import org.junit.jupiter.api.Test;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

public class SkinLoaderTest
{
    @Test
    public void skinLoaderRoFTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        SkinLoader skinLoader = new SkinLoader();
        Map<String, SkinsForPlane> skinsForPlanes = skinLoader.loadPwcgSkins();
        
        assert(skinsForPlanes != null);
    }

    @Test
    public void skinLoaderBoSTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        SkinLoader skinLoader = new SkinLoader();
        Map<String, SkinsForPlane> skinsForPlanes = skinLoader.loadPwcgSkins();
        
        assert(skinsForPlanes != null);
    }

}
