package pwcg.campaign.api;

import java.util.ArrayList;
import java.util.Map;

import pwcg.campaign.ArmedService;

public interface IRankHelper
{
    public static int COMMAND_RANK = 0;
    
    public ArrayList<String> getRanksByService(ArmedService service);
    public int getLowestRankPosForService(ArmedService service);
    public int getRankPosByService(String currentRank, ArmedService service);
    public String getRankAbbrev(String rank);
    public String getRankByService(int index, ArmedService service);
    public String getRankAbbrevByService(int index, ArmedService service);
    public boolean isCommandRank(String rank, ArmedService service);
    public Map<Integer, Integer> createRankMap();
    public int getNumCrewMembersInSquadron();
}