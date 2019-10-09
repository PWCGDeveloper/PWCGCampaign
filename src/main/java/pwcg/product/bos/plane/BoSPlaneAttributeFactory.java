package pwcg.product.bos.plane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.plane.IPlaneAttributeMapping;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class BoSPlaneAttributeFactory
{
    public static BosPlaneAttributeMapping createPlaneAttributeMap(String planeTypeName) throws PWCGException
    {
 	   List<BosPlaneAttributeMapping> BosPlaneAttributeMappingsList = Arrays.asList(BosPlaneAttributeMapping.values());
 	   Map<String, BosPlaneAttributeMapping> BosPlaneAttributeMappings = new HashMap<>();;
 	   for (BosPlaneAttributeMapping BosPlaneAttributeMapping : BosPlaneAttributeMappingsList)
 	   {
 		  BosPlaneAttributeMappings.put(BosPlaneAttributeMapping.getPlaneType(), BosPlaneAttributeMapping);
 	   }
 	   
 	   return BosPlaneAttributeMappings.get(planeTypeName);
    }

	public static IStaticPlane getStaticPlane(String planeType) throws PWCGException 
	{
		IPlaneAttributeMapping BosPlaneAttributeMapping = createPlaneAttributeMap(planeType);
		String[] staticPlaneMatches = BosPlaneAttributeMapping.getStaticPlaneMatches();
		int index = RandomNumberGenerator.getRandom(staticPlaneMatches.length);
		return new BoSStaticPlane(staticPlaneMatches[index]);
	}
}
