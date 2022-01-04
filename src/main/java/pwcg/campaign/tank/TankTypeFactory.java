package pwcg.campaign.tank;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.TankIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

public class TankTypeFactory 
{
    private Map<String, TankType> tankTypes = new TreeMap<>();
    private Map<String, TankArchType> tankArchTypes = new TreeMap<>();

    public TankTypeFactory ()
    {
    }

    public void initialize()  throws PWCGException
    {
        tankTypes = TankIOJson.readJson();
        createTankArchTypes();
    }

    private void createTankArchTypes()
    {
        for (TankType tankType : tankTypes.values())
        {
            if (!tankArchTypes.containsKey(tankType.getArchType()))
            {
                TankArchType tankArchType = new TankArchType(tankType.getArchType());
                tankArchTypes.put(tankType.getArchType(), tankArchType);
            }
            
            TankArchType tankArchType = tankArchTypes.get(tankType.getArchType());
            tankArchType.addTankTypeToArchType(tankType);
        }
    }

    public void dump() 
    {
        for (TankType tankType : tankTypes.values())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + tankType.getType() + "    " +  tankType.getDisplayName());
        }
    }    
    
    public TankArchType getTankArchType(String tankArchTypeName)
    {
        return tankArchTypes.get(tankArchTypeName);
    }

    public List<TankType> getAllFightersForCampaign(Campaign campaign) throws PWCGException 
    {
        List<TankType> aircraftTypes = new ArrayList<TankType>();

        for (TankType tankType : tankTypes.values())
        {
            if (tankType != null)
            {
                if (tankType.isRoleCategory(PwcgRoleCategory.FIGHTER))
                {
                    if (tankType.isPlaneActive(campaign.getDate()))
                    {
                        aircraftTypes.add(tankType);
                    }
                }
            }
        }

        return aircraftTypes;
    }

    public List<TankType> getAlliedPlanes() 
    {
        List<TankType>alliedPlanes = new ArrayList<TankType>();

        for (TankType tankType : tankTypes.values())
        {
            if (tankType.getSide() == Side.ALLIED)
            {
                alliedPlanes.add(tankType);
            }
        }

        return alliedPlanes;
    }

    public List<TankType> getAllPlanes()  throws PWCGException
    {
        List<TankType>allPlanes = new ArrayList<TankType>();
        Map<String, TankType>allPlanesSet = new HashMap<String, TankType>();
        for (TankType tank : tankTypes.values())
        {
            allPlanesSet.put(tank.getType(), tank);
        }
        allPlanes.addAll(allPlanesSet.values());

        return allPlanes;
    }

    public List<TankType> getAxisPlanes() 
    {
        List<TankType>axisPlanes = new ArrayList<TankType>();

        for (TankType tankType : tankTypes.values())
        {
            if (tankType.getSide() == Side.AXIS)
            {
                axisPlanes.add(tankType);
            }
        }

        return axisPlanes;
    }

    public TankType getPlaneById(String tankTypeName) throws PWCGException
    {
        TankType tank = null;
        if (tankTypes.containsKey(tankTypeName))
        {
            tank = tankTypes.get(tankTypeName);
        }
        else
        {
            throw new PWCGException ("Invalid aircraft id: " + tankTypeName);
        }

        return tank;
    }

    public TankType createTankTypeByType (String tankTypeName) throws PWCGException
    {
        TankType tank = null;

        for (TankType thisPlane : tankTypes.values())
        {
            if (thisPlane.getType().equalsIgnoreCase(tankTypeName))
            {
                tank = thisPlane;
                break;
            }
        }

        if (tank == null)
        {
            throw new PWCGException ("Invalid aircraft name: " + tankTypeName);
        }

        return tank;
    }

    public TankType createTankTypeByAnyName (String name)
    {
        TankType tank = getPlaneByTankType(name);
        if (tank != null)
        {
            return tank;
        }
        
        tank = getPlaneByDisplayName(name);
        if (tank != null)
        {
            return tank;
        }

        return null;
    }

    public List<TankType> getAvailableTankTypes(ICountry country, PwcgRoleCategory roleCategory, Date date) throws PWCGException
    {
        Map<Integer, TankType> availableTankTypes = new TreeMap<>();
        for (TankType thisPlane : tankTypes.values())
        {
            if (thisPlane.isUsedBy(country))
            {
                if (thisPlane.isRoleCategory(roleCategory))
                {
                    if (DateUtils.isDateInRange(date, thisPlane.getIntroduction(), thisPlane.getWithdrawal()))
                    {
                        availableTankTypes.put(thisPlane.getGoodness(), thisPlane);
                    }
                }
            }
        }
        
        return new ArrayList<>(availableTankTypes.values());
    }

    public List<TankType> createTankTypesForArchType(String tankArchType) throws PWCGException
    {
        List<TankType> tankTypesForArchType = new ArrayList<>();
        for (TankType thisPlane : tankTypes.values())
        {
            if (thisPlane.getArchType().equals(tankArchType))
            {
                tankTypesForArchType.add(thisPlane);
            }
        }
        
        if (tankTypesForArchType.isEmpty())
        {
            throw new PWCGException("No tanks found for archtype " + tankArchType);
        }
        
        return tankTypesForArchType;
    }

    public List<TankType> createActiveTankTypesForArchType(String tankArchType, Date date) throws PWCGException
    {
        List<TankType> tankTypesForArchType = new ArrayList<>();
        for (TankType thisPlane : tankTypes.values())
        {
            if (thisPlane.getArchType().equals(tankArchType))
            {
                if (DateUtils.isDateInRange(date, thisPlane.getIntroduction(), thisPlane.getWithdrawal()))
                {
                    tankTypesForArchType.add(thisPlane);
                }
            }
        }

        if (tankTypesForArchType.isEmpty())
        {
            tankTypesForArchType = createOlderTankTypesForArchType(tankArchType, date);
        }
        
        if (tankTypesForArchType.isEmpty())
        {
            throw new PWCGException("No tanks found for in range archtype " + tankArchType);
        }
        
        return tankTypesForArchType;
    }

    public List<TankType> createOlderTankTypesForArchType(String tankArchType, Date date) throws PWCGException
    {
        List<TankType> tankTypesForArchType = new ArrayList<>();
        for (TankType thisPlane : tankTypes.values())
        {
            if (thisPlane.getArchType().equals(tankArchType))
            {
                if (thisPlane.getIntroduction().before(date))
                {
                    tankTypesForArchType.add(thisPlane);
                }
            }
        }
        
        if (tankTypesForArchType.isEmpty())
        {
            throw new PWCGException("No older tanks found for archtype " + tankArchType);
        }
        
        return tankTypesForArchType;
    }
    

    public List<TankType> createPlanesByIntroduction(String tankArchType) throws PWCGException
    {
        TreeMap<Date, TankType> tankTypesTypeByIntroduction = new TreeMap<>();
        for (TankType thisPlane : tankTypes.values())
        {
            if (thisPlane.getArchType().equals(tankArchType))
            {
                tankTypesTypeByIntroduction.put(thisPlane.getIntroduction(), thisPlane);
            }
        }

        List<TankType> tankTypesForArchType = new ArrayList<>(tankTypesTypeByIntroduction.values());
        return tankTypesForArchType;
    }


    public List<TankType> createActiveTankTypesForDateAndSide(Side side, Date date) throws PWCGException
    {
        List<TankType> tankTypesForArchType = new ArrayList<>();
        for (TankType thisPlane : tankTypes.values())
        {
            if (DateUtils.isDateInRange(date, thisPlane.getIntroduction(), thisPlane.getWithdrawal()))
            {
                if (thisPlane.getSide() == side)
                {
                    tankTypesForArchType.add(thisPlane);
                }
            }
        }
        
        if (tankTypesForArchType.isEmpty())
        {
            throw new PWCGException("No tanks found for date " + DateUtils.getDateStringDashDelimitedYYYYMMDD(date));
        }
        
        return tankTypesForArchType;
    }


    public TankType findActiveTankTypeByCountryDateAndRole(ICountry country, Date date, PwcgRoleCategory roleCategory) throws PWCGException
    {
        List<TankType> possiblePlanes = new ArrayList<>();
        for (TankType tankType : tankTypes.values())
        {
            if (tankType.isUsedBy(country))
            {
                if (!(tankType.getIntroduction().after(date)))
                {
                    if (tankType.isRoleCategory(roleCategory))
                    {
                        possiblePlanes.add(tankType);
                    }
                }
            }
        }
        
        TankType selectedPlane = null;
        if (possiblePlanes.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(possiblePlanes.size());
            selectedPlane = possiblePlanes.get(index);
        }

        return selectedPlane;
    }

    public TankType findAnyTankTypeForCountryAndDate(ICountry country, Date date) throws PWCGException
    {
        List<TankType> possiblePlanes = new ArrayList<>();
        for (TankType tankType : tankTypes.values())
        {
            if (tankType.isUsedBy(country))
            {
                if (!(tankType.getIntroduction().after(date)))
                {
                    possiblePlanes.add(tankType);
                }
            }
        }
        
        TankType selectedPlane = null;
        if (possiblePlanes.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(possiblePlanes.size());
            selectedPlane = possiblePlanes.get(index);
        }

        return selectedPlane;
    }
    
    public TankType getPlaneByDisplayName(String pwcgDesc) 
    {
        TankType tank = null;

        for (TankType thisPlane : tankTypes.values())
        {
            if (thisPlane.getDisplayName().equalsIgnoreCase(pwcgDesc))
            {
                tank = thisPlane;
                break;
            }
        }

        return tank;
    }

    private TankType getPlaneByTankType (String abrevName)
    {
        TankType tank = null;

        for (TankType thisPlane : tankTypes.values())
        {
            if (abrevName.equalsIgnoreCase(thisPlane.getType()))
            {
                tank = thisPlane;
                break;
            }
        }

        return tank;
    }

    public Map<String, TankType> getTankTypes()
    {
        return tankTypes;
    }
}
