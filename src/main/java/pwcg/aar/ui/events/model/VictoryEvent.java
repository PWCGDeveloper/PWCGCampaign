package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Victory;


public class VictoryEvent extends AARPilotEvent
{
    private Victory victory;

    public VictoryEvent(Campaign campaign, Victory victory, int squadronId, int pilotSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, pilotSerialNumber, date, isNewsWorthy);
        this.victory = victory;
    }

    public Victory getVictory()
    {
        return victory;
    }
}
