package pwcg.aar.ui.events.model;

import pwcg.campaign.squadmember.SquadronMemberStatus;

public class PilotStatusEvent  extends AARPilotEvent
{
	private int status = SquadronMemberStatus.STATUS_ACTIVE;
	
	public PilotStatusEvent ()
	{
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
