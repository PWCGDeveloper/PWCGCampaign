package pwcg.campaign.resupply.depot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankArchType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class EquipmentReplacementCalculator
{
    private Campaign campaign;
    private List<String> squadronNeedsForLosses = new ArrayList<>();
    private List<String> weightedArchTypeUsage = new ArrayList<>();
    private int needIndex = 0;

    public EquipmentReplacementCalculator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void createArchTypeForReplacementPlane(List<Company> squadronsForService) throws PWCGException
    {
        loadWeightsByUsage(squadronsForService);        
        loadWeightsByNeed(squadronsForService);        
    }

    public String chooseArchTypeForReplacementByUsage() throws PWCGException
    {
        verifyWasInitialized();
        int index = RandomNumberGenerator.getRandom(weightedArchTypeUsage.size());        
        return weightedArchTypeUsage.get(index);
    }
    
    public boolean hasMoreForReplacement()
    {
        return (needIndex < squadronNeedsForLosses.size());
    }

    public String chooseArchTypeForReplacementByNeed() throws PWCGException
    {
        verifyWasInitialized();
        if (needIndex == squadronNeedsForLosses.size())
        {
            throw new PWCGException("Requested more planes than available for need");
        }
        String archType = squadronNeedsForLosses.get(needIndex);
        ++needIndex;
        return archType;
    }
    
    private void verifyWasInitialized() throws PWCGException
    {
        if (weightedArchTypeUsage.size() == 0)
        {
            throw new PWCGException("EquipmentReplacementCalculator used but has no aircraft arch types in service");
        }
    }

    private void loadWeightsByUsage(List<Company> squadronsForService) throws PWCGException
    {
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(campaign.getDate());
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);
        loadWeightedList(aircraftUsageByArchType, weightedArchTypeUsage);        
    }

    private void loadWeightsByNeed(List<Company> squadronsForService) throws PWCGException
    {
        EquipmentReplacementWeightByNeed equipmentReplacementWeightByNeed = new EquipmentReplacementWeightByNeed(campaign);
        Map<String, Integer> replacementsForLosses = equipmentReplacementWeightByNeed.getAircraftNeedByArchType(squadronsForService);
        loadWeightedList(replacementsForLosses, squadronNeedsForLosses);
    }

    private void loadWeightedList(Map<String, Integer> aircraftReplacementWeights, List<String> needs) throws PWCGException
    {
        for (String planeArchTypeName : aircraftReplacementWeights.keySet())
        {
            TankArchType planeArchType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(planeArchTypeName);
            if (planeArchType.getInProductionMemberTankTypes(campaign.getDate()).size() > 0)
            {
                int numUses = aircraftReplacementWeights.get(planeArchTypeName);
                for (int i = 0; i < numUses; ++i)
                {
                    needs.add(planeArchTypeName);
                }
            }
        }
        
        Collections.shuffle(needs);
    }
}
