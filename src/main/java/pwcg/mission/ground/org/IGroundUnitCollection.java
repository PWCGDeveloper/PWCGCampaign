package pwcg.mission.ground.org;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.IUnit;
import pwcg.mission.target.TacticalTarget;

public interface IGroundUnitCollection extends IUnit
{
    Coordinate getPosition();
    TacticalTarget getTargetType();
    void write(BufferedWriter writer) throws PWCGException;
    Coordinate getTargetCoordinatesFromGroundUnits(Side determineEnemySide) throws PWCGException;
    List<IGroundUnit> getGroundUnits();
    void addGroundUnit(IGroundUnit groundUnit);
    void finishGroundUnitCollection();
    void merge(IGroundUnitCollection relatedGroundCollection);
    List<IGroundUnit> getGroundUnitsForSide(Side side) throws PWCGException;
    GroundUnitCollectionType getGroundUnitCollectionType();
}
