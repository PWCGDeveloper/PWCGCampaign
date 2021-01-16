package pwcg.product.fc.plane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.plane.IPlaneAttributeMapping;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class FCPlaneAttributeFactory
{
    public static FCPlaneAttributeMapping createPlaneAttributeMap(String planeTypeName) throws PWCGException
    {
 	   List<FCPlaneAttributeMapping> FCPlaneAttributeMappingsList = Arrays.asList(FCPlaneAttributeMapping.values());
 	   Map<String, FCPlaneAttributeMapping> FCPlaneAttributeMappings = new HashMap<>();;
 	   for (FCPlaneAttributeMapping FCPlaneAttributeMapping : FCPlaneAttributeMappingsList)
 	   {
 		  FCPlaneAttributeMappings.put(FCPlaneAttributeMapping.getPlaneType(), FCPlaneAttributeMapping);
 	   }
 	   
 	   return FCPlaneAttributeMappings.get(planeTypeName);
    }

	public static IStaticPlane getStaticPlane(String planeType) throws PWCGException 
	{
		IPlaneAttributeMapping FCPlaneAttributeMapping = createPlaneAttributeMap(planeType);
		String[] staticPlaneMatches = FCPlaneAttributeMapping.getStaticPlaneMatches();
		if (staticPlaneMatches.length > 0)
		{
    		int index = RandomNumberGenerator.getRandom(staticPlaneMatches.length);
    		return new FCStaticPlane(staticPlaneMatches[index]);
		}
		return null;
	}


    public static boolean isStaticPlane(String planeType) throws PWCGException 
    {
        List<FCPlaneAttributeMapping> planeAttributeMappingsList = Arrays.asList(FCPlaneAttributeMapping.values());
        for (FCPlaneAttributeMapping planeAttributeMappings : planeAttributeMappingsList)
        {
            for (String staticPlaneType : planeAttributeMappings.getStaticPlaneMatches())
            {
                if (planeType.contains(staticPlaneType)) 
                {
                    return true;
                }
                if (staticPlaneType.contains(planeType)) 
                {
                    return true;
                }
            }
        }
        return false;
    }
}
