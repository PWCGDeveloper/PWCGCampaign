package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;

public class MaxFighterFlightCalculator
{
    private Campaign campaign;
    private Flight playerFlight;
    
    public MaxFighterFlightCalculator (Campaign campaign, Flight playerFlight)
    {
        this.campaign = campaign;
        this.playerFlight = playerFlight;
    }
    
    public int getMaxFighterFlightsForMission() throws PWCGException
    {
        int numFighterFlightsToKeep = 0;
        if (campaign.isFighterCampaign() && playerFlight.isFighterFlight())
        {
            numFighterFlightsToKeep = calcNumFighterFlightsToKeepForFighterMission();
        }
        else
        {
            numFighterFlightsToKeep = calcNumFighterFlightsToKeepForGroundMission();
        }
        return reduceFlightsForPeriodOfLowActivity(numFighterFlightsToKeep);
    }

    private int calcNumFighterFlightsToKeepForFighterMission() throws PWCGException
    {
        int maxFighterToKeepIfFighterCampaign = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AiFighterFlightsForFighterCampaignMaxKey);
        if (PWCGContextManager.getInstance().getCurrentMap().getMapIdentifier() == FrontMapIdentifier.BODENPLATTE_MAP)
        {
            int additionalFighterFLightsForWestFront = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AiAddidionalFighterFlightsForWestFrontCampaignKey);
            maxFighterToKeepIfFighterCampaign += additionalFighterFLightsForWestFront;
        }

        int minFighterFlightsToKeep = 2;
        int maxFighterFlightsToAdd = maxFighterToKeepIfFighterCampaign - minFighterFlightsToKeep;
        if (maxFighterFlightsToAdd < 0) 
        {
            maxFighterFlightsToAdd = 0;
        }
        int numFighterFlightsToKeep =  minFighterFlightsToKeep + RandomNumberGenerator.getRandom(maxFighterFlightsToAdd)+1;
        return numFighterFlightsToKeep;
    }

    private int calcNumFighterFlightsToKeepForGroundMission() throws PWCGException
    {
        int maxFighterToKeepIfGroundCampaign = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AiFighterFlightsForGroundCampaignMaxKey);
        int numFighterFlightsToKeep =  RandomNumberGenerator.getRandom(maxFighterToKeepIfGroundCampaign)+1;
        return numFighterFlightsToKeep;
    }

    private int reduceFlightsForPeriodOfLowActivity(int numFighterFlightsToKeep) throws PWCGException
    {
        if (numFighterFlightsToKeep == 0)
        {
            return numFighterFlightsToKeep;
        }
        
        if(campaign.getDate().before(DateUtils.getDateYYYYMMDD("19161001")))
        {
            int oddsNoFighterEarly = RandomNumberGenerator.getRandom(100);
            if (oddsNoFighterEarly < 70)
            {
                numFighterFlightsToKeep = 0;
            }
            else if (oddsNoFighterEarly < 90)
            {
                numFighterFlightsToKeep = numFighterFlightsToKeep - 1;
            }
        }
        return numFighterFlightsToKeep;
    }
}
