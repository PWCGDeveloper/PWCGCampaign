package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.SquadronIOJson;
import pwcg.campaign.io.json.SquadronMovingFrontIOJson;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronMovingFrontOverlay;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

public class SquadronManager 
{
	private TreeMap<Integer, Squadron> squadronMap = new TreeMap<Integer, Squadron>();

	public SquadronManager ()
	{
	}

	public void initialize() throws PWCGException 
	{
        squadronMap.clear();
        		
        List<Squadron> squadrons = SquadronIOJson.readJson(); 
		for (Squadron squadron : squadrons)
		{
			squadronMap.put(squadron.getSquadronId(), squadron);
		}
		
        setAirfieldsForMovingFront();
	}

	public Map<Integer, Squadron> countryToMap (ICountry country, Date date) throws PWCGException
	{
		Map<Integer, Squadron> squadronsByCountryMap = new TreeMap<Integer, Squadron>();
		
		for (Squadron squadron : squadronMap.values())
		{
		    ICountry squadCountry = squadron.determineSquadronCountry(date);
			if (squadCountry.equals(country))
			{
				squadronsByCountryMap.put(squadron.getSquadronId(), squadron);
			}
		}
		
		return squadronsByCountryMap;
	}

	public Squadron getSquadron(int id) throws PWCGException
	{
		if(squadronMap.containsKey(id))
		{
			Squadron squadron = squadronMap.get(id);
			return squadron;
		}
		
    	return null;
	}

	public List<Squadron> getSquadronsForSide(Side side, Date date) throws PWCGException
	{		
		ArrayList<Squadron> list = new ArrayList<Squadron>();

		for (Squadron squadron : squadronMap.values())
		{
		    if (side == squadron.determineSquadronCountry(date).getSide())
		    {
                list.add(squadron);
		    }
		}
		
		return list;
		
	}

	public List<Squadron> getAllSquadrons()
	{
		ArrayList<Squadron> list = new ArrayList<Squadron>();
		list.addAll(squadronMap.values());
		return list;
		
	}

    public List<Squadron> getActiveSquadrons(Date date) throws PWCGException 
    {
        List<Squadron> list = new ArrayList<Squadron>();
        for (Squadron squadron : squadronMap.values())
        {
            if (squadron.isCanFly(date))
            {
                list.add(squadron);
            }
        }
        
        return list;
    }

    public List<Squadron> getActiveSquadronsForSide(Side side, Date date) throws PWCGException 
    {
        List<Squadron> list = new ArrayList<Squadron>();
        for (Squadron squadron : squadronMap.values())
        {
            if (side == squadron.determineSquadronCountry(date).getSide())
            {
                if (squadron.isCanFly(date))
                {
                    list.add(squadron);
                }
            }
        }
        
        return list;
    }

    public List<Squadron> getActiveSquadronsForMap(Date date) throws PWCGException 
    {
        List<Squadron> listForMap = new ArrayList<Squadron>();
        List<Squadron> listAll = getActiveSquadrons(date);
        
        for (Squadron squadron : listAll)
        {
            IAirfield field = squadron.determineCurrentAirfieldCurrentMap(date);
            if (field != null)
            {
                listForMap.add(squadron);
            }
        }

        return listForMap;
        
    }
    
    public List<Squadron> getActiveSquadronsForService(Date date, ArmedService service) throws PWCGException 
    {
        List<Squadron> squadronsForService = new ArrayList<>();
        for (Squadron squadron : getActiveSquadrons(date))
        {
            if (squadron.determineServiceForSquadron(date).getServiceId() == service.getServiceId())
            {
                squadronsForService.add(squadron);
            }
        }
        return squadronsForService;
    }


	public Squadron getSquadronByName(String squadName, Date campaignDate) 
	                throws PWCGException 
	{
		Squadron squadReturn = null;
		for (Squadron squadron : squadronMap.values())
		{
			if (squadName.equalsIgnoreCase(squadron.determineDisplayName(campaignDate)))
			{
				squadReturn = squadron;
			}
			
			if (squadName.equalsIgnoreCase(squadron.determineDisplayName(campaignDate)))
			{
				squadReturn = squadron;
			}
			
			String displayName = squadron.determineDisplayName(campaignDate);			
			if (displayName != null)
			{
				if (squadName.equalsIgnoreCase(displayName))
				{
					squadReturn = squadron;
				}				
			}
			
			if (squadReturn != null)
			{
				 break;
			}
		}
		
		if (squadReturn == null)
		{
			 throw new PWCGException ("Squad not found for name " + squadName);
		}
		
		return squadReturn;
	}

	public ArrayList<Squadron> getFlyableSquadrons(ICountry country, Date date) throws PWCGException
	{
		TreeMap<String, Squadron> squadMap = new TreeMap<String, Squadron>();
		
		Map<Integer, Squadron> map = countryToMap(country, date);
		ArrayList<Squadron> squadList = new ArrayList<Squadron>();
		squadList.addAll(map.values());
		for (int i = 0; i < squadList.size(); ++i)
		{
			Squadron squadron = squadList.get(i);
            if (squadron.isCanFly(date))
            {
                squadMap.put(squadron.determineDisplayName(date), squadron);
            }
		}
		
		ArrayList<Squadron> list = new ArrayList<Squadron>();
		list.addAll(squadMap.values());
		return list;
		
	}

	public List<Squadron> getFlyableSquadronsByService(ArmedService service, Date date) throws PWCGException 
	{
		TreeMap<Integer, Squadron> squadMap = new TreeMap<Integer, Squadron>();
				
		List<Squadron> squadList = getAllSquadrons();        
	        
		for (Squadron squadron : squadList)
		{
			ArmedService squadService = squadron.determineServiceForSquadron(date);
			if (squadService.getServiceId() == service.getServiceId())
			{
			    if (squadron.isCanFly(date))
			    {
                    Logger.log(LogLevel.DEBUG, squadron.determineDisplayName(date) 
                            + "getFlyableSquadronsByService Add squadron + squadron id is " + squadron.getSquadronId());
			        squadMap.put(squadron.getSquadronId(), squadron);
			    }
			    else
			    {
	                Logger.log(LogLevel.DEBUG, squadron.determineDisplayName(date) 
	                        + " getFlyableSquadronsByService not flyable because of date: " + DateUtils.getDateStringDashDelimitedYYYYMMDD(date));
			    }
			}
		}
		
		ArrayList<Squadron> list = new ArrayList<Squadron>();
		list.addAll(squadMap.values());
		return list;
		
	}

	public List<Squadron> getNearestSquadronsByRole(Campaign campaign, 
	                                                Coordinate startPoint,
	                                                int minimumSquadrons,
	                                                double radius,
	                                                List<Role> acceptableRoles,
	                                                Side side,
	                                                Date date)
		throws PWCGException 
	{
		List<Squadron> squadronsForSide = getSquadronsForSide(side, date);
		List<Squadron> activeSquadronsForSide = reduceToActiveSquadrons(campaign, squadronsForSide, date);
		List<Squadron> squadronsForRole = getSquadronsByRole(activeSquadronsForSide, acceptableRoles, date);

		return squadronsForRole;
	}

    public List<Squadron> getNearestSquadronsBySide(Campaign campaign, 
                                                    Coordinate startPoint,
                                                    int minimumSquadrons,
                                                    double radius,
                                                    Side side,
                                                    Date date)
        throws PWCGException 
    {
        List<Squadron> squadronsForSide = getSquadronsForSide(side, date);
        List<Squadron> activeSquadronsForSide = reduceToActiveSquadrons(campaign, squadronsForSide, date);

        return activeSquadronsForSide;
    }

    public List<Squadron> reduceToFlyable(List<Squadron> squadronsToEvaluate, Date date) throws PWCGException
    {
        List<Squadron> activeSquadrons = new ArrayList<Squadron>();
        for (Squadron squadron : squadronsToEvaluate)
        {
            if (squadron.isCanFly(date))
            {
                activeSquadrons.add(squadron);
            }
        }
        
        return activeSquadrons;
    }

	public Squadron getAnyActiveSquadronForAirfield(IAirfield airfield, Date date) throws PWCGException 
	{
		for (Squadron squadron : squadronMap.values())
		{
			String currentFieldNameForSquad = squadron.determineCurrentAirfieldName(date);
			if (currentFieldNameForSquad != null)
			{
    			IAirfield currentFieldForSquad =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(currentFieldNameForSquad);
    			
    			if (currentFieldForSquad != null)
    			{
    				String squadAF = currentFieldForSquad.getName();
    				if (airfield.getName().equals(squadAF))
    				{
    					if (squadron.isCanFly(date))
    					{
    					    return squadron;
    					}
    				}
    			}
			}
		}
		
		return null;
	}

	public ArrayList<Squadron> getSquadronsByRole(List<Squadron> squadrons, List<Role> acceptableRoles, Date date) throws PWCGException 
	{		
		ArrayList<Squadron> squadronsWithRole = new ArrayList<Squadron>();
		for(Squadron squadron : squadrons)
		{
			for (Role acceptableRole : acceptableRoles)
			{
				if (squadron.isSquadronThisRole(date, acceptableRole))
				{
					squadronsWithRole.add(squadron);
					break;
				}
			}
		}

		return squadronsWithRole;
	}

    public Squadron getSquadronByProximityAndRoleAndSide(Campaign campaign, Coordinate position, Role role, Side side) throws PWCGException 
    {
        ConfigManager configManager = campaign.getCampaignConfigManager();

        int initialSquadronSearchRadiusKey = configManager.getIntConfigParam(ConfigItemKeys.InitialSquadronSearchRadiusKey);

        Squadron enemySquadron = null;

        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(role);

        List<Squadron> enemySquadrons = null;
        enemySquadrons =  getNearestSquadronsByRole(
                        campaign,
                        position.copy(),
                        100,
                        initialSquadronSearchRadiusKey,
                        acceptableRoles,
                        side,
                        campaign.getDate());

        if (enemySquadrons.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(enemySquadrons.size());
            enemySquadron = enemySquadrons.get(index);
        }

        return enemySquadron;
    }

    private void setAirfieldsForMovingFront() throws PWCGException, PWCGException
    {
        boolean useMovingFront = PWCGContext.getInstance().determineUseMovingFront();
        if (useMovingFront)
        {
            List<SquadronMovingFrontOverlay> overlays = SquadronMovingFrontIOJson.readJson();
            for (SquadronMovingFrontOverlay overlay : overlays)
            {
                if (squadronMap.containsKey(overlay.getSquadronId()))
                {
                    Squadron squadron = squadronMap.get(overlay.getSquadronId());
                    squadron.setAirfields(overlay.getAirfields());
                }
            }
        }
    }

    private ArrayList<Squadron> reduceToActiveSquadrons(Campaign campaign, List<Squadron> squadrons, Date  date) throws PWCGException 
    {
        ArrayList<Squadron> returnSquadList = new ArrayList<Squadron>();

         for (Squadron squadron : squadrons)
        {
            String currentFieldNameForSquad = squadron.determineCurrentAirfieldName(date);
            if (currentFieldNameForSquad != null)
            {
                IAirfield currentFieldForSquad =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(currentFieldNameForSquad);
                if (currentFieldForSquad != null)
                {
                    if (squadron.isCanFly(date))
                    {
                        if (squadron.isSquadronViable(campaign))
                        {
                            returnSquadList.add(squadron);
                        }
                    }
                }
            }
        }

         return returnSquadList;
    }
}
