package pwcg.campaign.resupply.depot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class EquipmentWeightCalculator
{
    private Date campaignDate;
    private Map<String, Integer> weightedPlaneOdds = new HashMap<>();

    public EquipmentWeightCalculator (Date campaignDate)
    {
        this.campaignDate = campaignDate;
    }
    
    public void determinePlaneWeightsForPlanes(List<TankType> planeTypes) throws PWCGException
    {
        for (TankType planeType : planeTypes)
        {
            Integer planeWeight = determinePlaneWeight(planeType);
            weightedPlaneOdds.put(planeType.getType(), planeWeight);
        }
    }

    public String getTankTypeFromWeight()
    {
        int totalWeight = determineTotalWeight(weightedPlaneOdds);
        int accumulatedWeight = 0;
        int roll = RandomNumberGenerator.getRandom(totalWeight);
        String planeTypeDefault = "";
        for (String planeTypeName : weightedPlaneOdds.keySet())
        {
            planeTypeDefault = planeTypeName;
            
            Integer weightForThisTankType = weightedPlaneOdds.get(planeTypeName);
            accumulatedWeight += weightForThisTankType;
            if (roll < accumulatedWeight)
            {
                return planeTypeName;
            }
        }
        
        return planeTypeDefault;
    }

    private int determineTotalWeight(Map<String, Integer> weightedPlaneOdds)
    {
        int totalWeight = 0;
        for (Integer weight : weightedPlaneOdds.values())
        {
            totalWeight += weight;
        }
        return totalWeight;
    }

    private Integer determinePlaneWeight(TankType planeType) throws PWCGException
    {
        Integer daysSinceIntroduction = DateUtils.daysDifference(planeType.getIntroduction(), campaignDate);
        Integer weight = daysSinceIntroduction;
        if (weight > 100)
        {
            weight = 100;
        }
        
        return weight;
    }

    public Map<String, Integer> getWeightedPlaneOdds()
    {
        return weightedPlaneOdds;
    }
}
