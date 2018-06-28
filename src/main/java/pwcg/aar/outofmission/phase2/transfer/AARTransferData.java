package pwcg.aar.outofmission.phase2.transfer;

public class AARTransferData
{
    private SquadronTransferData acesTransferred = new SquadronTransferData();
    private SquadronTransferData squadronTransferData = new SquadronTransferData();

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

    public void merge(AARTransferData transferData)
    {
        acesTransferred.merge(transferData.getAcesTransferred());
        squadronTransferData.merge(transferData.getSquadronTransferData());        
    }
}
