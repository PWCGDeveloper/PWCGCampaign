package pwcg.core.utils;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;

public class PositionFinder <T>
{
    public static final double ABSURDLY_LARGE_DISTANCE = 1000000.0;
    
    public T selectPositionWithinExpandingRadius(List<T> listOfPositions, Coordinate referenceLocation, double radius, double maxRadius) throws PWCGException
    {
        T selectedLocation = null;
                
        List<T> selectedLocations = findWithinExpandingRadius(listOfPositions, referenceLocation, radius, maxRadius);
        if (selectedLocations.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(selectedLocations.size());     
            selectedLocation = selectedLocations.get(index);
        }
        
        return selectedLocation;
    }

    public List<T> findWithinExpandingRadius(List<T> listOfPositions, Coordinate referenceLocation, double radius, double maxRadius) throws PWCGException
    {
        List<T> selectedLocations = new ArrayList<>();

        while (selectedLocations.isEmpty())
        {
            selectedLocations = findWithinRadius(listOfPositions, referenceLocation, radius);
            if (radius > maxRadius)
            {
                break;
            }
            
            radius += 10000.0;
        }
        
        return selectedLocations;
    }

    public List<T> findWithinRadius(List<T> listOfPositions, Coordinate referenceLocation, double radius) throws PWCGException
    {
        List<T> selectedLocations = new ArrayList<>();

        for (T t : listOfPositions)
        {
            if (t instanceof PWCGLocation)
            {
                PWCGLocation location = (PWCGLocation)t;
                double distance = MathUtils.calcDist(location.getPosition(), referenceLocation);
                if (distance <= radius)
                {
                    selectedLocations.add(t);
                }
            }
            else
            {
                throw new PWCGException ("Passed something that was not a fixed position to findWithinRadius");
            }
        }
        
        return selectedLocations;
    }

    public T selectClosestPosition(List<T> listOfPositions, Coordinate referenceLocation) throws PWCGException
    {
        T closestLocation = null;
        double closestLocationDistance = ABSURDLY_LARGE_DISTANCE;

        for (T t : listOfPositions)
        {
            if (t instanceof PWCGLocation)
            {
                PWCGLocation location = (PWCGLocation)t;
                double distance = MathUtils.calcDist(location.getPosition(), referenceLocation);
                if (distance <= closestLocationDistance)
                {
                    closestLocation = t;
                    closestLocationDistance = distance;
                }
            }
            else
            {
                throw new PWCGException ("Passed something that was not a fixed position to selectClosestPosition");
            }
        }
        
        return closestLocation;
    }
    

    public T selectDestinationPosition(List<T> listOfPositions, Coordinate referenceLocation) throws PWCGException
    {
        T destinationLocation = null;
        double currentDestinationDistance = 0;
        for (T t : listOfPositions)
        {
            if (t instanceof PWCGLocation)
            {
                PWCGLocation location = (PWCGLocation)t;
                double distance = MathUtils.calcDist(location.getPosition(), referenceLocation);
                if (distance > 100.0)
                {
                    if (currentDestinationDistance < distance)
                    {
                        destinationLocation = t;
                        currentDestinationDistance = distance;
                    }
                    
                    if (currentDestinationDistance > 30000.0)
                    {
                        break;
                    }
                }
            }
            else
            {
                throw new PWCGException ("Passed something that was not a fixed position to selectDestinationPosition");
            }
        }
        
        return destinationLocation;
    }
}
