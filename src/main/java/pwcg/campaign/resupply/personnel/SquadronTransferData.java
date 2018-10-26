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

    public List<TransferRecord> getSquadronMembersTransferredToSquadron(int squadronId)
    {
        List<TransferRecord> squadronMembersTransferredIn = new ArrayList<>();
        for (TransferRecord squadronTransferRecord : squadronMembersTransferred)
        {
            if (squadronTransferRecord.getTransferTo() == squadronId)
            {
                squadronMembersTransferredIn.add(squadronTransferRecord);
            }
        }
        return squadronMembersTransferredIn;
    }

    public List<TransferRecord> getSquadronMembersTransferredFromSquadron(Integer squadronId)
    {
        List<TransferRecord> squadronMembersTransferredOut = new ArrayList<>();
        for (TransferRecord squadronTransferRecord : squadronMembersTransferred)
        {
            if (squadronTransferRecord.getTransferFrom() == squadronId)
            {
                squadronMembersTransferredOut.add(squadronTransferRecord);
            }
        }
        return squadronMembersTransferredOut;
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
