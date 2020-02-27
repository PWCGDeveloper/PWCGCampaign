package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;

public class AceKilledEvent extends AARPilotEvent
{
    private String status = "";

    public AceKilledEvent(Campaign campaign, String status, int squadronId, int pilotSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, pilotSerialNumber, date, isNewsWorthy);
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
}
