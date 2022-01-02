package pwcg.campaign.resupply.depot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.resupply.SquadronNeedFactory;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;

public class EquipmentNeedForSquadronsCalculator
{
    private Campaign campaign;
    private SquadronNeedFactory squadronNeedFactory;
    
    public EquipmentNeedForSquadronsCalculator (Campaign campaign, SquadronNeedFactory squadronNeedFactory)
    {
        this.campaign = campaign;
        this.squadronNeedFactory = squadronNeedFactory;
    }

    public Map<String, Integer> getAircraftNeedByArchType(List<Company> squadronsForService) throws PWCGException
    {
        Map<String, Integer> aircraftNeedByArchType = new HashMap<>();

        for (Company squadron : squadronsForService)
        {
            ISquadronNeed squadronResupplyNeed = squadronNeedFactory.buildSquadronNeed(campaign, squadron);
            squadronResupplyNeed.determineResupplyNeeded();                
            for (int i = 0; i < squadronResupplyNeed.getNumNeeded(); ++i)
            {
                List<PlaneArchType> currentAircraftArchTypes = squadron.determineCurrentAircraftArchTypes(campaign.getDate());
                for (PlaneArchType planeArchType : currentAircraftArchTypes)
                {
                    if (!aircraftNeedByArchType.containsKey(planeArchType.getPlaneArchTypeName()))
                    {
                        aircraftNeedByArchType.put(planeArchType.getPlaneArchTypeName(), 0);
                    }
                    
                    int numNeededForArchType = aircraftNeedByArchType.get(planeArchType.getPlaneArchTypeName());
                    aircraftNeedByArchType.put(planeArchType.getPlaneArchTypeName(), numNeededForArchType+1);
                }
            }
        }
        
        return aircraftNeedByArchType;
    }
}
