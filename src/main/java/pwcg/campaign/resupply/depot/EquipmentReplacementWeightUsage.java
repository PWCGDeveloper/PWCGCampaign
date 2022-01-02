package pwcg.campaign.resupply.depot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.squadron.Company;
import pwcg.campaign.squadron.SquadronViability;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementWeightUsage
{
    private Date campaignDate;

    public EquipmentReplacementWeightUsage (Date campaignDate)
    {
        this.campaignDate = campaignDate;
    }

    public Map<String, Integer> getAircraftUsageByArchType(List<Company> squadronsForService) throws PWCGException
    {
        Map<String, Integer> aircraftUsageByArchType = new HashMap<>();
        
        for (Company squadron : squadronsForService)
        {
            if (SquadronViability.isSquadronActive(squadron, campaignDate))
            {
                List<PlaneArchType> currentAircraftArchTypes = squadron.determineCurrentAircraftArchTypes(campaignDate);
                for (PlaneArchType planeArchType : currentAircraftArchTypes)
                {
                    if (excludeFromWeightedList(planeArchType.getPlaneArchTypeName()))
                    {
                        continue;
                    }
                    
                    if (!aircraftUsageByArchType.containsKey(planeArchType.getPlaneArchTypeName()))
                    {
                        aircraftUsageByArchType.put(planeArchType.getPlaneArchTypeName(), Integer.valueOf(0)); 
                    }
                    
                    Integer numUsagesOfArchtype = aircraftUsageByArchType.get(planeArchType.getPlaneArchTypeName());
                    ++numUsagesOfArchtype;
                    aircraftUsageByArchType.put(planeArchType.getPlaneArchTypeName(), numUsagesOfArchtype);
                }
            }
        }
        
        return aircraftUsageByArchType;
    }
    
    private boolean excludeFromWeightedList(String archType)
    {
        if (archType.equalsIgnoreCase("ju52"))
        {
            return true;
        }
        return false;
    }
}
