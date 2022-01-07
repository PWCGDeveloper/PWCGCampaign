package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.factory.AirfieldConfigurationFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.AirfieldConfiguration;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.PositionFinder;

public class AirfieldManager
{
    private Map<String, Airfield> airfields = new TreeMap<String, Airfield>();

    public AirfieldManager()
    {
    }

    public void reset() throws PWCGMissionGenerationException
    {
        airfields.clear();
    }

    public void configure(FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        AirfieldConfiguration airfieldConfiguration = AirfieldConfigurationFactory.createAirfieldConfiguration();
        airfields = airfieldConfiguration.configure(mapIdentifier);
    }

    public static List<FrontMapIdentifier> getMapIdForAirfield(String airfieldName) throws PWCGException
    {
        List<FrontMapIdentifier> mapsForAirfield = new ArrayList<FrontMapIdentifier>();

        for (PWCGMap map : PWCGContext.getInstance().getAllMaps())
        {
            AirfieldManager airfieldManager = map.getAirfieldManager();
            if (airfieldManager != null)
            {
                Airfield field = airfieldManager.getAirfield(airfieldName);
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

    public void addField(Airfield field)
    {
        if (airfields.containsKey(field.getName()))
        {
            airfields.remove(field.getName());
        }

        airfields.put(field.getName(), field);
        PWCGLogger.log(LogLevel.DEBUG, "Add to allied: " + field.getName());
    }

    public Airfield getAirfield(String airfieldName)
    {
        Airfield airfield = getAirfieldNoClone(airfieldName);

        if (airfield != null)
        {
            return airfield.copy();
        }

        return null;
    }

    private Airfield getAirfieldNoClone(String airfieldName)
    {
        Airfield airfield = null;

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

        Airfield field = getAirfield(airfieldName);
        if (field != null)
        {
            hasAirfield = true;
        }

        return hasAirfield;
    }

    public List<Airfield> getAirFieldsForSide(Date date, Side side) throws PWCGException
    {
        ArrayList<Airfield> fieldsForSide = new ArrayList<Airfield>();

        for (Airfield airfield : airfields.values())
        {
            if (airfield.determineCountryOnDate(date).getSide() == side)
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
    
    public Map<String, Airfield> getAllAirfields()
    {
        return airfields;
    }

    public Airfield getClosestAirfield(Coordinate clickCoordinate)
    {
        Airfield closestAirfield = null;
        double closestDistance = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        for (Airfield airfield : airfields.values())
        {
            double distanceFromClick = MathUtils.calcDist(airfield.getPosition(), clickCoordinate);
            if (distanceFromClick < closestDistance)
            {
                closestAirfield = airfield;
                closestDistance = distanceFromClick;
            }
        }
        return closestAirfield;
    }
}
