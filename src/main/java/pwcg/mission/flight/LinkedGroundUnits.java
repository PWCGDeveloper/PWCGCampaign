package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class LinkedGroundUnits implements ILinkedGroundUnits
{
    private List<IGroundUnitCollection> linkedGroundUnits = new ArrayList<>();
    
    @Override
    public void addLinkedGroundUnit(IGroundUnitCollection groundUnit)
    {
        linkedGroundUnits.add(groundUnit);
    }
    
    @Override
    public List<IGroundUnitCollection> getLinkedGroundUnits()
    {
        return linkedGroundUnits;
    }

    @Override
    public IGroundUnitCollection getLinkedGroundUnitByType(GroundUnitCollectionType groundUnitCollectionType)
    {
        for (IGroundUnitCollection groundUnitCollection : linkedGroundUnits)
        {
            if (groundUnitCollection.getGroundUnitCollectionType() == groundUnitCollectionType)
            {
                return groundUnitCollection;
            }
        }
        return null;
    }
}
