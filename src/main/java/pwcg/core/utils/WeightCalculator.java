package pwcg.core.utils;

import java.util.ArrayList;
import java.util.List;

public class WeightCalculator
{
    private List<IWeight> weightedItems = new ArrayList<>();

    public WeightCalculator (List<IWeight> weightedItems)
    {
        this.weightedItems = weightedItems;
    }

    public int getItemFromWeight()
    {
        int totalWeight = determineTotalWeight();
        int accumulatedWeight = 0;
        int roll = RandomNumberGenerator.getRandom(totalWeight);
        int selectedItemIndex = 0;
        for (IWeight weight : weightedItems)
        {            
            accumulatedWeight += weight.getWeight();
            if (roll < accumulatedWeight)
            {
                return selectedItemIndex;
            }
            ++selectedItemIndex;
        }
        
        return weightedItems.size()-1;
    }

    private int determineTotalWeight()
    {
        int totalWeight = 0;
        for (IWeight weight : weightedItems)
        {
            totalWeight += weight.getWeight();
        }
        return totalWeight;
    }
}
