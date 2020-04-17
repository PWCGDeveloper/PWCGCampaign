package pwcg.gui.rofmap.brief;

import java.util.HashMap;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.gui.helper.BriefingMissionFlight;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingMissionHandlerBuilder
{
    private Mission mission;
    private Map<Integer, BriefingMissionFlight> briefingMissionHandlers = new HashMap<>();

    public BriefingMissionHandlerBuilder(Mission mission)
    {
        this.mission = mission;
    }
    
    public Map<Integer, BriefingMissionFlight> buildBriefingMissions() throws PWCGException
    {
        for (IFlight playerFlight : mission.getMissionFlightBuilder().getPlayerFlights())
        {
            BriefingMissionFlight briefingMissionHandler = new BriefingMissionFlight(mission, playerFlight);
            briefingMissionHandler.initializeFromMission(playerFlight.getSquadron());
            briefingMissionHandler.loadMissionParams(playerFlight);
            briefingMissionHandlers.put(playerFlight.getSquadron().getSquadronId(), briefingMissionHandler);
        }
        return briefingMissionHandlers;
    }
}
