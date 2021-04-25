package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PositionFinder;

public class AirfieldFinder
{
    private List<Airfield> airfields = new ArrayList<>();

    public AirfieldFinder(List<Airfield> airfields)
    {
        this.airfields = airfields;
    }    
    
    public List<Airfield> getWithinRadius(Coordinate referenceLocation, double radius) throws PWCGException
    {
        PositionFinder<Airfield> positionFinder = new PositionFinder<Airfield>();
        List<Airfield> airfieldsWithinRadius = positionFinder.findWithinExpandingRadius(airfields, referenceLocation, radius, radius * 3);
        return airfieldsWithinRadius;
    }

    public List<Airfield> getAirfieldsWithinRadiusBySide(Coordinate referenceLocation, Date date, double radius, Side side) throws PWCGException
    {
        PositionFinder<Airfield> positionFinder = new PositionFinder<Airfield>();
        List<Airfield> airfieldsForSide = getAirfieldsBySide(side, date);
        List<Airfield> airfieldsWithinRadius = positionFinder.findWithinExpandingRadius(airfieldsForSide, referenceLocation, radius, radius * 3);
        return airfieldsWithinRadius;
    }

    public Airfield findClosestAirfield(Coordinate referenceLocation) throws PWCGException
    {
        PositionFinder<Airfield> positionFinder = new PositionFinder<Airfield>();
        List<Airfield> airfieldsList = new ArrayList<>(airfields);
        Airfield closestAirfield = positionFinder.selectClosestPosition(airfieldsList, referenceLocation);
        return closestAirfield;
    }

    public Airfield getNearbyAirfield(Coordinate referenceLocation, double radius) throws PWCGException
    {
        PositionFinder<Airfield> positionFinder = new PositionFinder<Airfield>();
        List<Airfield> airfieldsList = new ArrayList<>(airfields);
        Airfield closestAirfield = positionFinder.selectPositionWithinExpandingRadius(airfieldsList, referenceLocation, radius, radius);
        return closestAirfield;
    }

    public Airfield getNearbyAirfieldForSide(Coordinate referenceLocation, double radius, Date date, Side side) throws PWCGException
    {
        PositionFinder<Airfield> positionFinder = new PositionFinder<Airfield>();
        List<Airfield> airfieldsForSide = getAirfieldsBySide(side, date);
        Airfield closestAirfield = positionFinder.selectPositionWithinExpandingRadius(airfieldsForSide, referenceLocation, radius, radius);
        return closestAirfield;
    }

    public Airfield findClosestAirfieldForSide(Coordinate referenceLocation, Date date, Side side) throws PWCGException
    {
        PositionFinder<Airfield> positionFinder = new PositionFinder<Airfield>();
        Airfield closestAirfield = positionFinder.selectClosestPosition(getAirfieldsBySide(side, date), referenceLocation);
        return closestAirfield;
    }
    
    public List<Airfield> getAirfieldsBySide(Side side, Date date) throws PWCGException 
    {
        List<Airfield>airfieldsForSide = new ArrayList<Airfield>();
        for (Airfield airfield : airfields)
        {
            if (airfield.determineCountryOnDate(date).getSide() == side)
            {
                airfieldsForSide.add(airfield);
            }
        }
            
        return airfieldsForSide;
    }

}
