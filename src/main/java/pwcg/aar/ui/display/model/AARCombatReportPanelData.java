package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.ui.events.model.AAREvent;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.mission.data.MissionHeader;

public class AARCombatReportPanelData
{
    private MissionHeader missionHeader;
    private Map<Integer, CrewMember> crewsInMission = new HashMap<>();
    private Map<Integer, CrewMemberStatusEvent> squadronMembersLostInMission = new HashMap<>();
    private Map<Integer, PlaneStatusEvent> squadronPlanesLostInMission = new HashMap<>();
    private List<VictoryEvent> victoriesForCrewMembersInMission = new ArrayList<>();
    private List<ClaimDeniedEvent> claimsDenied = new ArrayList<>();
    
    public List<AAREvent> getAllEvents()
    {
        List<AAREvent> allEvents = new ArrayList<>();
        allEvents.addAll(victoriesForCrewMembersInMission);
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

    public Map<Integer, CrewMember> getCrewsInMission()
    {
        return crewsInMission;
    }
    
    public Map<Integer, CrewMemberStatusEvent> getCrewMembersLostInMission()
    {
        return squadronMembersLostInMission;
    }

    public Map<Integer, PlaneStatusEvent> getSquadronPlanesLostInMission()
    {
        return squadronPlanesLostInMission;
    }

    public List<VictoryEvent> getVictoriesForCrewMembersInMission()
    {
        return victoriesForCrewMembersInMission;
    }

    public List<ClaimDeniedEvent> getClaimsDenied()
    {
        return claimsDenied;
    }

    public void addClaimsDenied(List<ClaimDeniedEvent> sourceClaimDeniedEvents)
    {
        claimsDenied.addAll(sourceClaimDeniedEvents);
    }

    public void addCrewMembersInMission(CrewMembers crewsInMissionFromPlayerSquadron)
    {
        crewsInMission.putAll(crewsInMissionFromPlayerSquadron.getCrewMemberCollection());
    }

    public void addCrewMemberLostInMission(CrewMemberStatusEvent crewMemberLostEvent)
    {
        squadronMembersLostInMission.put(crewMemberLostEvent.getCrewMemberSerialNumber(), crewMemberLostEvent);
    }

    public void addVictoryForCrewMembers(VictoryEvent victory)
    {
        victoriesForCrewMembersInMission.add(victory);
        
    }

    public void addPlaneLostInMission(PlaneStatusEvent planeLostEvent)
    {
        squadronPlanesLostInMission.put(planeLostEvent.getPlaneSerialNumber(), planeLostEvent);        
    }
}
