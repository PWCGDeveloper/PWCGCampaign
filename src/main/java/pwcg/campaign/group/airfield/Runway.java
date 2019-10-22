package pwcg.campaign.group.airfield;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;

public class Runway
{
    private Coordinate startPos;
    private Coordinate endPos;
    private PWCGLocation parkingLocation;

    private List<Coordinate> taxiToStart = new ArrayList<>();
    private List<Coordinate> taxiFromEnd = new ArrayList<>();

    public Runway copy()
    {
        Runway clone = new Runway();

        clone.startPos = startPos;
        clone.endPos = endPos;
        clone.parkingLocation = parkingLocation;

        for (Coordinate t : taxiToStart)
            clone.taxiToStart.add(t.copy());
        for (Coordinate t : taxiFromEnd)
            clone.taxiFromEnd.add(t.copy());

        return clone;
    }

    public double getHeading() throws PWCGException
    {
        return MathUtils.calcAngle(startPos, endPos);
    }

    public Runway invert()
    {
        Runway inv = new Runway();

        inv.startPos = endPos;
        inv.endPos = startPos;
        inv.parkingLocation = parkingLocation;

        for (Coordinate t : taxiFromEnd)
            inv.taxiToStart.add(0, t.copy());
        for (Coordinate t : taxiToStart)
            inv.taxiFromEnd.add(0, t.copy());

        return inv;
    }

    public List<Coordinate> getTaxiToStart()
    {
        return taxiToStart;
    }
    
    public void setStartPos(Coordinate startPos)
    {
        this.startPos = startPos;
    }

    public void setEndPos(Coordinate endPos)
    {
        this.endPos = endPos;
    }

    public void setParkingLocation(PWCGLocation parkingLocation)
    {
        this.parkingLocation = parkingLocation;
    }

    public List<Coordinate> getTaxiFromEnd()
    {
        return taxiFromEnd;
    }

    public void setTaxiToStart(List<Coordinate> taxiToStart)
    {
        this.taxiToStart = taxiToStart;
    }

    public void setTaxiFromEnd(List<Coordinate> taxiFromEnd)
    {
        this.taxiFromEnd = taxiFromEnd;
    }

    public Coordinate getStartPos()
    {
        return startPos;
    }

    public Coordinate getEndPos()
    {
        return endPos;
    }

    public PWCGLocation getParkingLocation()
    {
        return parkingLocation;
    }
    
    
}
