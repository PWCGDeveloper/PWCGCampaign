package pwcg.campaign.crewmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class CrewMembers
{
    private Map<Integer, CrewMember> squadronMemberCollection = new ConcurrentHashMap<>();

    public Map<Integer, CrewMember> getCrewMemberCollection()
    {
        return squadronMemberCollection;
    }

    public List<CrewMember> getCrewMemberList()
    {
        return new ArrayList<>(squadronMemberCollection.values());
    }

    public void setCrewMemberCollection(Map<Integer, CrewMember> squadronMembers)
    {
        this.squadronMemberCollection = squadronMembers;
    }

    public void addToCrewMemberCollection(CrewMember crewMember)
    {
        squadronMemberCollection.put(crewMember.getSerialNumber(), crewMember);
    }

    public void addCrewMembers(CrewMembers newCrewMembers)
    {
        squadronMemberCollection.putAll(newCrewMembers.squadronMemberCollection);
    }

    public boolean isCrewMember(Integer serialNumber)
    {
        return squadronMemberCollection.containsKey(serialNumber);
    }

    public CrewMember findCrewMember() throws PWCGException
    {
        List<Integer> serialNumbers = new ArrayList<>(squadronMemberCollection.keySet());
        int index = RandomNumberGenerator.getRandom(serialNumbers.size());
        int serialNumber = serialNumbers.get(index);
        CrewMember crewMember = squadronMemberCollection.get(serialNumber);
        return crewMember;
    }

    public CrewMember removeCrewMember(int serialNumber) throws PWCGException
    {
        CrewMember crewMember = squadronMemberCollection.remove(serialNumber);
        return crewMember;
    }

    public int getActiveCount(Date date) throws PWCGException
    {
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(squadronMemberCollection, date);
        return activeCrewMembers.getCrewMemberList().size();
    }
    
    public CrewMember getCrewMember(int serialNumber)
    {
        CrewMember crewMember = squadronMemberCollection.get(serialNumber);
        return crewMember;
    }
    
    public void clear()
    {
        squadronMemberCollection.clear();
    }

    public CrewMember getCrewMemberByName(String name) throws PWCGException
    {
        for (CrewMember crewMember : squadronMemberCollection.values())
        {
            if (crewMember.getName().equals(name))
            {
                return crewMember;
            }
        }
        
        throw new PWCGException("No squadronmember found for name " + name);
    }
    
    public List<CrewMember> sortCrewMembers(Date date) throws PWCGException 
    {
        Map<String, CrewMember> sortedCrewMembers = new TreeMap<>();
        for (CrewMember crewMember : squadronMemberCollection.values())
        {            
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPos = rankObj.getRankPosByService(crewMember.getRank(), crewMember.determineService(date));
            String keyVal = new String("" + rankPos + crewMember.getName());
            sortedCrewMembers.put(keyVal, crewMember);
        }
        
        return new ArrayList<>(sortedCrewMembers.values());
    }
}
