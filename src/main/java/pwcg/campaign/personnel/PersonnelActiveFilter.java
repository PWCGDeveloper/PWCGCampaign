package pwcg.campaign.personnel;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;

public class PersonnelActiveFilter
{
    public PersonnelActiveFilter ()
    {
    }
    
    public Map<Integer, SquadronMember> getActive(Map<Integer, SquadronMember> input) 
    {
        Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<>();
        for (SquadronMember pilot : input.values())
        {
            if (isActive(pilot))
            {
                returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
            }
        }

        return returnSquadronMembers;
    }
    
    public Map<Integer, SquadronMember> getInactive(Map<Integer, SquadronMember> input) 
    {
        Map<Integer, SquadronMember> returnSquadronMembers = new HashMap<>();
        for (SquadronMember pilot : input.values())
        {
            if (!isActive(pilot))
            {
                returnSquadronMembers.put(pilot.getSerialNumber(), pilot);
            }
        }

        return returnSquadronMembers;
    }

    
    private boolean isActive(SquadronMember pilot)
    {
        if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
        {
            return true;
        }
        else if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ON_LEAVE)
        {
            return true;
        }
        else if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_TRANSFERRED)
        {
            return false;
        }
        else if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_WOUNDED)
        {
            return true;
        }
        else if (pilot.isPlayer() && pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            return true;
        }
        
        return false;
    }
}
