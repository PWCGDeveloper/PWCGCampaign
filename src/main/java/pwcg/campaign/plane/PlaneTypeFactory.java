package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.io.json.AircraftIOJson;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class PlaneTypeFactory 
{
    private Map<String, PlaneType> planeTypes = new TreeMap<>();

    public PlaneTypeFactory ()
    {
    }

    public void initialize()  throws PWCGException
    {
        planeTypes = AircraftIOJson.readJson();
    }

    public List<PlaneType> getAllPlanes()  throws PWCGException
    {
        List<PlaneType>allPlanes = new ArrayList<PlaneType>();
        Map<String, PlaneType>allPlanesSet = new HashMap<String, PlaneType>();
        for (PlaneType plane : planeTypes.values())
        {
            allPlanesSet.put(plane.getType(), plane);
        }
        allPlanes.addAll(allPlanesSet.values());

        return allPlanes;
    }

    public PlaneType getPlaneById(String planeTypeName) throws PWCGException
    {
        PlaneType plane = null;
        if (planeTypes.containsKey(planeTypeName))
        {
            plane = planeTypes.get(planeTypeName);
        }
        else
        {
            throw new PWCGException ("Invalid aircraft id: " + planeTypeName);
        }

        return plane;
    }

    public PlaneType createPlaneTypeByAnyName (String name)
    {
        PlaneType plane = getPlaneByPlaneType(name);
        if (plane != null)
        {
            return plane;
        }
        
        plane = getPlaneByDisplayName(name);
        if (plane != null)
        {
            return plane;
        }

        return null;
    }

    public PlaneType findActivePlaneTypeByCountryDateAndRole(ICountry country, Date date, PwcgRoleCategory roleCategory) throws PWCGException
    {
        List<PlaneType> possiblePlanes = new ArrayList<>();
        for (PlaneType planeType : planeTypes.values())
        {
            if (planeType.isUsedBy(country))
            {
                if (!(planeType.getIntroduction().after(date)))
                {
                    if (planeType.isRoleCategory(roleCategory))
                    {
                        possiblePlanes.add(planeType);
                    }
                }
            }
        }
        
        PlaneType selectedPlane = null;
        if (possiblePlanes.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(possiblePlanes.size());
            selectedPlane = possiblePlanes.get(index);
        }

        return selectedPlane;
    }
    
    public PlaneType getPlaneByDisplayName(String pwcgDesc) 
    {
        PlaneType plane = null;

        for (PlaneType thisPlane : planeTypes.values())
        {
            if (thisPlane.getDisplayName().equalsIgnoreCase(pwcgDesc))
            {
                plane = thisPlane;
                break;
            }
        }

        return plane;
    }

    private PlaneType getPlaneByPlaneType (String abrevName)
    {
        PlaneType plane = null;

        for (PlaneType thisPlane : planeTypes.values())
        {
            if (abrevName.equalsIgnoreCase(thisPlane.getType()))
            {
                plane = thisPlane;
                break;
            }
        }

        return plane;
    }
}
