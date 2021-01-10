package pwcg.mission.ground.org;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;

public class GroundUnitBox
{
    public static CoordinateBox buildBattleBox(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        List<Coordinate> coordinates = new ArrayList<>();
        for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
        {
            coordinates.add(groundUnit.getPosition());
        }

        CoordinateBox battleBox = CoordinateBox.coordinateBoxFromCoordinateList(coordinates);
        battleBox.expandBox(1000);
        return battleBox;
    }

}
