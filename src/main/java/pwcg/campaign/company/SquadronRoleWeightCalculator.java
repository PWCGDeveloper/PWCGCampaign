package pwcg.campaign.company;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.tank.PwcgRoleCategory;

public class SquadronRoleWeightCalculator
{
    public static PwcgRoleCategory calculateHeaviestSquadronRole(SquadronRolePeriod squadronRolePeriod)
    {
        Map<PwcgRoleCategory, Integer> weightedRoleCategories = calculateCategoryWeights(squadronRolePeriod);
        PwcgRoleCategory heaviestCategory = determineHeaviestCategory(weightedRoleCategories);
        return heaviestCategory;
    }

    private static Map<PwcgRoleCategory, Integer> calculateCategoryWeights(SquadronRolePeriod squadronRolePeriod)
    {
        Map<PwcgRoleCategory, Integer> weightedRoleCategories = new HashMap<>();
        for (SquadronRoleWeight squadronRoleWeight : squadronRolePeriod.getWeightedRoles())
        {
            PwcgRoleCategory roleCategory = squadronRoleWeight.getRole().getRoleCategory();
            if (!weightedRoleCategories.containsKey(roleCategory))
            {
                weightedRoleCategories.put(roleCategory, 0);
            }
            
            int currentWeight = weightedRoleCategories.get(roleCategory);
            currentWeight +=  squadronRoleWeight.getWeight();
            weightedRoleCategories.put(roleCategory, currentWeight);
        }
        return weightedRoleCategories;
    }

    private static PwcgRoleCategory determineHeaviestCategory(Map<PwcgRoleCategory, Integer> weightedRoleCategories)
    {
        PwcgRoleCategory heaviestCategory = PwcgRoleCategory.OTHER;
        int heaviestWeight = 0;
        for (PwcgRoleCategory roleCategory : weightedRoleCategories.keySet())
        {
            int categoryWeight = weightedRoleCategories.get(roleCategory);
            if (categoryWeight > heaviestWeight)
            {
                heaviestWeight = categoryWeight;
                heaviestCategory = roleCategory;
            }
        }
        return heaviestCategory;
    }
}
