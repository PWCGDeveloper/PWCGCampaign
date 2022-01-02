package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
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
	private TreeMap<Integer, Company> squadronMap = new TreeMap<>();

	public SquadronManager ()
	{
	}

	public void initialize() throws PWCGException 
	{
        squadronMap.clear();
        		
        List<Company> squadrons = SquadronIOJson.readJson(); 
		for (Company squadron : squadrons)
		{
			squadronMap.put(squadron.getSquadronId(), squadron);
		}
	}

	public Company getSquadron(int id) throws PWCGException
	{
		if(squadronMap.containsKey(id))
		{
			Company squadron = squadronMap.get(id);
			return squadron;
		}
		
    	return null;
	}

    public Company getSquadronByName(String squadName, Date campaignDate) throws PWCGException 
    {
        Company squadReturn = null;
        for (Company squadron : squadronMap.values())
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

    public List<Company> getAllSquadrons()
    {
        ArrayList<Company> list = new ArrayList<Company>();
        list.addAll(squadronMap.values());
        return list;
        
    }

    public List<Company> getActiveSquadrons(Date date) throws PWCGException 
    {
        ArrayList<Company> returnSquadList = new ArrayList<Company>();
        for (Company squadron : getAllSquadrons())
        {
            if (SquadronViability.isSquadronActive(squadron, date))
            {
                returnSquadList.add(squadron);
            }
        }

        return returnSquadList;
    }

    public List<Company> getActiveSquadronsForCurrentMap(Date date) throws PWCGException 
    {
        List<Company> activeSquadronsForCurrentMap = SquadronReducer.reduceToCurrentMap(getActiveSquadrons(date), date);
        return activeSquadronsForCurrentMap;
        
    }

    public List<Company> getActiveSquadronsForSide(Date date, Side side) throws PWCGException 
    {
        List<Company> activeSquadronsForSide = SquadronReducer.reduceToSide(getActiveSquadrons(date), side);
        return activeSquadronsForSide;
    }

    public List<Company> getActiveSquadronsForService(Date date, ArmedService service) throws PWCGException 
    {
        List<Company> activeSquadronsForService = SquadronReducer.reduceToService( getActiveSquadrons(date), date, service);
        return activeSquadronsForService;
    }

    public List<Company> getActiveSquadronsBySideAndProximity(Side side, Date date, Coordinate referencePosition, double radius) throws PWCGException
    {
        List<Company> activeSquadronsForSide = SquadronReducer.reduceToSide( getActiveSquadrons(date), side);
        List<Company> activeSquadronsForSideInRange = new ArrayList<>();
        while (activeSquadronsForSideInRange.isEmpty())
        {
            activeSquadronsForSideInRange = SquadronReducer.reduceToProximityOnCurrentMap(activeSquadronsForSide, date, referencePosition, radius);
            radius += 5000;
            if (radius > 500000)
            {
                break;
            }
        }
        return activeSquadronsForSideInRange;        
    }

	public List<Company> getPlayerFlyableSquadronsByService(ArmedService service, Date date) throws PWCGException 
	{
		List<Company> squadList = getActiveSquadronsForService(date, service);
        List<Company> list = new ArrayList<Company>();
	        
		for (Company squadron : squadList)
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

    public Company getAnyActiveSquadronForAirfield(Airfield airfield, Date date) throws PWCGException 
    {
        for (Company squadron : squadronMap.values())
        {
            String currentFieldNameForSquad = squadron.determineCurrentAirfieldName(date);
            if (currentFieldNameForSquad != null)
            {
                Airfield currentFieldForSquad =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(currentFieldNameForSquad);
                
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

    public ArrayList<Company> getViableSquadrons(Campaign campaign) throws PWCGException 
    {
        ArrayList<Company> returnSquadList = new ArrayList<Company>();
        for (Company squadron : getAllSquadrons())
        {
            if (SquadronViability.isSquadronViable(squadron, campaign))
            {
                returnSquadList.add(squadron);
            }
        }

         return returnSquadList;
    }

    public List<Company> getViableAiSquadronsForCurrentMapAndSide(Campaign campaign, Side side) throws PWCGException 
    {
        List<Company> viableSquadronsForSide = SquadronReducer.reduceToSide(getViableSquadrons(campaign), side);
        List<Company> selectedSquadronsNoPlayer = SquadronReducer.reduceToAIOnly(viableSquadronsForSide, campaign);
        List<Company> selectedSquadronsForCurrentMap = SquadronReducer.reduceToCurrentMap(selectedSquadronsNoPlayer, campaign.getDate());
        
        return eliminateAnomalySquadrons(campaign, selectedSquadronsForCurrentMap);
    }

    public List<Company> getViableAiSquadronsForCurrentMapAndSideAndRole(Campaign campaign, List <PwcgRole> acceptableRoles, Side side) throws PWCGException 
    {
        List<Company> viableSquadronsForSide = SquadronReducer.reduceToSide(getViableSquadrons(campaign), side);
        List<Company> selectedSquadronsByRole = SquadronReducer.reduceToRole(viableSquadronsForSide, acceptableRoles, campaign.getDate());
        List<Company> selectedSquadronsNoPlayer = SquadronReducer.reduceToAIOnly(selectedSquadronsByRole, campaign);
        List<Company> selectedSquadronsForCurrentMap = SquadronReducer.reduceToCurrentMap(selectedSquadronsNoPlayer, campaign.getDate());
        
        return eliminateAnomalySquadrons(campaign, selectedSquadronsForCurrentMap);
    }

    private List<Company> eliminateAnomalySquadrons(Campaign campaign, List<Company> selectedSquadronsForCurrentMap) throws PWCGException
    {
        if (campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.RemoveNonHistoricalSquadronsKey) == 1)
        {
            List<Company> selectedSquadronsNoAnomalies = SquadronReducer.reduceToNoAnomalies(selectedSquadronsForCurrentMap, campaign.getDate());
            return selectedSquadronsNoAnomalies;
        }
        else
        {
            return selectedSquadronsForCurrentMap;
        }
    }

    public Company getClosestSquadron(Coordinate position, Date date) throws PWCGException 
    {
        Airfield airfield = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().findClosestAirfield(position);
        if (airfield != null)
        {
            Company squadron = getAnyActiveSquadronForAirfield(airfield, date);
            return squadron;
        }

        return null;
    }
}
