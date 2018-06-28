package pwcg.core.location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.CountryDesignator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.core.utils.RandomNumberGenerator;

public class LocationSet
{
	String locationSetName = "";
	private List <PWCGLocation> locations= new ArrayList<>();

	public LocationSet(String locationSetName)
	{
		this.locationSetName = locationSetName;
	}
	
	public String getLocationSetName()
	{
		return locationSetName;
	}

	public void setLocationSetName(String locationSetName)
	{
		this.locationSetName = locationSetName;
	}

	public List<PWCGLocation> getLocations()
	{
		return locations;
	}

	public void setLocations(List<PWCGLocation> locationSet)
	{
		this.locations = locationSet;
	}
	
	public void addLocation(PWCGLocation location)
	{
		locations.add(location);
	}
	
	public void reset(PWCGLocation location)
	{
		locations.clear();
	}
	
	public boolean hasLocations(Coordinate referenceLocation, double radius)
	{
	    List<PWCGLocation> selectedLocations = getLocationsWithinRadius(referenceLocation, radius);
		if (selectedLocations.isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public List<PWCGLocation> getLocationsWithinRadius(Coordinate referenceLocation, double radius) 
    {
        List<PWCGLocation> selectedLocations = new ArrayList<>();
        for (PWCGLocation location : locations)
        {
            double distance = MathUtils.calcDist(referenceLocation, location.getPosition());

            if (distance <= radius)
            {
                selectedLocations.add(location);
            }
        }

        return selectedLocations;
    }

    public PWCGLocation getSelectedLocationWithinRadius(Coordinate referenceLocation, double radius) 
    {
        List<PWCGLocation> selectedLocations = getLocationsWithinRadius(referenceLocation, radius);
        PWCGLocation selectedBargePosition = selectPositionFromList(selectedLocations);
        return selectedBargePosition;
    }
	
	public List<PWCGLocation> getLocationsWithinRadiusBySide(Side side, Date date, Coordinate referenceLocation, double radius) throws PWCGException 
    {
        List<PWCGLocation> selectedLocations = new ArrayList<>();
        List<PWCGLocation> locationsWithinRadius = getLocationsWithinRadius(referenceLocation, radius);
        
    	CountryDesignator countryDesignator = new CountryDesignator();
        for (PWCGLocation location : locationsWithinRadius)
        {
        	ICountry countryOwningLocation = countryDesignator.determineCountry(location.getPosition(), date);
        	if (countryOwningLocation.getSide() == side)
        	{
        		selectedLocations.add(location);
        	}
        }
        
        return selectedLocations;
    }
	
    public PWCGLocation getSelectedLocationWithinRadiusBySide(Side side, Date date, Coordinate referenceLocation, double radius) throws PWCGException 
    {
        List<PWCGLocation> selectedLocations = getLocationsWithinRadiusBySide(side, date, referenceLocation, radius);
        PWCGLocation selectedBargePosition = selectPositionFromList(selectedLocations);
        return selectedBargePosition;
    }
    
    public PWCGLocation findClosestLocation (Coordinate referenceLocation) throws PWCGException 
    {
        PositionFinder<PWCGLocation> positionFinder = new PositionFinder<PWCGLocation>();
        PWCGLocation closestLocation = positionFinder.selectClosestPosition(locations, referenceLocation);
        return closestLocation;
    }

    public PWCGLocation findClosestLocationForSide(Coordinate referenceLocation, Date date, Side side) throws PWCGException
    {
        PositionFinder<PWCGLocation> positionFinder = new PositionFinder<PWCGLocation>();
        PWCGLocation closestLocation = positionFinder.selectClosestPosition(getLocationsBySide(date, side), referenceLocation);
        return closestLocation;
    }

	private PWCGLocation selectPositionFromList(List<PWCGLocation> selectedLocations)
	{
		PWCGLocation selectedBargePosition = null;
        if (!selectedLocations.isEmpty())
        {
            int index = RandomNumberGenerator.getRandom(selectedLocations.size());
            selectedBargePosition = selectedLocations.get(index);
        }
		return selectedBargePosition;
	}
	
	private List<PWCGLocation> getLocationsBySide(Date date, Side side) throws PWCGException 
    {
        List<PWCGLocation> selectedLocations = new ArrayList<>();        
        CountryDesignator countryDesignator = new CountryDesignator();
        for (PWCGLocation location : locations)
        {
            ICountry countryOwningLocation = countryDesignator.determineCountry(location.getPosition(), date);
            if (countryOwningLocation.getSide() == side)
            {
                selectedLocations.add(location);
            }
        }
        
        return selectedLocations;
    }
	    
}
