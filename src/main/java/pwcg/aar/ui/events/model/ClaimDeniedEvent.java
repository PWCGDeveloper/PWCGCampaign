package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;

public class ClaimDeniedEvent extends AARPilotEvent
{
	private String aircraftType = "";
	
    public ClaimDeniedEvent(Campaign campaign, String aircraftType, int squadronId, int pilotSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, squadronId, pilotSerialNumber, date, isNewsWorthy);
        this.aircraftType = aircraftType;
    }

	public String getType() {
		return aircraftType;
	}
}
