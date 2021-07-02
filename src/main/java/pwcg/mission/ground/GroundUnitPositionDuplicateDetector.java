package pwcg.mission.ground;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.mcu.McuSpawn;

public class GroundUnitPositionDuplicateDetector
{
    Map <Integer, Integer> duplicateGroundUnitCollections = new HashMap<>();
    
    public boolean verifyMissionGroundUnitPositionsNotDuplicated (List<GroundUnitCollection> testGroundUnitCollections, List<GroundUnitCollection> groundUnitCollections) throws PWCGException
    {
        boolean noDuplicates = true;
        for (GroundUnitCollection groundUnitCollection : groundUnitCollections)
        {
            if (!verifyGroundCollectionUnitPositionsNotDuplicated(testGroundUnitCollections, groundUnitCollection))
            {
                noDuplicates = false;
            }
        }
        return noDuplicates;
    }
    
    public boolean verifyGroundCollectionUnitPositionsNotDuplicated (List<GroundUnitCollection> testGroundUnitCollections, GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        boolean noDuplicates = true;
        for (GroundUnitCollection testGroundUnitCollection : testGroundUnitCollections)
        {
            if (!verifyGroundUnitCollectionPositionsNotDuplicated(groundUnitCollection, testGroundUnitCollection))
            {
                noDuplicates = false;
            }
        }
        
        return noDuplicates;
    }
    
    public boolean verifyGroundUnitCollectionPositionsNotDuplicated (GroundUnitCollection groundUnitCollection, GroundUnitCollection testGroundUnitCollection) throws PWCGException
    {
        boolean noDuplicates = true;
        for (McuSpawn groundUnitSpawns : groundUnitCollection.getSpawns())
        {
            for (McuSpawn testGroundUnitSpawns : testGroundUnitCollection.getSpawns())
            {
                if (groundUnitSpawns.getIndex() != testGroundUnitSpawns.getIndex())
                {
                    double distance = MathUtils.calcDist(groundUnitSpawns.getPosition(), testGroundUnitSpawns.getPosition());
                    if (distance < 9.0)
                    {
                        noDuplicates = false;                        
                        duplicateGroundUnitCollections.put(testGroundUnitCollection.getIndex(), groundUnitCollection.getIndex());
                    }
                }
            }
        }
        
        return noDuplicates;
    }


    public Map<Integer, Integer> getDuplicateGroundUnitCollections()
    {
        return duplicateGroundUnitCollections;
    }


    public void setDuplicateGroundUnitCollections(Map<Integer, Integer> duplicateGroundUnitCollections)
    {
        this.duplicateGroundUnitCollections = duplicateGroundUnitCollections;
    }
}
