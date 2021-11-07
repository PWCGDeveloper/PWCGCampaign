package pwcg.mission.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.group.virtual.VirtualWaypoint;

public class AiAdjuster
{
    private Campaign campaign;
    
    public AiAdjuster(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void adjustAI(Mission mission) throws PWCGException
    {
        adjustAIForRealFlights(mission);
        adjustAIForVirtualMissions(mission);
    }

    private void adjustAIForRealFlights(Mission mission) throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane: flight.getFlightPlanes().getAiPlanes())
            {
                adjustAi(plane);
            }
        }
    }
    
    private void adjustAIForVirtualMissions(Mission mission) throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            if (flight.getFlightInformation().isVirtual())
            {
                for (VirtualWaypoint virtualWaypoint : flight.getVirtualWaypointPackage().getVirtualWaypoints())
                {
                    for (PlaneMcu plane: virtualWaypoint.getVwpPlanes().getAllPlanes())
                    {
                        adjustAi(plane);
                    }
                }
            }
        }
    }

    private void adjustAi(PlaneMcu plane) throws PWCGException
    {
        int aiSkillValue = plane.getAiLevel().getAiSkillLevel();
        if (plane.isPrimaryRole(PwcgRole.ROLE_FIGHTER) || plane.isNovice() == false)
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
