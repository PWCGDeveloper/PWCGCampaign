package pwcg.mission.ground.org;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.target.TargetType;

public interface IGroundUnitCollection
{
    Coordinate getPosition() throws PWCGException;
    Coordinate getPosition(Side side) throws PWCGException;
    TargetType getTargetType();
    void write(BufferedWriter writer) throws PWCGException;
    Coordinate getTargetCoordinatesFromGroundUnits(Side determineEnemySide) throws PWCGException;
    List<IGroundUnit> getGroundUnits();
    IGroundUnit getPrimaryGroundUnit();
    void addGroundUnit(IGroundUnit groundUnit);
    void setPrimaryGroundUnit(IGroundUnit groundUnit);
    void finishGroundUnitCollection() throws PWCGException;
    void merge(IGroundUnitCollection relatedGroundCollection);
    List<IGroundUnit> getGroundUnitsForSide(Side side) throws PWCGException;
    GroundUnitCollectionType getGroundUnitCollectionType();
    void validate() throws PWCGException;
    void triggerOnPlayerProximity(Mission mission) throws PWCGException;
    void triggerOnPlayerOrCoalitionProximity(Mission mission) throws PWCGException;
    int getUnitCount();
}
