package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;

public class MedalEvent extends AARPilotEvent
{
    private String medal = "";

    public MedalEvent(Campaign campaign, String medal, int squadronId, int pilotSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, pilotSerialNumber, date, isNewsWorthy);
        this.medal = medal;
    }

    public String getMedal()
    {
        return medal;
    }
}
