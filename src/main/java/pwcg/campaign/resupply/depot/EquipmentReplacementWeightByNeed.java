package pwcg.campaign.resupply.depot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.ServiceResupplyNeed;
import pwcg.campaign.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementWeightByNeed
{
    private Campaign campaign;

    public EquipmentReplacementWeightByNeed (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Map<String, Integer> getAircraftNeedByArchType(List<Company> squadronsForService) throws PWCGException
    {
        if (squadronsForService.size() == 0)
        {
            return new HashMap<>();
        }
        
        ArmedService service = squadronsForService.get(0).determineServiceForSquadron(campaign.getDate());
        ServiceResupplyNeed resupplyNeed = determineSquadronNeeds(service);        
        Map<String, Integer> aircraftNeedByArchType = determineAircraftNeedByArchType(resupplyNeed);
        return aircraftNeedByArchType;
    }

    private ServiceResupplyNeed determineSquadronNeeds(ArmedService service) throws PWCGException
    {
        ResupplyNeedBuilder needBuilder = new ResupplyNeedBuilder(campaign, service);
        ServiceResupplyNeed resupplyNeed = needBuilder.determineNeedForService(SquadronNeedType.EQUIPMENT);
        return resupplyNeed;
    }

    private Map<String, Integer> determineAircraftNeedByArchType(ServiceResupplyNeed resupplyNeed) throws PWCGException
    {
        Map<String, Integer> aircraftNeedByArchType = new HashMap<>();
        for (int squadronId: resupplyNeed.getSquadronNeeds().keySet())
        {
            ISquadronNeed squadronNeed = resupplyNeed.getSquadronNeeds().get(squadronId);
            if (squadronNeed.getNumNeeded() > 0)
            {
                Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);
                List<String> asrchTypesForSquadron = squadron.getActiveArchTypes(campaign.getDate());
                
                for (String archType : asrchTypesForSquadron)
                {
                    if (!aircraftNeedByArchType.containsKey(archType))
                    {
                        aircraftNeedByArchType.put(archType, 0);
                    }
                    
                    int numNeededForArchType = aircraftNeedByArchType.get(archType);
                    numNeededForArchType += squadronNeed.getNumNeeded();
                    aircraftNeedByArchType.put(archType, numNeededForArchType);                    
                }
            }
        }
        return aircraftNeedByArchType;
    }
}
