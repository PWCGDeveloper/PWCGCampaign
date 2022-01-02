package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;

public class ClaimDeniedEvent extends AARCrewMemberEvent
{
	private String aircraftType = "";
	
    public ClaimDeniedEvent(Campaign campaign, String aircraftType, int squadronId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, crewMemberSerialNumber, date, isNewsWorthy);
        this.aircraftType = aircraftType;
    }

	public String getType() {
		return aircraftType;
	}
}
