package pwcg.aar.outofmission.phase2.resupply;

import java.util.ArrayList;
import java.util.List;

public class EquipmentResupplyData
{
    private List<EquipmentResupplyRecord> planesResupplied = new ArrayList<>();
    
    public void addEquipmentResupplyRecord(EquipmentResupplyRecord equipmentResupplyRecord)
    {
        planesResupplied.add(equipmentResupplyRecord);
    }
    
    public void merge(EquipmentResupplyData source)
    {
        planesResupplied.addAll(source.planesResupplied);
    }

    public List<EquipmentResupplyRecord> getEquipmentresuppliedToSquadron(int squadronId)
    {
        List<EquipmentResupplyRecord> squadronMembersTransferredIn = new ArrayList<>();
        for (EquipmentResupplyRecord squadronEquipmentResupplyRecord : planesResupplied)
        {
            if (squadronEquipmentResupplyRecord.getTransferTo() == squadronId)
            {
                squadronMembersTransferredIn.add(squadronEquipmentResupplyRecord);
            }
        }
        return squadronMembersTransferredIn;
    }
    
    public int getTransferCount()
    {
        return planesResupplied.size();
    }
    
    public List<EquipmentResupplyRecord> getEquipmentResupplied()
    {
        return planesResupplied;
    }
}
