package pwcg.mission.flight.escort;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class NeedsEscortDecider
{
    public boolean needsEscort(Mission mission, IFlight escortedFlight) throws PWCGException
    {
        Campaign campaign = mission.getCampaign();
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE ||
            campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COOP)
        {
            if (escortedFlight.isPlayerFlight())
            {
                return playerNeedsEscort(mission, escortedFlight);
            }
            else
            {
                return aiNeedsEscort(mission, escortedFlight);
            }
        }
        
        return false;
    }
    
    private boolean playerNeedsEscort(Mission mission, IFlight escortedFlight) throws PWCGException, PWCGException
    {
        if (escortedFlight.getFlightInformation().isFighterMission())
        {
            return false;
        }
        
        if (!FlightTypes.isFlightNeedsEscort(escortedFlight.getFlightType()))
        {
            return false;
        }
        
        if (escortedFlight.getMission().isNightMission())
        {
            return false;
        }

        return true;
    }
    
    private boolean aiNeedsEscort(Mission mission, IFlight escortedFlight) throws PWCGException, PWCGException
    {
        Campaign campaign = mission.getCampaign();
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE ||
            campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COOP)
        {
            if (mission.getMissionFlightBuilder().hasPlayerFighterFlightType())
            {
                if (!escortedFlight.getFlightInformation().isEscortedByPlayerFlight())
                {
                    if (campaign.isFighterCampaign())
                    {
                        int escortedOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.IsEscortedOddsKey);
                        int escortedDiceRoll = RandomNumberGenerator.getRandom(100);        
                        if (escortedDiceRoll < escortedOdds)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

}
