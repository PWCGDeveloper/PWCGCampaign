package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;

public class PromotionEvent extends AARPilotEvent
{
    private String oldRank = "";
    private String newRank = "";
    private String promotingGeneral = "";

    public PromotionEvent(Campaign campaign, String oldRank, String newRank, String promotingGeneral, int squadronId, int pilotSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, pilotSerialNumber, date, isNewsWorthy);
        this.oldRank = oldRank;
        this.newRank = newRank;
        this.promotingGeneral = promotingGeneral;
    }

    public String getNewRank()
    {
        return newRank;
    }

    public String getOldRank()
    {
        return oldRank;
    }

    public String getPromotingGeneral()
    {
        return promotingGeneral;
    }
}
