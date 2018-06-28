package pwcg.campaign.ww2.country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;

public class BoSRank implements IRankHelper 
{
    Map <Integer, Map<Integer, RankStruct>> ranksByService = new TreeMap <Integer, Map<Integer, RankStruct>>();
    
    Map<Integer, RankStruct> ussr = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> luftwaffe = new TreeMap<Integer, RankStruct>();
    Map<Integer, RankStruct> iaf = new TreeMap<Integer, RankStruct>();

	public BoSRank ()
	{
        // Russian	    
        ussr.put (0, new RankStruct("Major", "Maj"));
        ussr.put (1, new RankStruct("Kapitan", "Kap"));
        ussr.put (2, new RankStruct("Starshyi leyitenant", "SLt"));
        ussr.put (3, new RankStruct("Leyitenant", "Lt"));
        ussr.put (4, new RankStruct("Serzhant", "Szt"));
        
		// Germany
        luftwaffe.put (0, new RankStruct("Major", "Maj"));
        luftwaffe.put (1, new RankStruct("Hauptmann", "Hptm"));
		luftwaffe.put (2, new RankStruct("Oberleutnant", "Olt"));
		luftwaffe.put (3, new RankStruct("Leutnant", "Ltn"));
		luftwaffe.put (4, new RankStruct("Oberfeldwebel", "OFw"));
		
		// Italy
        iaf.put (0, new RankStruct("Maggiore", "Mgr"));
        iaf.put (1, new RankStruct("Capitano", "Cpt"));
        iaf.put (2, new RankStruct("Tenente", "Tnt"));
        iaf.put (3, new RankStruct("Sottotenente", "STnt"));
        iaf.put (4, new RankStruct("Sergeant", "Sgt"));

        // Form a map of rank maps
        ranksByService.put(BoSServiceManager.VVS, ussr);
        ranksByService.put(BoSServiceManager.LUFTWAFFE, luftwaffe);
        ranksByService.put(BoSServiceManager.REGIA_AERONAUTICA, iaf);

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
        String abbrev = getRankAbbrevByService (rank, luftwaffe);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }
        
        abbrev = getRankAbbrevByService (rank, ussr);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }

        abbrev = getRankAbbrevByService (rank, iaf);
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
    public int getNumPilotsInSquadron()
    {
        return 12;
    }

}
