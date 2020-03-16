package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.factory.AirfieldConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class AirfieldManager
{
    private Map<String, IAirfield> airfields = new TreeMap<String, IAirfield>();

    public AirfieldManager()
    {
    }

    public void reset() throws PWCGMissionGenerationException
    {
        airfields.clear();
    }

    public void configure(String mapName) throws PWCGException
    {
        IAirfieldConfiguration airfieldConfiguration = AirfieldConfigurationFactory.createAirfieldConfiguration();
        airfields = airfieldConfiguration.configure(mapName);
    }

    public static List<FrontMapIdentifier> getMapIdForAirfield(String airfieldName) throws PWCGException
    {
        List<FrontMapIdentifier> mapsForAirfield = new ArrayList<FrontMapIdentifier>();

        for (PWCGMap map : PWCGContext.getInstance().getAllMaps())
        {
            AirfieldManager airfieldManager = map.getAirfieldManager();
            if (airfieldManager != null)
            {
                IAirfield field = airfieldManager.getAirfield(airfieldName);
                if (field != null)
                {
                    mapsForAirfield.add(map.getMapIdentifier());
                }
            }
        }

        if (mapsForAirfield.isEmpty())
        {
            throw new PWCGException("No map was associated with airfield: " + airfieldName);
        }

        return mapsForAirfield;
    }

    public void addField(IAirfield field)
    {
        if (airfields.containsKey(field.getName()))
        {
            airfields.remove(field.getName());
        }

        airfields.put(field.getName(), field);
        PWCGLogger.log(LogLevel.DEBUG, "Add to allied: " + field.getName());
    }

    public IAirfield getAirfield(String airfieldName)
    {
        IAirfield airfield = getAirfieldNoClone(airfieldName);

        if (airfield != null)
        {
            return airfield.copy();
        }

        return null;
    }

    private IAirfield getAirfieldNoClone(String airfieldName)
    {
        IAirfield airfield = null;

        if (airfields.containsKey(airfieldName))
        {
            airfield = airfields.get(airfieldName);
        }
        else
        {
            String largeAirfieldName = airfieldName + " 1";
            if (airfields.containsKey(largeAirfieldName))
            {
                airfield = airfields.get(largeAirfieldName);
            }
            else
            {
                return null;
            }
        }

        return airfield;
    }

    public boolean hasAirfield(String airfieldName)
    {
        boolean hasAirfield = false;

        IAirfield field = getAirfield(airfieldName);
        if (field != null)
        {
            hasAirfield = true;
        }

        return hasAirfield;
    }

    public List<IAirfield> getAirFieldsForSide(Date date, Side side) throws PWCGException
    {
        ArrayList<IAirfield> fieldsForSide = new ArrayList<IAirfield>();

        for (IAirfield airfield : airfields.values())
        {
            if (airfield.createCountry(date).getSide() == side)
            {
                fieldsForSide.add(airfield);
            }
        }

        return fieldsForSide;
    }
    
    public AirfieldFinder getAirfieldFinder()
    {
        AirfieldFinder airfieldFinder = new AirfieldFinder(new ArrayList<>(airfields.values()));
        return airfieldFinder;
    }
    
    public Map<String, IAirfield> getAllAirfields()
    {
        return airfields;
    }

}
