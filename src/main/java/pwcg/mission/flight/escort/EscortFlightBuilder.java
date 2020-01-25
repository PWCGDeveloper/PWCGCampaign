package pwcg.mission.flight.escort;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class EscortFlightBuilder
{
    public void addEscort(Mission mission, IFlight escortedFlight) throws PWCGException 
    {
        if (mission.isNightMission())
        {
            return;
        }
        
        Campaign campaign = mission.getCampaign();
        IFlight escortFlight = null;
        if (escortedFlight.isPlayerFlight())
        {
            escortFlight = createEscortForPlayerFlight(escortedFlight);
        }
        else if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE ||
                 campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COOP)
        {
            escortFlight = createEscortForAiFlight(mission, escortedFlight);
        }
        
        if (escortFlight != null)
        {
            escortedFlight.getLinkedFlights().addLinkedFlight(escortFlight);
        }
    }

    private IFlight createEscortForPlayerFlight(IFlight escortedFlight) throws PWCGException 
    {
        PlayerEscortBuilder playerEscortBuilder = new PlayerEscortBuilder();
        IFlight escortForPlayerFlight = playerEscortBuilder.createEscortForPlayerFlight(escortedFlight);
        return escortForPlayerFlight;
    }


    private IFlight createEscortForAiFlight(Mission mission, IFlight escortedFlight) throws PWCGException 
    {
        VirtualEscortFlightBuilder virtualEscortFlightBuilder = new VirtualEscortFlightBuilder();
        IFlight escortForAiFlight = virtualEscortFlightBuilder.createVirtualEscortFlight(escortedFlight);
        return escortForAiFlight;
    }
}
