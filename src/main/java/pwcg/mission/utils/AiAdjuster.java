package pwcg.mission.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;

public class AiAdjuster
{
    private Campaign campaign;
    
    public AiAdjuster(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void adjustAI(Mission mission) throws PWCGException
    {
        for (Flight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            for (PlaneMCU plane: flight.getAiPlanes())
            {
                adjustAi(plane);
            }
        }
    }

    private void adjustAi(PlaneMCU plane) throws PWCGException
    {

        int aiSkillValue = plane.getAiLevel().getAiSkillLevel();
        if (plane.isPrimaryRole(Role.ROLE_FIGHTER))
        {
            int fighterAISkillAdjustment = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.FighterAISkillAdjustmentKey);
            aiSkillValue += fighterAISkillAdjustment;
        }
        else 
        {
            int bomberAISkillAdjustment = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.BomberAISkillAdjustmentKey);
            aiSkillValue += bomberAISkillAdjustment;
        }
        
        if (aiSkillValue < AiSkillLevel.NOVICE.getAiSkillLevel()) 
        {
            aiSkillValue = AiSkillLevel.NOVICE.getAiSkillLevel();
        }
        
        if (aiSkillValue > AiSkillLevel.ACE.getAiSkillLevel()) 
        {
            aiSkillValue = AiSkillLevel.ACE.getAiSkillLevel();
        }
        
        AiSkillLevel aiSkillLevel = AiSkillLevel.createAiSkilLLevel(aiSkillValue);
        plane.setAiLevel(aiSkillLevel);
    }
}
