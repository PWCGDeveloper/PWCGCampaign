package pwcg.aar.data;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;

public class AARPersonnelLosses
{
    private Map<Integer, SquadronMember> personnelKilled = new HashMap<>();
    private Map<Integer, SquadronMember> personnelCaptured = new HashMap<>();
    private Map<Integer, SquadronMember> personnelMaimed = new HashMap<>();
    private Map<Integer, SquadronMember> personnelWounded = new HashMap<>();
    private Map<Integer, Ace> acesKilled = new HashMap<>();

    
    public AARPersonnelLosses()
    {
    }
    
    public Map<Integer, SquadronMember> getSquadMembersLost()
    {
        Map<Integer, SquadronMember> squadronMembersLost = new HashMap<>();
        squadronMembersLost.putAll(personnelKilled);
        squadronMembersLost.putAll(personnelMaimed);
        squadronMembersLost.putAll(personnelCaptured);
        squadronMembersLost.putAll(acesKilled);
        return squadronMembersLost;
    }

    public void merge(AARPersonnelLosses personnelEvents)
    {
        personnelKilled.putAll(personnelEvents.getPersonnelKilled());
        personnelMaimed.putAll(personnelEvents.getPersonnelMaimed());
        personnelCaptured.putAll(personnelEvents.getPersonnelCaptured());
        personnelWounded.putAll(personnelEvents.getPersonnelWounded());
        acesKilled.putAll(personnelEvents.getAcesKilled());
    }

    public boolean wasAceKilledInMission(Integer aceSerialNumber)
    {
        return acesKilled.containsKey(aceSerialNumber);
    }

    public Map<Integer, Ace> getAcesKilled()
    {
        return acesKilled;
    }

    public Map<Integer, SquadronMember> getPersonnelKilled()
    {
        return personnelKilled;
    }

    public Map<Integer, SquadronMember> getPersonnelMaimed()
    {
        return personnelMaimed;
    }

    public Map<Integer, SquadronMember> getPersonnelCaptured()
    {
        return personnelCaptured;
    }

    public Map<Integer, SquadronMember> getPersonnelWounded()
    {
        return personnelWounded;
    }

    public void mergePersonnelKilled(Map<Integer, SquadronMember> squadMembersKilled)
    {
        this.personnelKilled.putAll(squadMembersKilled);
    }

    public void mergePersonnelMaimed(Map<Integer, SquadronMember> squadMembersMaimed)
    {
        this.personnelMaimed.putAll(squadMembersMaimed);
    }


    public void mergePersonnelWounded(Map<Integer, SquadronMember> squadMembersWounded)
    {
        this.personnelWounded.putAll(squadMembersWounded);
    }
 
    public void mergePersonnelCaptured(Map<Integer, SquadronMember> squadMembersCaptured)
    {
        this.personnelCaptured.putAll(squadMembersCaptured);
    }

    public void mergeAcesKilled(Map<Integer, Ace> acesKilled)
    {
        this.acesKilled.putAll(acesKilled);
    }

    public void addPersonnelKilled(SquadronMember campaignMemberKilled)
    {
        this.personnelKilled.put(campaignMemberKilled.getSerialNumber(), campaignMemberKilled);
    }

    public void addPersonnelMaimed(SquadronMember campaignMemberMaimed)
    {
        this.personnelMaimed.put(campaignMemberMaimed.getSerialNumber(), campaignMemberMaimed);
    }

    public void addPersonnelWounded(SquadronMember campaignMemberWounded)
    {
        this.personnelWounded.put(campaignMemberWounded.getSerialNumber(), campaignMemberWounded);
    }

    public void addPersonnelCaptured(SquadronMember campaignMemberCaptured)
    {
        this.personnelCaptured.put(campaignMemberCaptured.getSerialNumber(), campaignMemberCaptured);
    }

    public void addAcesKilled(Ace aceKilled)
    {
        this.acesKilled.put(aceKilled.getSerialNumber(), aceKilled);
    }
}
