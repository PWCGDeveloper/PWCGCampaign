package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
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

    public List<SquadronMember> getSquadronMemberList()
    {
        return new ArrayList<>(squadronMemberCollection.values());
    }

    public void setSquadronMemberCollection(Map<Integer, SquadronMember> squadronMembers)
    {
        this.squadronMemberCollection = squadronMembers;
    }

    public void addToSquadronMemberCollection(SquadronMember squadronMember)
    {
        squadronMemberCollection.put(squadronMember.getSerialNumber(), squadronMember);
    }

    public void addSquadronMembers(SquadronMembers newSquadronMembers)
    {
        squadronMemberCollection.putAll(newSquadronMembers.squadronMemberCollection);
    }

    public boolean isSquadronMember(Integer serialNumber)
    {
        return squadronMemberCollection.containsKey(serialNumber);
    }

    public SquadronMember findSquadronMember() throws PWCGException
    {
        List<Integer> serialNumbers = new ArrayList<>(squadronMemberCollection.keySet());
        int index = RandomNumberGenerator.getRandom(serialNumbers.size());
        int serialNumber = serialNumbers.get(index);
        SquadronMember squadronMember = squadronMemberCollection.get(serialNumber);
        return squadronMember;
    }

    public SquadronMember removeSquadronMember(int serialNumber) throws PWCGException
    {
        SquadronMember squadronMember = squadronMemberCollection.remove(serialNumber);
        return squadronMember;
    }

    public int getActiveCount(Date date) throws PWCGException
    {
        SquadronMembers activeSquadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronMemberCollection, date);
        return activeSquadronMembers.getSquadronMemberList().size();
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
    
    public List<SquadronMember> sortPilots(Date date) throws PWCGException 
    {
        Map<String, SquadronMember> sortedPilots = new TreeMap<>();
        for (SquadronMember pilot : squadronMemberCollection.values())
        {            
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPos = rankObj.getRankPosByService(pilot.getRank(), pilot.determineService(date));
            String keyVal = new String("" + rankPos + pilot.getName());
            sortedPilots.put(keyVal, pilot);
        }
        
        return new ArrayList<>(sortedPilots.values());
    }
}
