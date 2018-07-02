package pwcg.campaign.plane;

import java.util.ArrayList;
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
            if (!planesByGoodnessMap.containsKey(plane.getGoodness()))
            {
                planesByGoodnessMap.put(plane.getGoodness(), plane);
            }
            else
            {
                int planeGoodness = plane.getGoodness();
                while (planesByGoodnessMap.containsKey(planeGoodness))
                {
                    ++planeGoodness;
                }

                planesByGoodnessMap.put(planeGoodness, plane);
            }
        }
        
        planesByGoodness.addAll(planesByGoodnessMap.values());
        
        return planesByGoodness;
    }

    public static List<EquippedPlane> sortEquippedPlanesByGoodness(List<EquippedPlane> planes) throws PWCGException
    {
        List<EquippedPlane> planesByGoodness = new ArrayList<>();
        TreeMap<Integer, EquippedPlane> planesByGoodnessMap = new TreeMap<>();
        
        for (EquippedPlane plane : planes)
        {
            if (!planesByGoodnessMap.containsKey(plane.getGoodness()))
            {
                planesByGoodnessMap.put(plane.getGoodness(), plane);
            }
            else
            {
                int planeGoodness = plane.getGoodness();
                while (planesByGoodnessMap.containsKey(planeGoodness))
                {
                    ++planeGoodness;
                }

                planesByGoodnessMap.put(planeGoodness, plane);
            }
        }
        
        planesByGoodness.addAll(planesByGoodnessMap.values());
        
        return planesByGoodness;
    }

}
