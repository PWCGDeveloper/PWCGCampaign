package pwcg.gui.rofmap.brief;

import java.util.HashMap;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.builder.BriefingFlightParametersBuilder;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.gui.rofmap.brief.model.BriefingFlightParameters;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingMissionFlightSetBuilder
{
    public static Map<Integer, BriefingFlight> buildBriefingMissions(Mission mission) throws PWCGException
    {
        Map<Integer, BriefingFlight> briefingMissionFlights = new HashMap<>();
        for (IFlight playerFlight : mission.getMissionFlights().getPlayerFlights())
        {
            
            BriefingFlightParameters briefingFlightParameters = buildBriefingFlightParameters(playerFlight);

            BriefingFlight briefingMissionFlight = new BriefingFlight(mission, briefingFlightParameters, playerFlight.getSquadron().getSquadronId());
            briefingMissionFlight.initializeFromMission(playerFlight.getSquadron());

            briefingMissionFlights.put(playerFlight.getSquadron().getSquadronId(), briefingMissionFlight);
        }
        
        return briefingMissionFlights;
    }
    
    private static BriefingFlightParameters buildBriefingFlightParameters(IFlight playerFlight) throws PWCGException
    {     
        BriefingFlightParametersBuilder briefingFlightParametersBuilder = new BriefingFlightParametersBuilder(playerFlight);
        BriefingFlightParameters briefingFlightParameters = briefingFlightParametersBuilder.buildBriefParametersContext();
        return briefingFlightParameters;
    }
}
