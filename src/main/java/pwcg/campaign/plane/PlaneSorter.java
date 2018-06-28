package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;

public class PlaneSorter
{

    public List<PlaneType> sortPlanesByGoodness(List<PlaneType> planes) throws PWCGException
    {
        List<PlaneType> planesByGoodness = new ArrayList<PlaneType>();
        TreeMap<Integer, PlaneType> planesByGoodnessMap = new TreeMap<Integer, PlaneType>();
        
        for (PlaneType plane : planes)
        {
            if (!planesByGoodnessMap.containsKey(plane.getGoodness()))
            {
                planesByGoodnessMap.put(plane.getGoodness(), plane);
            }
            else
            {
                int planeGoodness = plane.getGoodness();
                while (!planesByGoodnessMap.containsKey(plane.getGoodness()))
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
