package pwcg.aar.ui.events.model;

import pwcg.campaign.squadmember.SquadronMember;

public class AARPilotEvent extends AAREvent
{
	private String squadron = "";
	private SquadronMember  pilot = null;

	public String getSquadron() {
		return squadron;
	}

	public void setSquadron(String squadron) {
		this.squadron = squadron;
	}
	
	public SquadronMember getPilot() 
	{
		return pilot;
	}

	public void setPilot(SquadronMember pilot) {
		this.pilot = pilot;
	}
}
