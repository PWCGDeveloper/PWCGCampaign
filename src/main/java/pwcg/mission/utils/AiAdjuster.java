package pwcg.mission.utils;

import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.group.virtual.VirtualWaypoint;

public class AiAdjuster
{    
    public AiAdjuster()
    {
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
