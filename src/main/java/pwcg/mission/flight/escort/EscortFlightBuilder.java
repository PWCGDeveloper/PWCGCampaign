package pwcg.mission.flight.escort;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;

public class EscortFlightBuilder
{
    public void addPossibleEscort(Campaign campaign, Mission mission, Flight escortedFlight) throws PWCGException 
    {
        Flight escortFlight = null;
        if (escortedFlight.isPlayerFlight())
        {
            escortFlight = createEscortForPlayerFlight(escortedFlight);
        }
        else if (!campaign.getCampaignData().isCoop())
        {
            escortFlight = addPossibleEscortForEnemyFlight(campaign, mission, escortedFlight);
        }

        if (escortFlight != null)
        {
            escortedFlight.addLinkedUnit(escortFlight);
        }
    }

    private Flight createEscortForPlayerFlight(Flight escortedFlight) throws PWCGException 
    {
        PlayerEscortBuilder playerEscortBuilder = new PlayerEscortBuilder();
        Flight escortForPlayerFlight = playerEscortBuilder.createEscortForPlayerFlight(escortedFlight);
        return escortForPlayerFlight;
    }

    private Flight addPossibleEscortForEnemyFlight(Campaign campaign, Mission mission, Flight escortedFlight) throws PWCGException, PWCGException
    {
        if (!campaign.getCampaignData().isCoop())
        {
            if (mission.getMissionFlightBuilder().hasPlayerFighterFlightType())
            {
                if (campaign.isFighterCampaign())
                {
                    int escortedOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.IsEscortedOddsKey);
                    int escortedDiceRoll = RandomNumberGenerator.getRandom(100);        
                    if (escortedDiceRoll < escortedOdds)
                    {
                        VirtualEscortFlightBuilder virtualEscortFlightBuilder = new VirtualEscortFlightBuilder();
                        return virtualEscortFlightBuilder.createVirtualEscortFlight(escortedFlight);
                    }
                }
            }
        }
        
        return null;
    }

}
