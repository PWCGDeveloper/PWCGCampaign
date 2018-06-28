package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.AircraftIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.mission.flight.plane.Plane;
import pwcg.core.utils.RandomNumberGenerator;

public class PlaneTypeFactory 
{
    private Map<String, PlaneType> planeMap = new TreeMap<String, PlaneType>();

    public PlaneTypeFactory ()
    {
    }

    public void initialize()  throws PWCGException
    {
        planeMap = AircraftIOJson.readJson();
    }

    public void dump() 
    {
        for (PlaneType plane : planeMap.values())
        {
            Logger.log(LogLevel.DEBUG, "" + plane.getType() + "    " +  plane.getDisplayName());
        }
    }        

    public List<PlaneType> getAllFightersForCampaign(Campaign campaign) throws PWCGException 
    {
        List<PlaneType> aircraftTypes = new ArrayList<PlaneType>();

        for (PlaneType plane : planeMap.values())
        {
            if (plane != null)
            {
                if (plane.isFlyable())
                {
                    if (plane.isRole(Role.ROLE_FIGHTER))
                    {
                        if (plane.getSide() == campaign.determineCountry().getSide())
                        {
                            if (plane.isPlaneActive(campaign.getDate()))
                            {
                                aircraftTypes.add(plane);
                            }
                        }
                    }
                }
            }
        }

        return aircraftTypes;
    }

    public List<PlaneType> getAlliedPlanes() 
    {
        List<PlaneType>alliedPlanes = new ArrayList<PlaneType>();

        for (PlaneType planeType : planeMap.values())
        {
            if (planeType.getSide() == Side.ALLIED)
            {
                alliedPlanes.add(planeType);
            }
        }

        return alliedPlanes;
    }

    public List<PlaneType> getAllPlanes()  throws PWCGException
    {
        List<PlaneType>allPlanes = new ArrayList<PlaneType>();
        Map<String, PlaneType>allPlanesSet = new HashMap<String, PlaneType>();
        for (PlaneType plane : planeMap.values())
        {
            allPlanesSet.put(plane.getType(), plane);
        }
        allPlanes.addAll(allPlanesSet.values());

        return allPlanes;
    }

    public List<PlaneType> getAxisPlanes() 
    {
        List<PlaneType>axisPlanes = new ArrayList<PlaneType>();

        for (PlaneType planeType : planeMap.values())
        {
            if (planeType.getSide() == Side.AXIS)
            {
                axisPlanes.add(planeType);
            }
        }

        return axisPlanes;
    }

    public PlaneType getPlaneById(String planeTypeName) throws PWCGException
    {
        PlaneType plane = null;
        if (planeMap.containsKey(planeTypeName))
        {
            plane = planeMap.get(planeTypeName);
        }
        else
        {
            throw new PWCGException ("Invalid aircraft id: " + planeTypeName);
        }

        return plane;
    }

    public PlaneType getActivePlaneBySideDateAndRole(Side side, Date date, Role role) throws PWCGException
    {
        List<PlaneType> possiblePlanes = new ArrayList<>();
        for (PlaneType planeType : planeMap.values())
        {
            if (planeType.getSide() == side)
            {
                if (!(planeType.getIntroduction().after(date)))
                {
                    if (planeType.isRole(role))
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

    public PlaneType getActivePlaneBySideAndDate(Side side, Date date) throws PWCGException
    {
        List<PlaneType> possiblePlanes = new ArrayList<>();
        for (PlaneType planeType : planeMap.values())
        {
            if (planeType.getSide() == side)
            {
                if (!(planeType.getIntroduction().after(date)))
                {
                    possiblePlanes.add(planeType);
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

    public PlaneType getPlaneTypeByType (String planteTypeName) throws PWCGException
    {
        PlaneType plane = null;

        for (PlaneType thisPlane : planeMap.values())
        {
            if (thisPlane.getType().equalsIgnoreCase(planteTypeName))
            {
                if (thisPlane.isFlyable())
                {
                    plane = thisPlane;
                    break;
                }
            }
        }

        if (plane == null)
        {
            throw new PWCGException ("Invalid aircraft name: " + planteTypeName);
        }

        return plane;
    }

    public PlaneType getPlaneTypeByAnyName (String name)
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

    public static Plane createPlaneByPlaneType (PlaneType planeType, ICountry country, SquadronMember pilot)
    {
        Plane plane = new Plane(planeType, country, pilot);
        return plane;
    }

    private PlaneType getPlaneByDisplayName(String pwcgDesc) 
    {
        PlaneType plane = null;

        for (PlaneType thisPlane : planeMap.values())
        {
            if (thisPlane.getDisplayName().equalsIgnoreCase(pwcgDesc))
            {
                if (thisPlane.isFlyable())
                {
                    plane = thisPlane;
                    break;
                }
            }
        }

        return plane;
    }

    private PlaneType getPlaneByPlaneType (String abrevName)
    {
        PlaneType plane = null;

        for (PlaneType thisPlane : planeMap.values())
        {
            if (abrevName.equalsIgnoreCase(thisPlane.getType()))
            {
                if (thisPlane.isFlyable())
                {
                    plane = thisPlane;
                    break;
                }
            }
        }

        return plane;
    }

}
