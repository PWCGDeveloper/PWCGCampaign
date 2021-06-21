package pwcg.aar.data;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.squadmember.SquadronMember;

public class AARPersonnelLosses
{
    private Map<Integer, SquadronMember> personnelKilled = new HashMap<>();
    private Map<Integer, SquadronMember> personnelCaptured = new HashMap<>();
    private Map<Integer, SquadronMember> personnelMaimed = new HashMap<>();
    private Map<Integer, SquadronMember> personnelWounded = new HashMap<>();
    private Map<Integer, SquadronMember> personnelTransferredHome = new HashMap<>();
    private Map<Integer, SquadronMember> acesKilled = new HashMap<>();

    
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
        squadronMembersLost.putAll(personnelTransferredHome);
        return squadronMembersLost;
    }

    public void merge(AARPersonnelLosses personnelEvents)
    {
        personnelKilled.putAll(personnelEvents.getPersonnelKilled());
        personnelMaimed.putAll(personnelEvents.getPersonnelMaimed());
        personnelCaptured.putAll(personnelEvents.getPersonnelCaptured());
        personnelWounded.putAll(personnelEvents.getPersonnelWounded());
        acesKilled.putAll(personnelEvents.getAcesKilled());
        personnelTransferredHome.putAll(personnelEvents.getPersonnelTransferredHome());
    }

    public boolean wasAceKilledInMission(Integer aceSerialNumber)
    {
        return acesKilled.containsKey(aceSerialNumber);
    }

    public Map<Integer, SquadronMember> getAcesKilled()
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

    public Map<Integer, SquadronMember> getPersonnelTransferredHome()
    {
        return personnelTransferredHome;
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

    public void addAcesKilled(SquadronMember aceKilled)
    {
        this.acesKilled.put(aceKilled.getSerialNumber(), aceKilled);
    }
    
    public void addPersonnelTransferredHome(SquadronMember campaignMemberTransferred)
    {
        this.personnelTransferredHome.put(campaignMemberTransferred.getSerialNumber(), campaignMemberTransferred);
    }
    
    public Map<Integer, SquadronMember> getAllInjured()
    {
        Map<Integer, SquadronMember> allInjured = new HashMap<>();
        allInjured.putAll(personnelWounded);
        allInjured.putAll(personnelMaimed);
        allInjured.putAll(personnelKilled);
        allInjured.putAll(acesKilled);
        return allInjured;
    }

    public boolean pilotisWoundedToday(SquadronMember squadronMember)
    {
        Map<Integer, SquadronMember> allInjured = getAllInjured();
        return allInjured.containsKey(squadronMember.getSerialNumber());
    }
}
