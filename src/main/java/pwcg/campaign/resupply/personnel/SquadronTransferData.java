package pwcg.campaign.resupply.personnel;

import java.util.ArrayList;
import java.util.List;

public class SquadronTransferData
{
    private List<TransferRecord> squadronMembersTransferred = new ArrayList<>();
    
    public void addTransferRecord(TransferRecord transferRecord)
    {
        squadronMembersTransferred.add(transferRecord);
    }
    
    public void merge(SquadronTransferData source)
    {
        squadronMembersTransferred.addAll(source.squadronMembersTransferred);
    }
    
    public int getTransferCount()
    {
        return squadronMembersTransferred.size();
    }
    
    public List<TransferRecord> getSquadronMembersTransferred()
    {
        return squadronMembersTransferred;
    }
}
