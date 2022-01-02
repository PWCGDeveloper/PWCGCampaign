package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.Victory;


public class VictoryEvent extends AARCrewMemberEvent
{
    private Victory victory;

    public VictoryEvent(Campaign campaign, Victory victory, int squadronId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, crewMemberSerialNumber, date, isNewsWorthy);
        this.victory = victory;
    }

    public Victory getVictory()
    {
        return victory;
    }
}
