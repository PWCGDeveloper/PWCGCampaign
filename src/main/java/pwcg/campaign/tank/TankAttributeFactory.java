package pwcg.campaign.tank;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;

public class TankAttributeFactory
{
    public static TankAttributeMapping createTankAttributeMap(String tankTypeName) throws PWCGException
    {
 	   List<TankAttributeMapping> tankAttributeMappingsList = Arrays.asList(TankAttributeMapping.values());
 	   Map<String, TankAttributeMapping> tankAttributeMappings = new HashMap<>();;
 	   for (TankAttributeMapping BosTankAttributeMapping : tankAttributeMappingsList)
 	   {
 		  tankAttributeMappings.put(BosTankAttributeMapping.getTankType(), BosTankAttributeMapping);
 	   }
 	   
 	   return tankAttributeMappings.get(tankTypeName);
    }
}
