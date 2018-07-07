package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.AAREvent;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.mission.data.MissionHeader;

public class AARCombatReportPanelData
{
    private MissionHeader missionHeader;
    private Map<Integer, SquadronMember> crewsInMission = new HashMap<>();
    private Map<Integer, PilotStatusEvent> squadronMembersLostInMission = new HashMap<>();
    private Map<Integer, PlaneStatusEvent> squadronPlanesLostInMission = new HashMap<>();
    private List<VictoryEvent> victoriesForSquadronMembersInMission = new ArrayList<>();
    private List<ClaimDeniedEvent> claimsDenied = new ArrayList<>();
    
    public List<AAREvent> getAllEvents()
    {
        List<AAREvent> allEvents = new ArrayList<>();
        allEvents.addAll(victoriesForSquadronMembersInMission);
        allEvents.addAll(claimsDenied);
        allEvents.addAll(squadronMembersLostInMission.values());
        
        return allEvents;
    }

    public MissionHeader getMissionHeader()
    {
        return missionHeader;
    }

    public void setMissionAARHeader(MissionHeader missionHeader)
    {
        this.missionHeader = missionHeader;
    }

    public Map<Integer, SquadronMember> getCrewsInMission()
    {
        return crewsInMission;
    }
    
    public Map<Integer, PilotStatusEvent> getSquadronMembersLostInMission()
    {
        return squadronMembersLostInMission;
    }

    public Map<Integer, PlaneStatusEvent> getSquadronPlanesLostInMission()
    {
        return squadronPlanesLostInMission;
    }

    public List<VictoryEvent> getVictoriesForSquadronMembersInMission()
    {
        return victoriesForSquadronMembersInMission;
    }

    public List<ClaimDeniedEvent> getClaimsDenied()
    {
        return claimsDenied;
    }

    public void addClaimsDenied(List<ClaimDeniedEvent> sourceClaimDeniedEvents)
    {
        claimsDenied.addAll(sourceClaimDeniedEvents);
    }

    public void addPilotsInMission(Map<Integer, SquadronMember> crewsInMissionFromPlayerSquadron)
    {
        crewsInMission.putAll(crewsInMissionFromPlayerSquadron);
    }

    public void addPilotLostInMission(PilotStatusEvent pilotLostEvent)
    {
        squadronMembersLostInMission.put(pilotLostEvent.getPilot().getSerialNumber(), pilotLostEvent);
    }

    public void addVictoryForSquadronMembers(VictoryEvent victory)
    {
        victoriesForSquadronMembersInMission.add(victory);
        
    }

    public void addPlaneLostInMission(PlaneStatusEvent planeLostEvent)
    {
        squadronPlanesLostInMission.put(planeLostEvent.getPlane().getSerialNumber(), planeLostEvent);        
    }
}
