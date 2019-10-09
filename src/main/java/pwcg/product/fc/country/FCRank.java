package pwcg.product.fc.country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IRankHelper;

public class FCRank implements IRankHelper
{
    Map <Integer, Map<Integer, RankStruct>> ranksByService = new TreeMap <Integer, Map<Integer, RankStruct>>();
    
	Map<Integer, RankStruct> frenchArmy = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> belgium = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> royalFlyingCorps = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> royalNavalAirService = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> royalAirForce = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> unitedStatesAirService = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> luftstrietkrafte = new TreeMap<Integer, RankStruct>();

	public FCRank ()
	{
		// French and Belgian
		frenchArmy.put (0, new RankStruct("Capitaine", "Cne"));
		frenchArmy.put (1, new RankStruct("Lieutenant", "Lt"));
		frenchArmy.put (2, new RankStruct("Sous Lieutenant", "S/Lt"));
		frenchArmy.put (3, new RankStruct("Sergent", "Sergt"));
		frenchArmy.put (4, new RankStruct("Corporal", "Cpl"));
		
		belgium.put (0, new RankStruct("Capitain", "Cne"));
		belgium.put (1, new RankStruct("1.eme Lieutenant", "Lt"));
		belgium.put (2, new RankStruct("Sous Lieutenant", "S/Lt"));
		belgium.put (3, new RankStruct("1er Sergent", "Sergt"));
		belgium.put (4, new RankStruct("Caporal", "Cpl"));
        
		// British
		royalFlyingCorps.put (0, new RankStruct("Major", "Maj"));
		royalFlyingCorps.put (1, new RankStruct("Captain", "Cpt"));
		royalFlyingCorps.put (2, new RankStruct("Lieutenant", "Lt"));
		royalFlyingCorps.put (3, new RankStruct("2nd Lieutenant", "2nd Lt"));

		royalNavalAirService.put (0, new RankStruct("Squadron Commander", "Sqdn.Cdr"));
		royalNavalAirService.put (1, new RankStruct("Flight Commander", "Flt.Cdr"));
		royalNavalAirService.put (2, new RankStruct("Flight Lieutenant", "Flt Lt"));
		royalNavalAirService.put (3, new RankStruct("Flight Sub-Lieutenant", "Flt SLt"));

		royalAirForce.put (0, new RankStruct("Squadron Leader", "Sqn Ldr"));
		royalAirForce.put (1, new RankStruct("Flight Lieutenant", "Flt Lt"));
		royalAirForce.put (2, new RankStruct("Flying Officer", "Fg Off"));
		royalAirForce.put (3, new RankStruct("Pilot Officer", "Plt Off"));

		unitedStatesAirService.put (0, new RankStruct("Major", "Maj"));
		unitedStatesAirService.put (1, new RankStruct("Captain", "Cpt"));
		unitedStatesAirService.put (2, new RankStruct("1st Lieutenant", "Lt"));
		unitedStatesAirService.put (3, new RankStruct("2nd Lieutenant", "2nd Lt"));

		luftstrietkrafte.put (0, new RankStruct("Kommandeur", "Kmdr"));
		luftstrietkrafte.put (1, new RankStruct("Oberleutnant", "Obltn"));
		luftstrietkrafte.put (2, new RankStruct("Leutnant", "Ltn"));
		luftstrietkrafte.put (3, new RankStruct("Feldwebel", "Fw"));
		luftstrietkrafte.put (4, new RankStruct("Unteroffizier", "Uffz"));

        ranksByService.put(FCServiceManager.LAVIATION_MILITAIRE, frenchArmy);
        ranksByService.put(FCServiceManager.RFC, royalFlyingCorps);
        ranksByService.put(FCServiceManager.RNAS, royalNavalAirService);
        ranksByService.put(FCServiceManager.RAF, royalAirForce);
        ranksByService.put(FCServiceManager.USAS, unitedStatesAirService);
        ranksByService.put(FCServiceManager.AVIATION_MILITAIRE_BELGE, belgium);
        ranksByService.put(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE, luftstrietkrafte);

	}

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

    public int getLowestRankPosForService (ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = ranksByService.get(service.getServiceId()); 
        
        return rankMap.size() - 1;
    }

    public int getRankPosByService (String currentRank, ArmedService service)
    {
    	int rankPos = this.getRankPosByRankAndService(currentRank, service);
    	if (rankPos < 0)
    	{
        	rankPos = this.getRankPosByRankAbbrevAndService(currentRank, service);
    	}
        
        return rankPos;
    }
    
    public int getRankPosByRankAndService (String currentRank, ArmedService service)
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
        
        return -1;
    }
    
    
    public int getRankPosByRankAbbrevAndService (String currentRankAbbrev, ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = getRankMapByService(service);

        ArrayList<String> ranks = getRanksByService(service);
        for (int i = 0; i < ranks.size(); ++i)
        {
            RankStruct rankStruct = rankMap.get(i);
            if(currentRankAbbrev.equalsIgnoreCase(rankStruct.abbrev))
            {
                return i;
            }
        }
        
        return -1;
    }

    public String getRankAbbrev (String rank)
    {
        String abbrev = getRankAbbrevByService (rank, frenchArmy);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }

        abbrev = getRankAbbrevByService (rank, belgium);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }
        
        abbrev = getRankAbbrevByService (rank, royalFlyingCorps);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }
        
        abbrev = getRankAbbrevByService (rank, royalNavalAirService);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }
        
        abbrev = getRankAbbrevByService (rank, royalAirForce);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }
        
        abbrev = getRankAbbrevByService (rank, unitedStatesAirService);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }
        
        abbrev = getRankAbbrevByService (rank, luftstrietkrafte);
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

    public String getRankByService (int index, ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = getRankMapByService(service);
        RankStruct rankStruct = rankMap.get(index);
        
        return rankStruct.rank;
    }

    public String getRankAbbrevByService (int index, ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = getRankMapByService(service);
        RankStruct rankStruct = rankMap.get(index);
        
        return rankStruct.abbrev;
    }

    public boolean isCommandRank(String rank, ArmedService service)
    {
        FCRank rankObj = new FCRank();
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
        numAtRank.put(2, 3);
        numAtRank.put(3, 3);
        numAtRank.put(4, 2);
        return numAtRank;
    }

    @Override
    public int getNumPilotsInSquadron()
    {
        return 10;
    }
}
