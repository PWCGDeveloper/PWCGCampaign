package pwcg.aar.ui.events.model;

import pwcg.campaign.squadmember.SquadronMemberStatus;

public class PilotStatusEvent  extends AARPilotEvent
{
	private int status = SquadronMemberStatus.STATUS_ACTIVE;
    private int serialNumber;

    public PilotStatusEvent(int squadronId)
    {
        super(squadronId);
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }
}
