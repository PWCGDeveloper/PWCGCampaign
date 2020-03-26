package pwcg.core.utils;

import java.util.List;

public class WeightedOddsCalculator
{
    public static int calculateWeightedodds(List<Integer> weightedOdds)
    {
        int totalWeighting = 0;
        for (int weight : weightedOdds)
        {
            totalWeighting += weight;
        }
        
        int roll = RandomNumberGenerator.getRandom(totalWeighting);
        int selectedIndex = 0;
        int sumOfWeights = 0;
        for(int index = 0; index < weightedOdds.size(); ++ index)
        {
            sumOfWeights += weightedOdds.get(index);
            if (roll < sumOfWeights)
            {
                selectedIndex = index;
                break;
            }
        }
        return selectedIndex;
    }
}
