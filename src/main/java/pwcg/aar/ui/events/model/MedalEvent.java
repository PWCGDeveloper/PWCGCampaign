package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;

public class MedalEvent extends AARCrewMemberEvent
{
    private String medal = "";

    public MedalEvent(Campaign campaign, String medal, int squadronId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, crewMemberSerialNumber, date, isNewsWorthy);
        this.medal = medal;
    }

    public String getMedal()
    {
        return medal;
    }
}
