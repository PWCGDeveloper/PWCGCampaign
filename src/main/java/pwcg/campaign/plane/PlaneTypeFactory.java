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
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

public class PlaneTypeFactory 
{
    private Map<String, PlaneType> planeTypes = new TreeMap<>();
    private Map<String, PlaneArchType> planeArchTypes = new TreeMap<>();

    public PlaneTypeFactory ()
    {
    }

    public void initialize()  throws PWCGException
    {
        planeTypes = AircraftIOJson.readJson();
        createPlaneArchTypes();
    }

    private void createPlaneArchTypes()
    {
        for (PlaneType planeType : planeTypes.values())
        {
            if (!planeArchTypes.containsKey(planeType.getArchType()))
            {
                PlaneArchType planeArchType = new PlaneArchType(planeType.getArchType());
                planeArchTypes.put(planeType.getArchType(), planeArchType);
            }
            
            PlaneArchType planeArchType = planeArchTypes.get(planeType.getArchType());
            planeArchType.addPlaneTypeToArchType(planeType);
        }
    }

    public void dump() 
    {
        for (PlaneType planeType : planeTypes.values())
        {
            Logger.log(LogLevel.DEBUG, "" + planeType.getType() + "    " +  planeType.getDisplayName());
        }
    }    
    
    public PlaneArchType getPlaneArchType(String planeArchTypeName)
    {
        return planeArchTypes.get(planeArchTypeName);
    }

    public List<PlaneType> getAllFightersForCampaign(Campaign campaign) throws PWCGException 
    {
        List<PlaneType> aircraftTypes = new ArrayList<PlaneType>();

        for (PlaneType planeType : planeTypes.values())
        {
            if (planeType != null)
            {
                if (planeType.isFlyable())
                {
                    if (planeType.isRole(Role.ROLE_FIGHTER))
                    {
                        if (planeType.isPlaneActive(campaign.getDate()))
                        {
                            aircraftTypes.add(planeType);
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

        for (PlaneType planeType : planeTypes.values())
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
        for (PlaneType plane : planeTypes.values())
        {
            allPlanesSet.put(plane.getType(), plane);
        }
        allPlanes.addAll(allPlanesSet.values());

        return allPlanes;
    }

    public List<PlaneType> getAxisPlanes() 
    {
        List<PlaneType>axisPlanes = new ArrayList<PlaneType>();

        for (PlaneType planeType : planeTypes.values())
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

    public PlaneType createPlaneTypeByType (String planteTypeName) throws PWCGException
    {
        PlaneType plane = null;

        for (PlaneType thisPlane : planeTypes.values())
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

    public List<PlaneType> createPlaneTypesForArchType(String planeArchType) throws PWCGException
    {
        List<PlaneType> planeTypesForArchType = new ArrayList<>();
        for (PlaneType thisPlane : planeTypes.values())
        {
            if (thisPlane.getArchType().equals(planeArchType))
            {
                planeTypesForArchType.add(thisPlane);
            }
        }
        
        if (planeTypesForArchType.isEmpty())
        {
            throw new PWCGException("No planes found for archtype " + planeArchType);
        }
        
        return planeTypesForArchType;
    }

    public List<PlaneType> createActivePlaneTypesForArchType(String planeArchType, Date date) throws PWCGException
    {
        List<PlaneType> planeTypesForArchType = new ArrayList<>();
        for (PlaneType thisPlane : planeTypes.values())
        {
            if (thisPlane.getArchType().equals(planeArchType))
            {
                if (DateUtils.isDateInRange(date, thisPlane.getIntroduction(), thisPlane.getWithdrawal()))
                {
                    planeTypesForArchType.add(thisPlane);
                }
            }
        }

        if (planeTypesForArchType.isEmpty())
        {
            planeTypesForArchType = createOlderPlaneTypesForArchType(planeArchType, date);
        }
        
        if (planeTypesForArchType.isEmpty())
        {
            throw new PWCGException("No planes found for in range archtype " + planeArchType);
        }
        
        return planeTypesForArchType;
    }

    private List<PlaneType> createOlderPlaneTypesForArchType(String planeArchType, Date date) throws PWCGException
    {
        List<PlaneType> planeTypesForArchType = new ArrayList<>();
        for (PlaneType thisPlane : planeTypes.values())
        {
            if (thisPlane.getArchType().equals(planeArchType))
            {
                if (thisPlane.getIntroduction().before(date))
                {
                    planeTypesForArchType.add(thisPlane);
                }
            }
        }
        
        if (planeTypesForArchType.isEmpty())
        {
            throw new PWCGException("No older planes found for archtype " + planeArchType);
        }
        
        return planeTypesForArchType;
    }

    public List<PlaneType> createActivePlaneTypesForDateAndSide(Side side, Date date) throws PWCGException
    {
        List<PlaneType> planeTypesForArchType = new ArrayList<>();
        for (PlaneType thisPlane : planeTypes.values())
        {
            if (DateUtils.isDateInRange(date, thisPlane.getIntroduction(), thisPlane.getWithdrawal()))
            {
                if (thisPlane.getSide() == side)
                {
                    planeTypesForArchType.add(thisPlane);
                }
            }
        }
        
        if (planeTypesForArchType.isEmpty())
        {
            throw new PWCGException("No planes found for date " + DateUtils.getDateStringDashDelimitedYYYYMMDD(date));
        }
        
        return planeTypesForArchType;
    }


    public PlaneType findActivePlaneTypeByCountryDateAndRole(ICountry country, Date date, Role role) throws PWCGException
    {
        List<PlaneType> possiblePlanes = new ArrayList<>();
        for (PlaneType planeType : planeTypes.values())
        {
            if (planeType.isUsedBy(country))
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

    public PlaneType findAnyPlaneTypeForCountryAndDate(ICountry country, Date date) throws PWCGException
    {
        List<PlaneType> possiblePlanes = new ArrayList<>();
        for (PlaneType planeType : planeTypes.values())
        {
            if (planeType.isUsedBy(country))
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
    
    private PlaneType getPlaneByDisplayName(String pwcgDesc) 
    {
        PlaneType plane = null;

        for (PlaneType thisPlane : planeTypes.values())
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

        for (PlaneType thisPlane : planeTypes.values())
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

    public Map<String, PlaneType> getPlaneTypes()
    {
        return planeTypes;
    }
}
