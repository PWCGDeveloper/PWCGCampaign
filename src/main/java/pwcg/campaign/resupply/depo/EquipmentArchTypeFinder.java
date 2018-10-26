package pwcg.campaign.resupply.depo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class EquipmentArchTypeFinder
{
    private Campaign campaign;
    private Map<String, Integer> aircraftUsageByArchType = new HashMap<>();

    public EquipmentArchTypeFinder (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public String getArchTypeForReplacementPlane(List<Squadron> squadronsForService) throws PWCGException
    {
        aircraftUsageByArchType = getAircraftUsageByArchType(squadronsForService);

        List<String> weightedArchTypeUsage = new ArrayList<>();
        for (String planeArchTypeName : aircraftUsageByArchType.keySet())
        {
            int numUses = aircraftUsageByArchType.get(planeArchTypeName);
            for (int i = 0; i < numUses; ++i)
            {
                weightedArchTypeUsage.add(planeArchTypeName);
            }
        }
        
        int index = RandomNumberGenerator.getRandom(weightedArchTypeUsage.size());
        
        return weightedArchTypeUsage.get(index);
    }

    private Map<String, Integer> getAircraftUsageByArchType(List<Squadron> squadronsForService) throws PWCGException
    {
        for (Squadron squadron : squadronsForService)
        {
            List<PlaneArchType> currentAircraftArchTypes = squadron.determineCurrentAircraftArchTypes(campaign.getDate());
            for (PlaneArchType planeArchType : currentAircraftArchTypes)
            {
                if (!aircraftUsageByArchType.containsKey(planeArchType.getPlaneArchTypeName()))
                {
                    aircraftUsageByArchType.put(planeArchType.getPlaneArchTypeName(), new Integer(0)); 
                }
                
                Integer numUsagesOfArchtype = aircraftUsageByArchType.get(planeArchType.getPlaneArchTypeName());
                ++numUsagesOfArchtype;
                aircraftUsageByArchType.put(planeArchType.getPlaneArchTypeName(), numUsagesOfArchtype);
            }
        }
        return aircraftUsageByArchType;
    }

    public Map<String, Integer> getAircraftUsageByArchType()
    {
        return aircraftUsageByArchType;
    }
}
