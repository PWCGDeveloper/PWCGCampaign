package pwcg.testutils;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetType;

public class GroundUnitTypeFinder
{
    static public boolean hasGroundUnitType (Mission mission, TargetType groundUnitType) throws PWCGException
    {
        for (GroundUnitCollection groundUnitCollection : mission.getGroundUnitBuilder().getAllMissionGroundUnits())
        {
            System.out.println("GroundUnitCollection " + groundUnitCollection.getName() + " flying misison type: " + groundUnitCollection.getTargetType());
            if (groundUnitCollection.getTargetType() == groundUnitType)
            {
                return true;
            }
        }
        
        return false;
    }
}
