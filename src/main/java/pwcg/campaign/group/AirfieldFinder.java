package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PositionFinder;

public class AirfieldFinder
{
    private List<IAirfield> airfields = new ArrayList<>();

    public AirfieldFinder(List<IAirfield> airfields)
    {
        this.airfields = airfields;
    }    
    
    public List<IAirfield> getWithinRadius(Coordinate referenceLocation, double radius) throws PWCGException
    {
        PositionFinder<IAirfield> positionFinder = new PositionFinder<IAirfield>();
        List<IAirfield> airfieldsWithinRadius = positionFinder.findWithinExpandingRadius(airfields, referenceLocation, radius, radius * 3);
        return airfieldsWithinRadius;
    }

    public List<IAirfield> getAirfieldsWithinRadiusBySide(Coordinate referenceLocation, Date date, double radius, Side side) throws PWCGException
    {
        PositionFinder<IAirfield> positionFinder = new PositionFinder<IAirfield>();
        List<IAirfield> airfieldsForSide = getAirfieldsBySide(side, date);
        List<IAirfield> airfieldsWithinRadius = positionFinder.findWithinExpandingRadius(airfieldsForSide, referenceLocation, radius, radius * 3);
        return airfieldsWithinRadius;
    }

    public IAirfield findClosestAirfield(Coordinate referenceLocation) throws PWCGException
    {
        PositionFinder<IAirfield> positionFinder = new PositionFinder<IAirfield>();
        List<IAirfield> airfieldsList = new ArrayList<>(airfields);
        IAirfield closestAirfield = positionFinder.selectClosestPosition(airfieldsList, referenceLocation);
        return closestAirfield;
    }

    public IAirfield getNearbyAirfield(Coordinate referenceLocation, double radius) throws PWCGException
    {
        PositionFinder<IAirfield> positionFinder = new PositionFinder<IAirfield>();
        List<IAirfield> airfieldsList = new ArrayList<>(airfields);
        IAirfield closestAirfield = positionFinder.selectPositionWithinExpandingRadius(airfieldsList, referenceLocation, radius, radius);
        return closestAirfield;
    }

    public IAirfield getNearbyAirfieldForSide(Coordinate referenceLocation, double radius, Date date, Side side) throws PWCGException
    {
        PositionFinder<IAirfield> positionFinder = new PositionFinder<IAirfield>();
        List<IAirfield> airfieldsForSide = getAirfieldsBySide(side, date);
        IAirfield closestAirfield = positionFinder.selectPositionWithinExpandingRadius(airfieldsForSide, referenceLocation, radius, radius);
        return closestAirfield;
    }

    public IAirfield findClosestAirfieldForSide(Coordinate referenceLocation, Date date, Side side) throws PWCGException
    {
        PositionFinder<IAirfield> positionFinder = new PositionFinder<IAirfield>();
        IAirfield closestAirfield = positionFinder.selectClosestPosition(getAirfieldsBySide(side, date), referenceLocation);
        return closestAirfield;
    }
    
    public List<IAirfield> getAirfieldsBySide(Side side, Date date) throws PWCGException 
    {
        List<IAirfield>airfieldsForSide = new ArrayList<IAirfield>();
        for (IAirfield airfield : airfields)
        {
            if (airfield.createCountry(date).getSide() == side)
            {
                airfieldsForSide.add(airfield);
            }
        }
            
        return airfieldsForSide;
    }

}
