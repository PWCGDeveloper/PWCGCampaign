package pwcg.campaign.plane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IStaticPlane;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class PlaneAttributeFactory
{
    public static PlaneAttributeMapping createPlaneAttributeMap(String planeTypeName) throws PWCGException
    {
 	   List<PlaneAttributeMapping> planeAttributeMappingsList = Arrays.asList(PlaneAttributeMapping.values());
 	   Map<String, PlaneAttributeMapping> planeAttributeMappings = new HashMap<>();;
 	   for (PlaneAttributeMapping BosPlaneAttributeMapping : planeAttributeMappingsList)
 	   {
 		  planeAttributeMappings.put(BosPlaneAttributeMapping.getPlaneType(), BosPlaneAttributeMapping);
 	   }
 	   
 	   return planeAttributeMappings.get(planeTypeName);
    }

    public static IStaticPlane getStaticPlane(String planeType) throws PWCGException 
    {
        PlaneAttributeMapping bosPlaneAttributeMapping = createPlaneAttributeMap(planeType);
        String[] staticPlaneMatches = bosPlaneAttributeMapping.getStaticPlaneMatches();
        int index = RandomNumberGenerator.getRandom(staticPlaneMatches.length);
        return new StaticPlane(staticPlaneMatches[index]);
    }

    public static boolean isStaticPlane(String planeType) throws PWCGException 
    {
        List<PlaneAttributeMapping> planeAttributeMappingsList = Arrays.asList(PlaneAttributeMapping.values());
        for (PlaneAttributeMapping planeAttributeMappings : planeAttributeMappingsList)
        {
            for (String staticTankType : planeAttributeMappings.getStaticPlaneMatches())
            {
                if (planeType.contains(staticTankType)) 
                {
                    return true;
                }
                if (staticTankType.contains(planeType)) 
                {
                    return true;
                }
            }
        }
        return false;
    }
}
