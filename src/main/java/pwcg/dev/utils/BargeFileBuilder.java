package pwcg.dev.utils;

import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationSet;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;

public class BargeFileBuilder
{
    private LocationSet bargeLocations = new LocationSet("Stalingrad Barges");

    public static void main(String[] args)
    {
        try
        {
            BargeFileBuilder bargeFileBuilder = new BargeFileBuilder();
            bargeFileBuilder.makeBargeLocations();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void makeBargeLocations() throws PWCGException
    {
        makeBargeLocation(new Coordinate(344130, 0, 352431), new Orientation(35));
        makeBargeLocation(new Coordinate(329164, 0, 340561), new Orientation(27));
        makeBargeLocation(new Coordinate(317465, 0, 330212), new Orientation(30));
        makeBargeLocation(new Coordinate(303527, 0, 321485), new Orientation(30));
        makeBargeLocation(new Coordinate(285721, 0, 315332), new Orientation(15));
        makeBargeLocation(new Coordinate(255451, 0, 308791), new Orientation(28));
        makeBargeLocation(new Coordinate(197656, 0, 287518), new Orientation(12));
        makeBargeLocation(new Coordinate(165575, 0, 276146), new Orientation(33));
        makeBargeLocation(new Coordinate(146794, 0, 265252), new Orientation(8));
        makeBargeLocation(new Coordinate(133362, 0, 256505), new Orientation(30));
        makeBargeLocation(new Coordinate(127570, 0, 251493), new Orientation(48));
        makeBargeLocation(new Coordinate(115915, 0, 250895), new Orientation(326));
        makeBargeLocation(new Coordinate(112575, 0, 263730), new Orientation(304));
        makeBargeLocation(new Coordinate(107623, 0, 271916), new Orientation(90));
        
        LocationIOJson.writeJson("", "Test.json", bargeLocations);
    }
    
    private void makeBargeLocation(Coordinate bargeCoordinate, Orientation bargeorientation)
    {
        PWCGLocation location = new PWCGLocation();
        location.setName("Barge Location");
        location.setPosition(bargeCoordinate);
        location.setOrientation(bargeorientation);
        bargeLocations.addLocation(location);
    }
}
