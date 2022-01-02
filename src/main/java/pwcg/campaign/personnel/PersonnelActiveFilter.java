package pwcg.campaign.personnel;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;

public class PersonnelActiveFilter
{
    public PersonnelActiveFilter ()
    {
    }
    
    public Map<Integer, CrewMember> getActive(Map<Integer, CrewMember> input) 
    {
        Map<Integer, CrewMember> returnCrewMembers = new HashMap<>();
        for (CrewMember crewMember : input.values())
        {
            if (isActive(crewMember))
            {
                returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }

        return returnCrewMembers;
    }
    
    public Map<Integer, CrewMember> getInactive(Map<Integer, CrewMember> input) 
    {
        Map<Integer, CrewMember> returnCrewMembers = new HashMap<>();
        for (CrewMember crewMember : input.values())
        {
            if (!isActive(crewMember))
            {
                returnCrewMembers.put(crewMember.getSerialNumber(), crewMember);
            }
        }

        return returnCrewMembers;
    }

    
    private boolean isActive(CrewMember crewMember)
    {
        if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE)
        {
            return true;
        }
        else if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ON_LEAVE)
        {
            return true;
        }
        else if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_TRANSFERRED)
        {
            return false;
        }
        else if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_WOUNDED)
        {
            return true;
        }
        else if (crewMember.isPlayer() && crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            return true;
        }
        
        return false;
    }
}
