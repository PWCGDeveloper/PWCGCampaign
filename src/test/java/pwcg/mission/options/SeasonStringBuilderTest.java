package pwcg.mission.options;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class SeasonStringBuilderTest
{
    public SeasonStringBuilderTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.BODENPLATTE_MAP);
    }

    @Test
    public void testClearSkies() throws PWCGException
    {
        String skies = SeasonStringBuilder.getClearSkys(DateUtils.getDateYYYYMMDD("19441201"));
        assert(skies.startsWith("winter"));
        assert(skies.toLowerCase().contains("clear"));
        assert(skies.endsWith("_00\\sky.ini") || skies.endsWith("_02\\sky.ini") || skies.endsWith("_04\\sky.ini"));
    }

    @Test
    public void testLightSkies() throws PWCGException
    {
        String skies = SeasonStringBuilder.getLightSkys(DateUtils.getDateYYYYMMDD("19441001"));
        assert(skies.startsWith("summer"));
        assert(skies.toLowerCase().contains("light"));
        assert(skies.endsWith("_00\\sky.ini") || skies.endsWith("_02\\sky.ini") || skies.endsWith("_04\\sky.ini"));
    }

    @Test
    public void testAverageSkies() throws PWCGException
    {
        String skies = SeasonStringBuilder.getAverageSkys(DateUtils.getDateYYYYMMDD("19440901"));
        assert(skies.startsWith("summer"));
        assert(skies.toLowerCase().contains("medium"));
    }

    @Test
    public void testHeavySkies() throws PWCGException
    {
        String skies = SeasonStringBuilder.getHeavySkys(DateUtils.getDateYYYYMMDD("19440301"));
        assert(skies.startsWith("summer"));
        assert(skies.toLowerCase().contains("heavy"));
        assert(skies.endsWith("_05\\sky.ini") || skies.endsWith("_07\\sky.ini") || skies.endsWith("_09\\sky.ini"));
    }

    @Test
    public void testOvercastSkies() throws PWCGException
    {
        String skies = SeasonStringBuilder.getOvercastSkys(DateUtils.getDateYYYYMMDD("19441201"));
        assert(skies.startsWith("winter"));
        assert(skies.toLowerCase().contains("overcast"));
        assert(skies.endsWith("_05\\sky.ini") || skies.endsWith("_07\\sky.ini") || skies.endsWith("_09\\sky.ini"));
    }

}
