package pwcg.campaign.resupply.equipment;

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

    public List<EquipmentResupplyRecord> getEquipmentResuppliedToSquadron(int squadronId)
    {
        List<EquipmentResupplyRecord> equipmentResupplyRecords = new ArrayList<>();
        for (EquipmentResupplyRecord equipmentResupply : planesResupplied)
        {
            if (equipmentResupply.getTransferTo() == squadronId)
            {
                equipmentResupplyRecords.add(equipmentResupply);
            }
        }
        return equipmentResupplyRecords;
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
