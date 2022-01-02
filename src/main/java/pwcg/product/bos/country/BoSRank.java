package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.io.json.RankIOJson;
import pwcg.campaign.personnel.Rank;
import pwcg.campaign.personnel.Ranks;
import pwcg.core.utils.PWCGLogger;

public class BoSRank implements IRankHelper 
{
    Map <Integer, Map<Integer, RankStruct>> ranksByService = new TreeMap <Integer, Map<Integer, RankStruct>>();

    Map<Integer, RankStruct> svv = new TreeMap<Integer, RankStruct>();
    Map<Integer, RankStruct> wehrmacht = new TreeMap<Integer, RankStruct>();
    Map<Integer, RankStruct> usarmy = new TreeMap<Integer, RankStruct>();
    Map<Integer, RankStruct> britisharmy = new TreeMap<Integer, RankStruct>();

	public BoSRank ()
	{
	    try
	    {
    	    Ranks ranks = RankIOJson.readJson();
    	    
    	    for (Rank rank : ranks.getRanks())
    	    {
    	        RankStruct rankStruct = new RankStruct(rank.getRankName(), rank.getRankAbbrev());

                if (rank.getRankService() == BoSServiceManager.WEHRMACHT)
                {
                    wehrmacht.put(rank.getRankId(), rankStruct);
                }
                else if (rank.getRankService() == BoSServiceManager.SVV)
                {
                    svv.put(rank.getRankId(), rankStruct);
                }
                else if (rank.getRankService() == BoSServiceManager.US_ARMY)
                {
                    usarmy.put(rank.getRankId(), rankStruct);
                }
                else if (rank.getRankService() == BoSServiceManager.BRITISH_ARMY)
                {
                    britisharmy.put(rank.getRankId(), rankStruct);
                }
    	    }
            
            ranksByService.put(BoSServiceManager.WEHRMACHT, wehrmacht);
            ranksByService.put(BoSServiceManager.SVV, svv);
            ranksByService.put(BoSServiceManager.US_ARMY, usarmy);
            ranksByService.put(BoSServiceManager.BRITISH_ARMY, britisharmy);

	    }
	    catch (Exception e)
	    {
	        PWCGLogger.logException(e);
	    }
	}

	@Override
    public ArrayList<String> getRanksByService (ArmedService service)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		Map<Integer, RankStruct> rankMap = ranksByService.get(service.getServiceId()); 
		
		for (RankStruct rankStruct : rankMap.values())
		{
		    list.add(rankStruct.rank);
		}
		
		return list;
	}

    private Map<Integer, RankStruct> getRankMapByService (ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = ranksByService.get(service.getServiceId()); 
        
        return rankMap;
    }

    @Override
    public int getLowestRankPosForService (ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = ranksByService.get(service.getServiceId()); 
        
        return rankMap.size() - 1;
    }

    @Override
    public int getRankPosByService (String currentRank, ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = getRankMapByService(service);

        ArrayList<String> ranks = getRanksByService(service);
        for (int i = 0; i < ranks.size(); ++i)
        {
            RankStruct rankStruct = rankMap.get(i);
            if(currentRank.equalsIgnoreCase(rankStruct.rank))
            {
                return i;
            }
        }
        
        return 0;
    }

    @Override
    public String getRankAbbrev (String rank)
    {
        String abbrev = getRankAbbrevByService (rank, britisharmy);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }
        
        abbrev = getRankAbbrevByService (rank, svv);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }

        abbrev = getRankAbbrevByService (rank, usarmy);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }

        abbrev = getRankAbbrevByService (rank, wehrmacht);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }

        return "";
    }

    private String getRankAbbrevByService (String rank, Map<Integer, RankStruct> rankMap)
    {
        for (RankStruct rankStruct : rankMap.values())
        {
            if(rank.equalsIgnoreCase(rankStruct.rank))
            {
                return rankStruct.abbrev;
            }
        }
        
        return "";
    }

    @Override
    public String getRankByService (int index, ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = getRankMapByService(service);
        RankStruct rankStruct = rankMap.get(index);
        
        return rankStruct.rank;
    }

    @Override
    public String getRankAbbrevByService (int index, ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = getRankMapByService(service);
        RankStruct rankStruct = rankMap.get(index);
        
        return rankStruct.abbrev;
    }

    public boolean isCommandRank(String rank, ArmedService service)
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(rank, service);
        if (rankPos == 0)
        {
            return true;
        }
        
        return false;
    }

	private class RankStruct
	{
	    public String rank;
	    public String abbrev;

	    public RankStruct(String rank, String abbrev)
	    {
            this.rank = rank;
            this.abbrev = abbrev;
	    }
	}

    public Map<Integer, Integer> createRankMap()
    {
        Map<Integer, Integer> numAtRank = new HashMap<Integer, Integer>();
        numAtRank.put(0, 1);
        numAtRank.put(1, 1);
        numAtRank.put(2, 2);
        numAtRank.put(3, 6);
        numAtRank.put(4, 2);
        return numAtRank;
    }

    @Override
    public int getNumCrewMembersInSquadron()
    {
        return 12;
    }

}
