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
    private Map<Integer, SquadronMember> squadronMemberCollection = new HashMap<>();

    public Map<Integer, SquadronMember> getSquadronMemberCollection()
    {
        return squadronMemberCollection;
    }

    public void setSquadronMemberCollection(Map<Integer, SquadronMember> squadronMembers)
    {
        this.squadronMemberCollection = squadronMembers;
    }

    public void addToSquadronMemberCollection(SquadronMember squadronMember)
    {
        squadronMemberCollection.put(squadronMember.getSerialNumber(), squadronMember);
    }

    public boolean isSquadronMember(Integer serialNumber)
    {
        return squadronMemberCollection.containsKey(serialNumber);
    }

    public SquadronMember removeAnySquadronMember() throws PWCGException
    {
        List<Integer> serialNumbers = new ArrayList<>(squadronMemberCollection.keySet());
        int index = RandomNumberGenerator.getRandom(serialNumbers.size());
        int serialNumber = serialNumbers.get(index);
        return removeSquadronMember(serialNumber);
    }

    public SquadronMember removeSquadronMember(int serialNumber) throws PWCGException
    {
        SquadronMember squadronMember = squadronMemberCollection.remove(serialNumber);
        return squadronMember;
    }

    public int getActiveCount(Date date) throws PWCGException
    {
        Map<Integer, SquadronMember> activeSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronMemberCollection, date);
        return activeSquadronMembers.size();
    }
    
    public SquadronMember getSquadronMember(int serialNumber)
    {
        SquadronMember squadronMember = squadronMemberCollection.get(serialNumber);
        return squadronMember;
    }
    
    public void clear()
    {
        squadronMemberCollection.clear();
    }

    public SquadronMember getSquadronMemberByName(String name) throws PWCGException
    {
        for (SquadronMember squadronMember : squadronMemberCollection.values())
        {
            if (squadronMember.getName().equals(name))
            {
                return squadronMember;
            }
        }
        
        throw new PWCGException("No squadronmember found for name " + name);
    }

}
