package pwcg.campaign.resupply.personnel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SquadronTransferData
{
    private Map<Integer, List<TransferRecord>> squadronMembersTransferred = new HashMap<>();
    
    public void addTransferRecord(TransferRecord transferRecord)
    {
        int serialNumber = transferRecord.getCrewMember().getSerialNumber();
        if (!squadronMembersTransferred.containsKey(serialNumber))
        {
            List<TransferRecord> newTransfeRecordSet = new ArrayList<>();
            squadronMembersTransferred.put(serialNumber, newTransfeRecordSet);
        }
        
        List<TransferRecord> transfeRecordSet = squadronMembersTransferred.get(serialNumber);
        transfeRecordSet.add(transferRecord);
    }
    
    public void merge(SquadronTransferData source)
    {
        for (TransferRecord transferRecord : source.getCrewMembersTransferred())
        {
            addTransferRecord(transferRecord);
        }
    }
    
    public int getTransferCount()
    {
        return squadronMembersTransferred.size();
    }
    
    public List<TransferRecord> getCrewMembersTransferred()
    {
        List<TransferRecord> allTransferrecords = new ArrayList<>();
        for (List<TransferRecord> transferRecordsForCrewMember : squadronMembersTransferred.values())
        {
            for (TransferRecord transferRecord : transferRecordsForCrewMember)
            {
                allTransferrecords.add(transferRecord);
            }
        }

        return allTransferrecords;
    }
}
