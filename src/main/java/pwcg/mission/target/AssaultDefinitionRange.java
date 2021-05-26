package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.BattleSize;

public class AssaultDefinitionRange
{
    private static final int MINIMUM_INDECES_FROM_ASSAULT_CENTER = 6;
    private static final int MINIMUM_INDECES_FOR_ASSAULT = MINIMUM_INDECES_FROM_ASSAULT_CENTER * 2;

    public static int determineNumberOfAssaultSegments(BattleSize battleSize)
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int numAssaultSegments = productSpecific.getNumAssaultSegments(battleSize);
        return numAssaultSegments;
    }


    public static int calculateMaximumAssultsForFrontPoints(int numberOfIndeces, int numBattles)
    {
        int maxBattles = numberOfIndeces / MINIMUM_INDECES_FOR_ASSAULT;
        return maxBattles;
    }

    public static List<Integer> calculateFirstBattlePoints(int numberOfIndeces, int numBattles)
    {
        int numIndecesNeededForBattle = numBattles * MINIMUM_INDECES_FOR_ASSAULT;
        int extraIndeces = numberOfIndeces  - numIndecesNeededForBattle;
        int startIndex = MINIMUM_INDECES_FROM_ASSAULT_CENTER;
        if (extraIndeces > 0)
        {
            startIndex = RandomNumberGenerator.getRandom(extraIndeces) + MINIMUM_INDECES_FROM_ASSAULT_CENTER;
        }
        
        List<Integer> indecesToUse = new ArrayList<>();
        for (int i = 0; i < numBattles; ++i)
        {
            int indexToUse = startIndex + (i * MINIMUM_INDECES_FOR_ASSAULT);
            indecesToUse.add(indexToUse);
        }
        return indecesToUse;
    }
}
