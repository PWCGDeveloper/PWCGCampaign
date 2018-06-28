package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class SquadronMembers
{
    private Map<Integer, SquadronMember> squadronMembers = new HashMap<>();

    public Map<Integer, SquadronMember> getSquadronMembers()
    {
        return squadronMembers;
    }

    public void setSquadronMembers(Map<Integer, SquadronMember> squadronMembers)
    {
        this.squadronMembers = squadronMembers;
    }

    public void addSquadronMember(SquadronMember squadronMember)
    {
        squadronMembers.put(squadronMember.getSerialNumber(), squadronMember);
    }

    public boolean isSquadronMember(Integer serialNumber)
    {
        return squadronMembers.containsKey(serialNumber);
    }

    public SquadronMember removeAnySquadronMember() throws PWCGException
    {
        List<Integer> serialNumbers = new ArrayList<>(squadronMembers.keySet());
        int index = RandomNumberGenerator.getRandom(serialNumbers.size());
        int serialNumber = serialNumbers.get(index);
        return removeSquadronMember(serialNumber);
    }

    public SquadronMember removeSquadronMember(int serialNumber) throws PWCGException
    {
        SquadronMember squadronMember = squadronMembers.remove(serialNumber);
        return squadronMember;
    }

    public int getActiveCount(Date date) throws PWCGException
    {
        Map<Integer, SquadronMember> activeSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronMembers, date);
        return activeSquadronMembers.size();
    }
    
    public SquadronMember getSquadronMember(int serialNumber)
    {
        SquadronMember squadronMember = squadronMembers.get(serialNumber);
        return squadronMember;
    }
    
    public void clear()
    {
        squadronMembers.clear();
    }

    public SquadronMember getSquadronMemberByName(String name) throws PWCGException
    {
        for (SquadronMember squadronMember : squadronMembers.values())
        {
            if (squadronMember.getName().equals(name))
            {
                return squadronMember;
            }
        }
        
        throw new PWCGException("No squadronmember found for name " + name);
    }

}
