package pwcg.product.rof.country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IRankHelper;

public class RoFRank implements IRankHelper
{
    Map <Integer, Map<Integer, RankStruct>> ranksByService = new TreeMap <Integer, Map<Integer, RankStruct>>();
    
	Map<Integer, RankStruct> frenchArmy = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> frenchNavy = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> belgium = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> royalFlyingCorps = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> royalNavalAirService = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> royalAirForce = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> unitedStatesAirService = new TreeMap<Integer, RankStruct>();
    Map<Integer, RankStruct> imperialRussianAirService = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> luftstrietkrafte = new TreeMap<Integer, RankStruct>();
	Map<Integer, RankStruct> marineKorp = new TreeMap<Integer, RankStruct>();
    Map<Integer, RankStruct> lft = new TreeMap<Integer, RankStruct>();

	public RoFRank ()
	{
		// French and Belgian
		frenchArmy.put (0, new RankStruct("Capitaine", "Cne"));
		frenchArmy.put (1, new RankStruct("Lieutenant", "Lt"));
		frenchArmy.put (2, new RankStruct("Sous Lieutenant", "S/Lt"));
		frenchArmy.put (3, new RankStruct("Sergent", "Sergt"));
		frenchArmy.put (4, new RankStruct("Corporal", "Cpl"));

		frenchNavy.put (0, new RankStruct("Lieutenant de Vaisseau", "Lt.d.V."));
		frenchNavy.put (1, new RankStruct("Enseigne 1re classe", "E.d.V. 1re"));
		frenchNavy.put (2, new RankStruct("Enseigne 2e Classe", "E.d.V. 2e"));
		frenchNavy.put (3, new RankStruct("Maitre", "Mtr"));
        frenchNavy.put (4, new RankStruct("Quartier Maitre", "Q.Mtr"));
		
		belgium.put (0, new RankStruct("Capitain", "Cne"));
		belgium.put (1, new RankStruct("1.eme Lieutenant", "Lt"));
		belgium.put (2, new RankStruct("Sous Lieutenant", "S/Lt"));
		belgium.put (3, new RankStruct("1er Sergent", "Sergt"));
		belgium.put (4, new RankStruct("Caporal", "Cpl"));

        // Russian
        imperialRussianAirService.put (0, new RankStruct("Captain", "Cpt"));
        imperialRussianAirService.put (1, new RankStruct("Lieutenant", "Lt"));
        imperialRussianAirService.put (2, new RankStruct("Praporshchik", "Pk"));
        imperialRussianAirService.put (3, new RankStruct("Deputy Praporshchik", "DPk"));
        imperialRussianAirService.put (4, new RankStruct("Sub-Praporshchik ", "SPk"));
        
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
		
		marineKorp.put (0, new RankStruct("Kapitanleutnant", "KptLtn"));
		marineKorp.put (1, new RankStruct("Oberleutnant zur See", "Obltn.z.S"));
		marineKorp.put (2, new RankStruct("Leutnant zur See", "Ltn.z.S"));
		marineKorp.put (3, new RankStruct("Flugmeister", "Flgm"));
		marineKorp.put (4, new RankStruct("VizeFlugmeister", "Vflgm"));

        lft.put (0, new RankStruct("Kommandeur", "Kmdr"));
        lft.put (1, new RankStruct("Oberleutnant", "Obltn"));
        lft.put (2, new RankStruct("Leutnant", "Ltn"));
        lft.put (3, new RankStruct("Feldwebel", "Fw"));
        lft.put (4, new RankStruct("Korporal ", "Kp"));

        ranksByService.put(RoFServiceManager.LAVIATION_MILITAIRE, frenchArmy);
        ranksByService.put(RoFServiceManager.LAVIATION_MARINE, frenchNavy);
        ranksByService.put(RoFServiceManager.AVIATION_MILITAIRE_BELGE, belgium);
        ranksByService.put(RoFServiceManager.RFC, royalFlyingCorps);
        ranksByService.put(RoFServiceManager.RNAS, royalNavalAirService);
        ranksByService.put(RoFServiceManager.RAF, royalAirForce);
        ranksByService.put(RoFServiceManager.USAS, unitedStatesAirService);
        ranksByService.put(RoFServiceManager.RUSSIAN_AIR_SERVICE, imperialRussianAirService);
        ranksByService.put(RoFServiceManager.DEUTSCHE_LUFTSTREITKRAFTE, luftstrietkrafte);
        ranksByService.put(RoFServiceManager.MARINE_FLIEGER_CORP, marineKorp);
        ranksByService.put(RoFServiceManager.AUSTRO_HUNGARIAN_AIR_SERVICE, lft);

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


    /**
     * @param currentRank
     * @param service
     * @return
     */
    public String getRankAbbrev (String rank)
    {
        String abbrev = getRankAbbrevByService (rank, frenchArmy);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }
        
        abbrev = getRankAbbrevByService (rank, frenchNavy);
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
        
        abbrev = getRankAbbrevByService (rank, marineKorp);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }

        abbrev = getRankAbbrevByService (rank, imperialRussianAirService);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }

        abbrev = getRankAbbrevByService (rank, lft);
        if (abbrev.length() > 0)
        {
            return abbrev;
        }

        return "";
    }

    /**
     * @param currentRank
     * @param service
     * @return
     */
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

    /**
     * @param index
     * @param service
     * @return
     */
    public String getRankByService (int index, ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = getRankMapByService(service);
        RankStruct rankStruct = rankMap.get(index);
        
        return rankStruct.rank;
    }

    /**
     * @param index
     * @param service
     * @return
     */
    public String getRankAbbrevByService (int index, ArmedService service)
    {
        Map<Integer, RankStruct> rankMap = getRankMapByService(service);
        RankStruct rankStruct = rankMap.get(index);
        
        return rankStruct.abbrev;
    }
	
    
    /**
     * @param rank
     * @param service
     * @return
     */
    public boolean isCommandRank(String rank, ArmedService service)
    {
        RoFRank rankObj = new RoFRank();
        int rankPos = rankObj.getRankPosByService(rank, service);
        if (rankPos == 0)
        {
            return true;
        }
        
        return false;
    }
	/**
	 * @author Patrick Wilson
	 *
	 */
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

    
    /**
     * Create a map of number of positions at each rank
     * 
     * @return
     */
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
    

    

    /**
     * Number of pilots in a WWI squadron
     * 
     * @return
     */
    @Override
    public int getNumPilotsInSquadron()
    {
        return 10;
    }
}
