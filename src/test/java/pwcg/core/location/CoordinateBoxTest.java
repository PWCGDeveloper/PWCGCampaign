package pwcg.core.location;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.core.exception.PWCGException;

public class CoordinateBoxTest
{
    @Test
    public void testExpandingCoordinateBox() throws PWCGException
    {
        CoordinateBox coordinateBox = CoordinateBox.coordinateBoxFromCorners(new Coordinate(10000,  0, 10000), new Coordinate(20000, 0, 20000));
        List<Coordinate> expansionCoordinates = new ArrayList<>();
        expansionCoordinates.add(new Coordinate(10000,  0, 10000));
        expansionCoordinates.add(new Coordinate(9000,  0, 9000));
        expansionCoordinates.add(new Coordinate(7000,  0, 15000));
        expansionCoordinates.add(new Coordinate(13000,  0, 10000));
        expansionCoordinates.add(new Coordinate(12000,  0, 9000));
        coordinateBox.expandBoxCornersFromCoordinates(expansionCoordinates);
        
        assert(coordinateBox.getSW().getXPos() == 7000);
        assert(coordinateBox.getSW().getZPos() == 9000);
        
        assert(coordinateBox.getNE().getXPos() == 20000);
        assert(coordinateBox.getNE().getZPos() == 20000);
    }
}
