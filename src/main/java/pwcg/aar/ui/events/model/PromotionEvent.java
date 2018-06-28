package pwcg.aar.ui.events.model;

public class PromotionEvent extends AARPilotEvent
{
    private String oldRank = "";
    private String newRank = "";
    private String promotingGeneral = "";

    public PromotionEvent()
    {
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
