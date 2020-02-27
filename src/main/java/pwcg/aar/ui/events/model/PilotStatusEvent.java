package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMemberStatus;

public class PilotStatusEvent  extends AARPilotEvent
{
	private int status = SquadronMemberStatus.STATUS_ACTIVE;

    public PilotStatusEvent(Campaign campaign, int status, int squadronId, int pilotSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, pilotSerialNumber, date, isNewsWorthy);
        this.status = status;
    }

	public int getStatus() {
		return status;
	}
}
