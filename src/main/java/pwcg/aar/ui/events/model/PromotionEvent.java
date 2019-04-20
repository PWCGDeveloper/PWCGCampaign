package pwcg.aar.ui.events.model;

public class PromotionEvent extends AARPilotEvent
{
    private String oldRank = "";
    private String newRank = "";
    private String promotingGeneral = "";
    private int serialNumber = 0;

    public PromotionEvent(int squadronId, int serialNumber)
    {
        super(squadronId);
        this.serialNumber = serialNumber;
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getNewRank()
    {
        return newRank;
    }

    public void setNewRank(String newRank)
    {
        this.newRank = newRank;
    }

    public String getOldRank()
    {
        return oldRank;
    }

    public void setOldRank(String oldRank)
    {
        this.oldRank = oldRank;
    }    

    public String getPromotingGeneral()
    {
        return promotingGeneral;
    }

    public void setPromotingGeneral(String promotingGeneral)
    {
        this.promotingGeneral = promotingGeneral;
    }
}
