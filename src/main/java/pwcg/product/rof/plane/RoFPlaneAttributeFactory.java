package pwcg.product.rof.plane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.plane.IPlaneAttributeMapping;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class RoFPlaneAttributeFactory
{
    public static RoFPlaneAttributeMapping createPlaneAttributeMap(String planeTypeName) throws PWCGException
    {
 	   List<RoFPlaneAttributeMapping> RoFPlaneAttributeMappingsList = Arrays.asList(RoFPlaneAttributeMapping.values());
 	   Map<String, RoFPlaneAttributeMapping> RoFPlaneAttributeMappings = new HashMap<>();;
 	   for (RoFPlaneAttributeMapping RoFPlaneAttributeMapping : RoFPlaneAttributeMappingsList)
 	   {
 		  RoFPlaneAttributeMappings.put(RoFPlaneAttributeMapping.getPlaneType(), RoFPlaneAttributeMapping);
 	   }
 	   
 	   return RoFPlaneAttributeMappings.get(planeTypeName);
    }

	public static IStaticPlane getStaticPlane(String planeType) throws PWCGException 
	{
		IPlaneAttributeMapping RoFPlaneAttributeMapping = createPlaneAttributeMap(planeType);
		String[] staticPlaneMatches = RoFPlaneAttributeMapping.getStaticPlaneMatches();
		int index = RandomNumberGenerator.getRandom(staticPlaneMatches.length);
		return new RoFStaticPlane(staticPlaneMatches[index]);
	}
}
