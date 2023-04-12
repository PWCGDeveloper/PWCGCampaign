package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.io.json.SquadronIOJson;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class SquadronManager 
{
	private TreeMap<Integer, Squadron> squadronMap = new TreeMap<>();

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
			squadron.initializeSkins();
		}
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

    public Squadron getSquadronByName(String squadName, Date campaignDate) throws PWCGException 
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

    public List<Squadron> getAllSquadrons()
    {
        ArrayList<Squadron> list = new ArrayList<Squadron>();
        list.addAll(squadronMap.values());
        return list;
        
    }

    public List<Squadron> getActiveSquadrons(Date date) throws PWCGException 
    {
        ArrayList<Squadron> returnSquadList = new ArrayList<Squadron>();
        for (Squadron squadron : getAllSquadrons())
        {
            if (SquadronViability.isSquadronActive(squadron, date))
            {
                returnSquadList.add(squadron);
            }
        }

        return returnSquadList;
    }

    public List<Squadron> getActiveSquadronsForCurrentMap(FrontMapIdentifier mapIdentifier, Date date) throws PWCGException 
    {
        List<Squadron> activeSquadronsForCurrentMap = SquadronReducer.reduceToCurrentMap(mapIdentifier, getActiveSquadrons(date), date);
        return activeSquadronsForCurrentMap;
        
    }

    public List<Squadron> getActiveSquadronsForSide(Date date, Side side) throws PWCGException 
    {
        List<Squadron> activeSquadronsForSide = SquadronReducer.reduceToSide(getActiveSquadrons(date), side);
        return activeSquadronsForSide;
    }

    public List<Squadron> getActiveSquadronsForService(Date date, ArmedService service) throws PWCGException 
    {
        List<Squadron> activeSquadronsForService = SquadronReducer.reduceToService( getActiveSquadrons(date), date, service);
        return activeSquadronsForService;
    }

    public List<Squadron> getActiveSquadronsBySideAndProximity(FrontMapIdentifier mapIdentifier, Side side, Date date, Coordinate referencePosition, double radius) throws PWCGException
    {
        List<Squadron> activeSquadronsForSide = SquadronReducer.reduceToSide( getActiveSquadrons(date), side);
        List<Squadron> activeSquadronsForSideInRange = new ArrayList<>();
        while (activeSquadronsForSideInRange.isEmpty())
        {
            activeSquadronsForSideInRange = SquadronReducer.reduceToProximityOnCurrentMap(mapIdentifier, activeSquadronsForSide, date, referencePosition, radius);
            radius += 5000;
            if (radius > 500000)
            {
                break;
            }
        }
        return activeSquadronsForSideInRange;        
    }

	public List<Squadron> getPlayerFlyableSquadronsByService(ArmedService service, Date date) throws PWCGException 
	{
		List<Squadron> squadList = getActiveSquadronsForService(date, service);
        List<Squadron> list = new ArrayList<Squadron>();
	        
		for (Squadron squadron : squadList)
		{
		    if (squadron.hasFlyablePlane(date))
		    {
		        PWCGLogger.log(LogLevel.DEBUG, squadron.determineDisplayName(date)
		                + "getFlyableSquadronsByService Add squadron + squadron id is " + squadron.getSquadronId());
		        list.add(squadron);
		    }
		    else
		    {
		        PWCGLogger.log(LogLevel.DEBUG, squadron.determineDisplayName(date)
		                + " getFlyableSquadronsByService not flyable because of date: " + DateUtils.getDateStringDashDelimitedYYYYMMDD(date));
		    }
		}
		
		return list;
	}

    public Squadron getAnyActiveSquadronForAirfield(FrontMapIdentifier mapIdentifier, Airfield airfield, Date date) throws PWCGException 
    {
        for (Squadron squadron : squadronMap.values())
        {
            String currentFieldNameForSquad = squadron.determineCurrentAirfieldName(date);
            if (currentFieldNameForSquad != null)
            {
                Airfield currentFieldForSquad =  PWCGContext.getInstance().getMap(mapIdentifier).getAirfieldManager().getAirfield(currentFieldNameForSquad);
                
                if (currentFieldForSquad != null)
                {
                    String squadAF = currentFieldForSquad.getName();
                    if (airfield.getName().equals(squadAF))
                    {
                        if (SquadronViability.isSquadronActive(squadron, date))
                        {
                            return squadron;
                        }
                    }
                }
            }
        }
        
        return null;
    }

    public ArrayList<Squadron> getViableSquadrons(Campaign campaign) throws PWCGException 
    {
        ArrayList<Squadron> returnSquadList = new ArrayList<Squadron>();
        for (Squadron squadron : getAllSquadrons())
        {
            if (SquadronViability.isSquadronViable(squadron, campaign))
            {
                returnSquadList.add(squadron);
            }
        }

         return returnSquadList;
    }

    public List<Squadron> getViableAiSquadronsForCurrentMapAndSide(Campaign campaign, Side side) throws PWCGException 
    {
        List<Squadron> viableSquadronsForSide = SquadronReducer.reduceToSide(getViableSquadrons(campaign), side);
        List<Squadron> selectedSquadronsNoPlayer = SquadronReducer.reduceToAIOnly(viableSquadronsForSide, campaign);
        List<Squadron> selectedSquadronsForCurrentMap = SquadronReducer.reduceToCurrentMap(campaign.getCampaignMap(), selectedSquadronsNoPlayer, campaign.getDate());
        
        return eliminateAnomalySquadrons(campaign, selectedSquadronsForCurrentMap);
    }

    public List<Squadron> getViableAiSquadronsForCurrentMapAndSideAndRole(Campaign campaign, List <PwcgRole> acceptableRoles, Side side) throws PWCGException 
    {
        List<Squadron> viableSquadronsForSide = SquadronReducer.reduceToSide(getViableSquadrons(campaign), side);
        List<Squadron> selectedSquadronsByRole = SquadronReducer.reduceToRole(viableSquadronsForSide, acceptableRoles, campaign.getDate());
        List<Squadron> selectedSquadronsNoPlayer = SquadronReducer.reduceToAIOnly(selectedSquadronsByRole, campaign);
        List<Squadron> selectedSquadronsForCurrentMap = SquadronReducer.reduceToCurrentMap(campaign.getCampaignMap(), selectedSquadronsNoPlayer, campaign.getDate());
        
        return eliminateAnomalySquadrons(campaign, selectedSquadronsForCurrentMap);
    }

    private List<Squadron> eliminateAnomalySquadrons(Campaign campaign, List<Squadron> selectedSquadronsForCurrentMap) throws PWCGException
    {
        if (campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.RemoveNonHistoricalSquadronsKey) == 1)
        {
            List<Squadron> selectedSquadronsNoAnomalies = SquadronReducer.reduceToNoAnomalies(selectedSquadronsForCurrentMap, campaign.getDate());
            return selectedSquadronsNoAnomalies;
        }
        else
        {
            return selectedSquadronsForCurrentMap;
        }
    }

    public Squadron getClosestSquadron(FrontMapIdentifier mapIdentifier, Coordinate position, Date date) throws PWCGException 
    {
        Airfield airfield = PWCGContext.getInstance().getMap(mapIdentifier).getAirfieldManager().getAirfieldFinder().findClosestAirfield(position);
        if (airfield != null)
        {
            Squadron squadron = getAnyActiveSquadronForAirfield(mapIdentifier, airfield, date);
            return squadron;
        }

        return null;
    }
}
