package pwcg.aar.outofmission.phase2.transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class ServiceTransferNeed
{
    private Campaign campaign;
    private int serviceId;
    private Map<Integer, SquadronTransferNeed> squadronTransferNeeds = new HashMap<>();

    public ServiceTransferNeed (Campaign campaign, int serviceId)
    {
        this.campaign = campaign;
        this.serviceId = serviceId;
    }
    
    public void determineTransferNeed() throws PWCGException
    {
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
            int serviceIdForSquadron = squadronPersonnel.getSquadron().determineServiceForSquadron(campaign.getDate()).getServiceId();
            if (serviceId == serviceIdForSquadron)
            {
                SquadronTransferNeed squadronTransferNeed = new SquadronTransferNeed(campaign, squadronPersonnel.getSquadron());
                squadronTransferNeed.determineTransfersNeeded();
                if (squadronTransferNeed.needsTransfer())
                {
                    squadronTransferNeeds.put(squadronTransferNeed.getSquadronId(), squadronTransferNeed);
                }
            }
        }
    }
    
    public boolean hasNeedySquadron()
    {
        if (squadronTransferNeeds.isEmpty())
        {
            return false;
        }
        return true;
    }

    public int chooseNeedySquadron() throws PWCGException
    {
        if (squadronTransferNeeds.isEmpty())
        {
            throw new PWCGException("No needy squadron to select");
        }
        
        List<Integer> squadronKeys = new ArrayList<>(squadronTransferNeeds.keySet());
        int index = RandomNumberGenerator.getRandom(squadronKeys.size());
        int squadronKey = squadronKeys.get(index);
        
        SquadronTransferNeed squadronForReplacement = squadronTransferNeeds.get(squadronKey);
        squadronForReplacement.noteTransfer();
        if (!squadronForReplacement.needsTransfer())
        {
            squadronTransferNeeds.remove(squadronKey);
        }
        
        return squadronForReplacement.getSquadronId();
    }
}
