package pwcg.mission.flight;

import java.util.List;

import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnitCollection;

public interface ILinkedGroundUnits
{

    void addLinkedGroundUnit(IGroundUnitCollection groundUnit);
    List<IGroundUnitCollection> getLinkedGroundUnits();
    IGroundUnitCollection getLinkedGroundUnitByType(GroundUnitCollectionType groundUnitCollectionType);
}