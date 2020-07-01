package pwcg.gui.rofmap.brief;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.gui.helper.BriefingMissionFlight;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingContext
{
    private BriefingMissionParameters briefingMissionParameters = new BriefingMissionParameters();
    private Map<Integer, BriefingMissionFlight> briefingMissionHandlers = new HashMap<>();
    private Map<Integer, String> aiFlightsToDisplay = new HashMap<>();
    private int selectedSquadronId = 0;
    private Mission mission;

    public BriefingContext(Mission mission)
    {
        this.mission = mission;
    }

    public void buildBriefingMissions() throws PWCGException
    {
        BriefingMissionHandlerBuilder briefingMissionHandlerBuilder = new BriefingMissionHandlerBuilder(mission);
        briefingMissionHandlers = briefingMissionHandlerBuilder.buildBriefingMissions();
        SquadronMember referencePlayer = mission.getCampaign().findReferencePlayer();
        selectedSquadronId = referencePlayer.getSquadronId();
    }

    public BriefingMissionFlight getActiveBriefingHandler()
    {
        return briefingMissionHandlers.get(selectedSquadronId);
    }

    public void finalizeMission() throws PWCGException
    {
        if (!mission.isFinalized())
        {
            updateMissionBriefingParameters();
            mission.finalizeMission();
            mission.write();

            Campaign campaign = PWCGContext.getInstance().getCampaign();
            campaign.setCurrentMission(mission);
        }
    }

    public void updateMissionBriefingParameters() throws PWCGException 
    {
        if (!mission.isFinalized())
        {
            for (int squadronIdForFlight : briefingMissionHandlers.keySet())
            {
                IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(squadronIdForFlight);
                        
                BriefingMissionFlight briefingMissionHandler = briefingMissionHandlers.get(squadronIdForFlight);
                        
                playerFlight.getWaypointPackage().updateWaypoints(briefingMissionHandler.getBriefingFlightParameters().getWaypointsInBriefing());
                playerFlight.getFlightPlanes().setFuel(briefingMissionHandler.getBriefingFlightParameters().getSelectedFuel());
            }
            
            PWCGContext.getInstance().getCurrentMap().getMissionOptions().getMissionTime().setMissionTime(briefingMissionParameters.getSelectedTime());
        }
    }
    
    public IFlight getSelectedFlight()
    {
        IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(selectedSquadronId);
        return playerFlight;
    }

    public void setAiFlightsToDisplay(Map<Integer, String> aiFlightsToDisplay)
    {
        this.aiFlightsToDisplay = aiFlightsToDisplay;
    }

    public void clearAiFlightsToDisplay()
    {
        aiFlightsToDisplay.clear();
    }
    
    public void changeSelectedFlight(Squadron squadron)
    {
        selectedSquadronId = squadron.getSquadronId();
    }

    public Map<Integer, String> getAiFlightsToDisplay()
    {
        return aiFlightsToDisplay;
    }

    public BriefingMissionParameters getBriefingMissionParameters()
    {
        return briefingMissionParameters;
    }

    public Mission getMission()
    {
        return mission;
    }
}
