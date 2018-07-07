package pwcg.aar.outofmission.phase2.resupply;

public class AARResupplyData
{
    private SquadronTransferData acesTransferred = new SquadronTransferData();
    private SquadronTransferData squadronTransferData = new SquadronTransferData();
    private EquipmentResupplyData equipmentResupplyData = new EquipmentResupplyData();

    public SquadronTransferData getAcesTransferred()
    {
        return acesTransferred;
    }

    public void setAcesTransferred(SquadronTransferData acesTransferred)
    {
        this.acesTransferred = acesTransferred;
    }

    public SquadronTransferData getSquadronTransferData()
    {
        return squadronTransferData;
    }

    public void setSquadronTransferData(SquadronTransferData squadronTransferData)
    {
        this.squadronTransferData = squadronTransferData;
    }

    public void merge(AARResupplyData transferData)
    {
        acesTransferred.merge(transferData.getAcesTransferred());
        squadronTransferData.merge(transferData.getSquadronTransferData());        
        equipmentResupplyData.merge(transferData.getEquipmentResupplyData());        
    }

    public void setEquipmentResupplyData(EquipmentResupplyData equipmentResupplyData)
    {
        this.equipmentResupplyData = equipmentResupplyData;        
    }

    public EquipmentResupplyData getEquipmentResupplyData()
    {
        return equipmentResupplyData;
    }
}
