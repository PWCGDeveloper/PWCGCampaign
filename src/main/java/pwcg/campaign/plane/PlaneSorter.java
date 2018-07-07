package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;

public class PlaneSorter
{

    public static List<PlaneType> sortPlanesByGoodness(List<PlaneType> planes) throws PWCGException
    {
        List<PlaneType> planesByGoodness = new ArrayList<>();
        TreeMap<Integer, PlaneType> planesByGoodnessMap = new TreeMap<>();
        
        for (PlaneType plane : planes)
        {
            int planeGoodnessKey = plane.getGoodness() * 1000;
            while (planesByGoodnessMap.containsKey(planeGoodnessKey))
            {
                ++planeGoodnessKey;
            }
            planesByGoodnessMap.put(planeGoodnessKey, plane);
        }
        
        planesByGoodness.addAll(planesByGoodnessMap.values());
        Collections.reverse(planesByGoodness);

        return planesByGoodness;
    }

    public static List<EquippedPlane> sortEquippedPlanesByGoodness(List<EquippedPlane> planes) throws PWCGException
    {
        List<EquippedPlane> planesByGoodness = new ArrayList<>();
        TreeMap<String, EquippedPlane> planesByGoodnessMap = new TreeMap<>();
        
        for (EquippedPlane plane : planes)
        {
            planesByGoodnessMap.put(formKey(plane), plane);
        }
        
        planesByGoodness.addAll(planesByGoodnessMap.values());
        
        return planesByGoodness;
    }
    
    private static String formKey(EquippedPlane plane)
    {
        String key = "" + (10000 - plane.getGoodness()) + plane.getType() + plane.getSerialNumber();
        return key;
    }

}
