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
        int serialNumber = transferRecord.getSquadronMember().getSerialNumber();
        if (!squadronMembersTransferred.containsKey(serialNumber))
        {
            List<TransferRecord> newTransfeRecordSet = new ArrayList<>();
            squadronMembersTransferred.put(serialNumber, newTransfeRecordSet);
        }
        else
        {
            System.out.println("Dup transfer of squadron member" + serialNumber);
        }
        
        List<TransferRecord> transfeRecordSet = squadronMembersTransferred.get(serialNumber);
        transfeRecordSet.add(transferRecord);
    }
    
    public void merge(SquadronTransferData source)
    {
        for (TransferRecord transferRecord : source.getSquadronMembersTransferred())
        {
            addTransferRecord(transferRecord);
        }
    }
    
    public int getTransferCount()
    {
        return squadronMembersTransferred.size();
    }
    
    public List<TransferRecord> getSquadronMembersTransferred()
    {
        List<TransferRecord> allTransferrecords = new ArrayList<>();
        for (List<TransferRecord> transferRecordsForPilot : squadronMembersTransferred.values())
        {
            for (TransferRecord transferRecord : transferRecordsForPilot)
            {
                allTransferrecords.add(transferRecord);
            }
        }

        return allTransferrecords;
    }
}
