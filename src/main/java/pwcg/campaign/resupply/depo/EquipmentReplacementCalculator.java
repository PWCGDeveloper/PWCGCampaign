package pwcg.campaign.resupply.depo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.resupply.SquadronNeedFactory;
import pwcg.campaign.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class EquipmentReplacementCalculator
{
    private Campaign campaign;
    private List<String> weightedArchTypeUsage = new ArrayList<>();

    public EquipmentReplacementCalculator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public String getArchTypeForReplacementPlane(List<Squadron> squadronsForService) throws PWCGException
    {
        loadWeightsByUsage(squadronsForService);        
        loadWeightsByNeed(squadronsForService);        
        return chooseArchTypeForReplacement();
    }

    private String chooseArchTypeForReplacement()
    {
        int index = RandomNumberGenerator.getRandom(weightedArchTypeUsage.size());        
        return weightedArchTypeUsage.get(index);
    }

    private void loadWeightsByUsage(List<Squadron> squadronsForService) throws PWCGException
    {
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(campaign.getDate());
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);
        loadWeightedList(aircraftUsageByArchType);
    }

    private void loadWeightsByNeed(List<Squadron> squadronsForService) throws PWCGException
    {
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentReplacementWeightNeed equipmentReplacementWeightNeed = new EquipmentReplacementWeightNeed(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);
        loadWeightedList(aircraftNeedByArchType);
    }

    private void loadWeightedList(Map<String, Integer> aircraftReplacementWeights)
    {
        for (String planeArchTypeName : aircraftReplacementWeights.keySet())
        {
            int numUses = aircraftReplacementWeights.get(planeArchTypeName);
            for (int i = 0; i < numUses; ++i)
            {
                weightedArchTypeUsage.add(planeArchTypeName);
            }
        }
    }
}
