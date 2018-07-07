package pwcg.campaign.group;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AirfieldInEnemyTerritory
{
	
	private Map<String, String> badAirfields = new HashMap<>();
	private boolean acceptNeutralPlacement = false;
    
	AirfieldInEnemyTerritory(boolean acceptNeutralPlacement)
	{
		this.acceptNeutralPlacement = acceptNeutralPlacement;
	}
	
	protected void findEnemy(FrontMapIdentifier mapId, Date startDate, Date endDate)
    {
        try
        {
            while (startDate.before(endDate))
            {                
                determineProperPlacementForDate(mapId, startDate);
                startDate = DateUtils.advanceTimeDays(startDate, 1);
            }            
            printBadSquadrons();
            
            assert(badAirfields.size() == 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	private void determineProperPlacementForDate(FrontMapIdentifier mapId, Date startDate) throws PWCGException
	{
		SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
		for (Squadron squadron : squadronManager.getActiveSquadrons(startDate))
		{
		    determineSquadronIsOnCorrectSide(mapId, startDate, squadron);
		}
	}

	private void determineSquadronIsOnCorrectSide(FrontMapIdentifier mapId, Date startDate, Squadron squadron) throws PWCGException
	{
		IAirfield squadronField = squadron.determineCurrentAirfieldCurrentMap(startDate);
		if (squadronField != null)
		{
		    List<FrontMapIdentifier> mapsForAirfield = AirfieldManager.getMapIdForAirfield(squadronField.getName());
		    for (FrontMapIdentifier mapForAirfield : mapsForAirfield)
		    {
		        if (mapForAirfield == mapId)
		        {
		            noteBadlyPlacedSquadron(startDate, squadron, squadronField, mapForAirfield);
		        }
		    }
		}
	}

	private void noteBadlyPlacedSquadron(Date startDate, Squadron squadron, IAirfield squadronField,
	        FrontMapIdentifier mapForAirfield) throws PWCGException
	{
		ICountry squadronCountry = squadron.determineSquadronCountry(startDate);
		ICountry airfieldCountry = squadronField.createCountry(startDate);
		if (isBadlyPlaced(squadronCountry, airfieldCountry))
		{
			String key = formKey(squadron, squadronField);
			String message = 
					"Badly placed squadron " + squadron.determineDisplayName(startDate) + 
					"\n   	field " + squadronField.getName() + 
					"\n     map " + mapForAirfield + 
					"\n     date " + DateUtils.getDateStringDashDelimitedYYYYMMDD(startDate) + 
					"\n     side " + squadronField.createCountry(startDate).getCountryName();
			badAirfields.put(key, message);
		}
	}

	private boolean isBadlyPlaced(ICountry squadronCountry, ICountry airfieldCountry)
	{
		if (squadronCountry.isSameSide(airfieldCountry))
		{
			return false;
		}
		
		if (airfieldCountry.getCountry() == Country.NEUTRAL)
		{
			if (acceptNeutralPlacement)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private void printBadSquadrons()
	{
		for (String message : badAirfields.values())
		{
			System.out.println(message);
		}
	}
	
	private String formKey (Squadron squadron, IAirfield squadronField)
	{
    	return squadron.getSquadronId() + " at " + squadronField.getName();
	}
}
