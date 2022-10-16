package pwcg.campaign.group;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class GroupManagerTest
{
    @Mock Campaign campaign;
    
    @Test
    public void readJsonNormandyTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Normandy";
        GroupManager beforeNormandy = validateGroundStructures(mapName);
        beforeNormandy.configureForDate(mapName, DateUtils.getDateYYYYMMDD("19430601"));

        PWCGContext.setProduct(PWCGProduct.BOS);
        GroupManager atNormandy = validateGroundStructures(mapName);
        atNormandy.configureForDate(mapName, DateUtils.getDateYYYYMMDD("19440601"));

        PWCGContext.setProduct(PWCGProduct.BOS);
        GroupManager afterNormandy = validateGroundStructures(mapName);
        afterNormandy.configureForDate(mapName, DateUtils.getDateYYYYMMDD("19440701"));

        Assertions.assertTrue(atNormandy.getStandaloneBlocks().size() > beforeNormandy.getStandaloneBlocks().size());
        Assertions.assertTrue(afterNormandy.getStandaloneBlocks().size() > atNormandy.getStandaloneBlocks().size());

        Assertions.assertTrue(atNormandy.getBlockFinder().getAllBlocks().size() > beforeNormandy.getBlockFinder().getAllBlocks().size());
        Assertions.assertTrue(afterNormandy.getBlockFinder().getAllBlocks().size() > atNormandy.getBlockFinder().getAllBlocks().size());
    }
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Moscow";
        GroupManager beforeNormandy = validateGroundStructures(mapName);
        beforeNormandy.configureForDate(mapName, null);

        PWCGContext.setProduct(PWCGProduct.BOS);
        GroupManager atNormandy = validateGroundStructures(mapName);
        atNormandy.configureForDate(mapName, null);

        PWCGContext.setProduct(PWCGProduct.BOS);
        GroupManager afterNormandy = validateGroundStructures(mapName);
        afterNormandy.configureForDate(mapName, null);

        Assertions.assertTrue(atNormandy.getStandaloneBlocks().size() == beforeNormandy.getStandaloneBlocks().size());
        Assertions.assertTrue(afterNormandy.getStandaloneBlocks().size() == atNormandy.getStandaloneBlocks().size());

        Assertions.assertTrue(atNormandy.getBlockFinder().getAllBlocks().size() == beforeNormandy.getBlockFinder().getAllBlocks().size());
        Assertions.assertTrue(afterNormandy.getBlockFinder().getAllBlocks().size() == atNormandy.getBlockFinder().getAllBlocks().size());
    }


    private GroupManager validateGroundStructures(String mapName) throws PWCGException, PWCGException
    {
        GroupManager groupManager = new GroupManager();
        groupManager.configure(mapName);
        Assertions.assertTrue (groupManager.getRailroadList().size() > 0);
        Assertions.assertTrue (groupManager.getBridgeFinder().findAllBridges().size() > 0);
        Assertions.assertTrue (groupManager.getStandaloneBlocks().size() > 0);
                
        return groupManager;
    }


}
