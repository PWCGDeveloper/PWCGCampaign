package pwcg.product.bos.plane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IStaticPlane;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class BoSPlaneAttributeFactory
{
    public static BosPlaneAttributeMapping createPlaneAttributeMap(String planeTypeName) throws PWCGException
    {
 	   List<BosPlaneAttributeMapping> planeAttributeMappingsList = Arrays.asList(BosPlaneAttributeMapping.values());
 	   Map<String, BosPlaneAttributeMapping> planeAttributeMappings = new HashMap<>();;
 	   for (BosPlaneAttributeMapping BosPlaneAttributeMapping : planeAttributeMappingsList)
 	   {
 		  planeAttributeMappings.put(BosPlaneAttributeMapping.getPlaneType(), BosPlaneAttributeMapping);
 	   }
 	   
 	   return planeAttributeMappings.get(planeTypeName);
    }

    public static IStaticPlane getStaticPlane(String planeType) throws PWCGException 
    {
        BosPlaneAttributeMapping bosPlaneAttributeMapping = createPlaneAttributeMap(planeType);
        String[] staticPlaneMatches = bosPlaneAttributeMapping.getStaticPlaneMatches();
        int index = RandomNumberGenerator.getRandom(staticPlaneMatches.length);
        return new BoSStaticPlane(staticPlaneMatches[index]);
    }

    public static boolean isStaticPlane(String planeType) throws PWCGException 
    {
        List<BosPlaneAttributeMapping> planeAttributeMappingsList = Arrays.asList(BosPlaneAttributeMapping.values());
        for (BosPlaneAttributeMapping planeAttributeMappings : planeAttributeMappingsList)
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
