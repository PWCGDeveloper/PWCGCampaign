package pwcg.core.location;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;

public class CoordinateBox
{
    protected Coordinate sw = new Coordinate();
    protected Coordinate ne = new Coordinate();
    protected Coordinate se = new Coordinate();
    protected Coordinate nw = new Coordinate();
    protected Coordinate center = new Coordinate();
    protected Coordinate north = new Coordinate();
    protected Coordinate south = new Coordinate();
    protected Coordinate east = new Coordinate();
    protected Coordinate west = new Coordinate();
    
    public static CoordinateBox coordinateBoxFromCorners (Coordinate sw, Coordinate ne) throws PWCGException
    {
        CoordinateBox coordinateBox = new CoordinateBox();
        coordinateBox.sw = sw;
        coordinateBox.ne = ne;
        coordinateBox.calcBoxImportantLocations();        
        return coordinateBox;
    }
    
    public static CoordinateBox coordinateBoxFromCenter(Coordinate center, int boxSize) throws PWCGException
    {
        CoordinateBox coordinateBox = new CoordinateBox();
        coordinateBox.calculateSW(center, boxSize);
        coordinateBox.calculateNE(center, boxSize);
        coordinateBox.calcBoxImportantLocations();
        return coordinateBox;
    }
    
    public static CoordinateBox coordinateBoxFromTwoCoordinates (Coordinate coordinate1, Coordinate coordinate2) throws PWCGException
    {
        CoordinateBox coordinateBox = new CoordinateBox();
        coordinateBox.calculateSWCornerFromTwoCoordinates(coordinate1, coordinate2);
        coordinateBox.calculateNECornerFromTwoCoordinates(coordinate1, coordinate2);
        coordinateBox.calcBoxImportantLocations();
        return coordinateBox;
    }
    
    public static CoordinateBox coordinateBoxFromCoordinateList (List<Coordinate> coordinates) throws PWCGException
    {
        CoordinateBox coordinateBox = new CoordinateBox();
        coordinateBox.setBoxCornersFromCoordinates(coordinates);
        coordinateBox.calcBoxImportantLocations();
        return coordinateBox;
    }

    public static CoordinateBox coordinateBoxFromFlights (List<IFlight> flights) throws PWCGException
    {
        List<Coordinate> flightWaypointCoordinates = new ArrayList<>();
        for (IFlight flight : flights)
        {
            for (MissionPoint missionPoint : flight.getWaypointPackage().getFlightMissionPoints())
            {
                flightWaypointCoordinates.add(missionPoint.getPosition());
            }
        }        
        CoordinateBox coordinateBox = coordinateBoxFromCoordinateList(flightWaypointCoordinates);
        return coordinateBox;
    }

    public void setBoxCornersFromCoordinates(List<Coordinate> coordinates) throws PWCGException
    {
        double lowerLeftX = 10000000.0;
        double lowerLeftZ = 10000000.0;

        double upperRightX = 0.0;
        double upperRightZ = 0.0;

        for (Coordinate coordinate : coordinates)
        {
            if (coordinate.getXPos() < lowerLeftX)
            {
                lowerLeftX = coordinate.getXPos();
                sw.setXPos(lowerLeftX);
            }
            if (coordinate.getZPos() < lowerLeftZ)
            {
                lowerLeftZ = coordinate.getZPos();
                sw.setZPos(lowerLeftZ);
            }
            if (coordinate.getXPos() > upperRightX)
            {
                upperRightX = coordinate.getXPos();
                ne.setXPos(upperRightX);
            }
            if (coordinate.getZPos() > upperRightZ)
            {
                upperRightZ = coordinate.getZPos();
                ne.setZPos(upperRightZ);
            }
        }
    }

    public CoordinateBox expandBox(int meters) throws PWCGException
    {
        CoordinateBox coordinateBox = this.copy();
        if (meters < 0)
        {
            if ((getBoxWidth() < Math.abs(meters)) || getBoxHeight() < Math.abs(meters))
            {
                return coordinateBox;
            }
        }
        
        sw.setXPos(sw.getXPos() - meters);
        sw.setZPos(sw.getZPos() - meters);

        ne.setXPos(ne.getXPos() + meters);
        ne.setZPos(ne.getZPos() + meters);
        
        calcBoxImportantLocations();
        
        return coordinateBox;
    }
    
    public boolean isInBox (Coordinate itemPosition)
    {
        boolean isInBox = false;

        if (itemPosition.getXPos() >= sw.getXPos() &&
            itemPosition.getXPos() <= ne.getXPos() &&
            itemPosition.getZPos() >= sw.getZPos() &&
            itemPosition.getZPos() <= ne.getZPos())
        {
            isInBox = true;
        }
        
        return isInBox;
    }

    public Coordinate getCoordinateInBox() throws PWCGException 
    {
        Coordinate targetCoord = new Coordinate(); 
        
        int zDistance = Math.abs(Double.valueOf(ne.getZPos() - sw.getZPos()).intValue());
        int xDistance = Math.abs(Double.valueOf(ne.getXPos() - sw.getXPos()).intValue());
        
        int zPos = Double.valueOf(ne.getZPos()).intValue() + RandomNumberGenerator.getRandom(zDistance);
        int xPos = Double.valueOf(sw.getXPos()).intValue() + RandomNumberGenerator.getRandom(xDistance);
        
        targetCoord.setZPos(zPos);
        targetCoord.setXPos(xPos);

        return targetCoord;
    }

    protected void calcBoxImportantLocations() throws PWCGException
    {
        calcSouthEast();
        calcNorthWest();
        calcCenterOfBox();
        calcNorthOfBox();
        calcSouthOfBox();
        calcEastOfBox();
        calcWestOfBox();
    }
    
    protected Coordinate calcSouthEast() throws PWCGException
    {
        se = new Coordinate();
        se.setXPos(sw.getXPos());
        se.setZPos(ne.getZPos());
        return se;
    }

    protected Coordinate calcNorthWest() throws PWCGException
    {
        nw = new Coordinate();
        nw.setXPos(ne.getXPos());
        nw.setZPos(sw.getZPos());
        return nw;
    }

    protected Coordinate calcCenterOfBox() throws PWCGException
    {
        double xDistance = ne.getXPos() - sw.getXPos();
        double zDistance = ne.getZPos() - sw.getZPos();
        
        center = new Coordinate();
        center.setXPos(sw.getXPos() + (xDistance / 2));
        center.setZPos(sw.getZPos() + (zDistance / 2));
        return center;
    }
    
    protected Coordinate calcNorthOfBox() throws PWCGException
    {
        north = new Coordinate();
        north.setXPos(ne.getXPos());
        north.setZPos(center.getZPos());
        return north;
    }

    protected Coordinate calcSouthOfBox() throws PWCGException
    {
        south = new Coordinate();
        south.setXPos(sw.getXPos());
        south.setZPos(center.getZPos());
        return south;
    }

    protected Coordinate calcEastOfBox() throws PWCGException
    {
        east = new Coordinate();
        east.setXPos(center.getXPos());
        east.setZPos(ne.getZPos());
        return east;
    }

    protected Coordinate calcWestOfBox() throws PWCGException
    {
        west = new Coordinate();
        west.setXPos(center.getXPos());
        west.setZPos(sw.getZPos());
        return west;
    }

    protected void calculateSWCornerFromTwoCoordinates (Coordinate coordinate1, Coordinate coordinate2) throws PWCGException
    {
        sw = coordinate1.copy();
        if (coordinate2.getXPos() < sw.getXPos())
        {
            sw.setXPos(coordinate2.getXPos());
        }
        if (coordinate2.getZPos() < sw.getZPos())
        {
            sw.setZPos(coordinate2.getZPos());
        }
    }

    protected void calculateNECornerFromTwoCoordinates (Coordinate coordinate1, Coordinate coordinate2) throws PWCGException
    {
        ne = coordinate1.copy();
        if (coordinate2.getXPos() > ne.getXPos())
        {
            ne.setXPos(coordinate2.getXPos());
        }
        if (coordinate2.getZPos() > ne.getZPos())
        {
            ne.setZPos(coordinate2.getZPos());
        }
    }

    protected Coordinate calculateSW (Coordinate center, int boxSize) throws PWCGException
    {
        sw = MathUtils.calcNextCoord(center, 270, boxSize / 2);
        sw = MathUtils.calcNextCoord(sw, 180, boxSize / 2);
        return sw;
    }
    
    protected Coordinate calculateNE (Coordinate center, int boxSize) throws PWCGException
    {
        ne = MathUtils.calcNextCoord(center, 90, boxSize / 2);
        ne = MathUtils.calcNextCoord(ne, 0, boxSize / 2);
        return ne;
    }
    
    private CoordinateBox copy()
    {
        CoordinateBox coordinateBox = new CoordinateBox();
        coordinateBox.sw = this.sw.copy();
        coordinateBox.ne = this.ne.copy();
        coordinateBox.center = this.center.copy();
        return coordinateBox;
    }

    public Coordinate chooseCoordinateWithinBox() throws PWCGException
    {
        int distanceX = Double.valueOf(ne.getXPos() - sw.getXPos()).intValue();
        int distanceZ = Double.valueOf(ne.getZPos() - sw.getZPos()).intValue();
        
        int offsetX = RandomNumberGenerator.getRandom(distanceX);
        int offsety = RandomNumberGenerator.getRandom(distanceZ);
        
        Coordinate approximatePosition = sw.copy();
        approximatePosition.setXPos(approximatePosition.getXPos() + offsetX);
        approximatePosition.setZPos(approximatePosition.getZPos() + offsety);
        return approximatePosition;
    }
 
    public double getLongestEdge()
    {
        double ewEdge = ne.getZPos() - sw.getZPos();
        double nsEdge = ne.getXPos() - sw.getXPos();
        
        if (ewEdge > nsEdge)
        {
            return ewEdge;
        }
        
        return nsEdge;
    }

    public void addHeight(double extraHeight) throws PWCGException
    {
        sw.setXPos(sw.getXPos() - extraHeight);
        ne.setXPos(ne.getXPos() + extraHeight);
    }

    public void addWidth(double extraWidth) throws PWCGException
    {
        sw.setZPos(sw.getZPos() - extraWidth);
        ne.setZPos(ne.getZPos() + extraWidth);
    }

    public double getBoxHeight() throws PWCGException
    {
        double height = ne.getXPos() - sw.getXPos();
        return height;
    }

    public double getBoxWidth() throws PWCGException
    {
        double width = ne.getZPos() - sw.getZPos();
        return width;
    }
    
    public double getAreaRadius() throws PWCGException
    {
        double areaDiameter = getBoxWidth();
        if (getBoxHeight() > areaDiameter)
        {
            areaDiameter = getBoxHeight();
        }
        return areaDiameter / 2.0;
    }
    
    public Coordinate getSW()
    {
        return sw.copy();
    }

    public Coordinate getNE()
    {
        return ne.copy();
    }

    public Coordinate getSE()
    {
        return se.copy();
    }

    public Coordinate getNW()
    {
        return nw.copy();
    }

    public Coordinate getCenter()
    {
        return center.copy();
    }

    public Coordinate getNorth()
    {
        return north.copy();
    }

    public Coordinate getSouth()
    {
        return south.copy();
    }

    public Coordinate getEast()
    {
        return east.copy();
    }

    public Coordinate getWest()
    {
        return west.copy();
    }
}
