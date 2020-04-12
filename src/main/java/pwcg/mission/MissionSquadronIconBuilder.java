package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuIcon;

public class MissionSquadronIconBuilder
{
    private List<McuIcon> squadronIcons = new ArrayList<>();
    private Campaign campaign;

    public MissionSquadronIconBuilder(Campaign campaign) {
        this.campaign = campaign;
    }

    public void createSquadronIcons(List<IFlight> playerFlights) throws PWCGException
    {
        for (IFlight playerFlight : playerFlights)
        {
            createSquadronIcon(playerFlight);
        }
    }

    public void createSquadronIcon(IFlight playerFlight) throws PWCGException
    {
        McuIcon icon = new McuIcon(playerFlight, campaign.getDate());
        squadronIcons.add(icon);
    }

	public void write(BufferedWriter writer) throws PWCGIOException
	{
		for (McuIcon icon : squadronIcons)
		{
			icon.write(writer);
		}
	}
}
