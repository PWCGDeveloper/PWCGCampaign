package pwcg.aar.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.GreatAce;
import pwcg.core.exception.PWCGException;

public class AARPersonnelLosses
{
    private Map<Integer, CrewMember> personnelKilled = new HashMap<>();
    private Map<Integer, CrewMember> personnelCaptured = new HashMap<>();
    private Map<Integer, CrewMember> personnelMaimed = new HashMap<>();
    private Map<Integer, CrewMember> personnelWounded = new HashMap<>();
    private Map<Integer, CrewMember> personnelTransferredHome = new HashMap<>();

    
    public AARPersonnelLosses()
    {
    }
    
    public Map<Integer, CrewMember> getSquadMembersLostAndInjured()
    {
        Map<Integer, CrewMember> squadronMembersLost = new HashMap<>();
        squadronMembersLost.putAll(personnelKilled);
        squadronMembersLost.putAll(personnelCaptured);
        squadronMembersLost.putAll(personnelMaimed);
        squadronMembersLost.putAll(personnelTransferredHome);
        return squadronMembersLost;
    }

    public void merge(AARPersonnelLosses personnelEvents)
    {
        personnelKilled.putAll(personnelEvents.getPersonnelKilled());
        personnelMaimed.putAll(personnelEvents.getPersonnelMaimed());
        personnelCaptured.putAll(personnelEvents.getPersonnelCaptured());
        personnelWounded.putAll(personnelEvents.getPersonnelWounded());
        personnelTransferredHome.putAll(personnelEvents.getPersonnelTransferredHome());
    }

    public Map<Integer, CrewMember> getAcesKilled(Campaign campaign) throws PWCGException
    {
        Map<Integer, CrewMember> acesLost = new HashMap<>();
        for (CrewMember crewMember : getSquadMembersLost())
        {
            if (GreatAce.isGreatAce(campaign, crewMember))
            {
                acesLost.put(crewMember.getSerialNumber(), crewMember);
            }
        }
        return acesLost;
    }
    
    private List<CrewMember> getSquadMembersLost()
    {
        Map<Integer, CrewMember> squadronMembersLost = new HashMap<>();
        squadronMembersLost.putAll(personnelKilled);
        squadronMembersLost.putAll(personnelCaptured);
        return new ArrayList<>(squadronMembersLost.values());
    }

    public Map<Integer, CrewMember> getPersonnelKilled()
    {
        return personnelKilled;
    }

    public Map<Integer, CrewMember> getPersonnelMaimed()
    {
        return personnelMaimed;
    }

    public Map<Integer, CrewMember> getPersonnelCaptured()
    {
        return personnelCaptured;
    }

    public Map<Integer, CrewMember> getPersonnelWounded()
    {
        return personnelWounded;
    }    

    public Map<Integer, CrewMember> getPersonnelTransferredHome()
    {
        return personnelTransferredHome;
    }

    public void addPersonnelKilled(CrewMember campaignMemberKilled)
    {
        this.personnelKilled.put(campaignMemberKilled.getSerialNumber(), campaignMemberKilled);
    }

    public void addPersonnelMaimed(CrewMember campaignMemberMaimed)
    {
        this.personnelMaimed.put(campaignMemberMaimed.getSerialNumber(), campaignMemberMaimed);
    }

    public void addPersonnelWounded(CrewMember campaignMemberWounded)
    {
        this.personnelWounded.put(campaignMemberWounded.getSerialNumber(), campaignMemberWounded);
    }

    public void addPersonnelCaptured(CrewMember campaignMemberCaptured)
    {
        this.personnelCaptured.put(campaignMemberCaptured.getSerialNumber(), campaignMemberCaptured);
    }

    public void addPersonnelTransferredHome(CrewMember campaignMemberTransferred)
    {
        this.personnelTransferredHome.put(campaignMemberTransferred.getSerialNumber(), campaignMemberTransferred);
    }
    
    public Map<Integer, CrewMember> getAllInjured()
    {
        Map<Integer, CrewMember> allInjured = new HashMap<>();
        allInjured.putAll(personnelWounded);
        allInjured.putAll(personnelMaimed);
        allInjured.putAll(personnelKilled);
        return allInjured;
    }

    public boolean crewMemberisWoundedToday(CrewMember crewMember)
    {
        Map<Integer, CrewMember> allInjured = getAllInjured();
        return allInjured.containsKey(crewMember.getSerialNumber());
    }
}
