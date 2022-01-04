package pwcg.campaign.crewmember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class CrewMemberVictories
{
    private List<Victory>  airToAirVictories = new ArrayList<>();
    private List<Victory>  tankVictories = new ArrayList<>();
    private List<Victory>  trainVictories = new ArrayList<>();
    private List<Victory>  groundVictories = new ArrayList<>();
    
    private Map<String, List<Victory>> airVictoriesdInType = new HashMap<>();
    private Map<String, List<Victory>> groundVictoriesdInType = new HashMap<>();
    private Map<String, List<Victory>> airVictoriesType = new HashMap<>();
    private Map<String, List<Victory>> tankVictoriesType = new HashMap<>();
    
    public CrewMemberVictories (List<Victory> victories) throws PWCGException
    {
        for (Victory victory : victories)
        {
            if (victory.getVictim().getAirOrGround() == Victory.AIRCRAFT || victory.getVictim().getAirOrGround() == Victory.UNSPECIFIED_VICTORY)
            {
                airToAirVictories.add(victory);
                addAirVictoryInType(victory);
                addAirTypeVictory(victory);
            }
            
            if (victory.getVictim().getAirOrGround() == Victory.VEHICLE)
            {
                VehicleDefinition vehicleDefinitionByName = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionByVehicleName(victory.getVictim().getName());
                if (vehicleDefinitionByName != null && vehicleDefinitionByName.getVehicleClass() == VehicleClass.Tank)
                {
                    tankVictories.add(victory);
                    addTankTypeVictory(victory);
                }
                else if (vehicleDefinitionByName != null && vehicleDefinitionByName.getVehicleClass() == VehicleClass.TrainLocomotive)
                {
                    trainVictories.add(victory);
                }
                else
                {
                    groundVictories.add(victory);
                }
                
                addGroundVictoryInType(victory);
            }
        }
    }

    private void addAirVictoryInType(Victory victory)
    {
        String key = getTankArchTypeForPlaneName(victory.getVictor().getType());
        if (!airVictoriesdInType.containsKey(key))
        {
            List<Victory> playerAircraftTypeTypeList = new ArrayList<>();
            airVictoriesdInType.put(key, playerAircraftTypeTypeList);
        }
        List<Victory> playerAircraftTypeTypeList = airVictoriesdInType.get(key);
        playerAircraftTypeTypeList.add(victory);
    }

    private void addGroundVictoryInType(Victory victory)
    {
        String key = getTankArchTypeForPlaneName(victory.getVictor().getType());
        if (!groundVictoriesdInType.containsKey(key))
        {
            List<Victory> playerAircraftTypeTypeList = new ArrayList<>();
            groundVictoriesdInType.put(key, playerAircraftTypeTypeList);
        }
        List<Victory> playerAircraftTypeTypeList = groundVictoriesdInType.get(key);
        playerAircraftTypeTypeList.add(victory);
    }

    private void addAirTypeVictory(Victory victory)
    {
        String key = getTankArchTypeForPlaneName(victory.getVictim().getType());
        if (!airVictoriesType.containsKey(key))
        {
            List<Victory> planeTypeList = new ArrayList<>();
            airVictoriesType.put(key, planeTypeList);
        }
        List<Victory> planeTypeList = airVictoriesType.get(key);
        planeTypeList.add(victory);
    }

    private void addTankTypeVictory(Victory victory)
    {
        String key = victory.getVictim().getType();
        if (!tankVictoriesType.containsKey(key))
        {
            List<Victory> tankTypeList = new ArrayList<>();
            tankVictoriesType.put(key, tankTypeList);
        }
        List<Victory> tankTypeList = tankVictoriesType.get(key);
        tankTypeList.add(victory);
    }
    
    private String getTankArchTypeForPlaneName(String planeTypeName)
    {
        TankType planeType = PWCGContext.getInstance().getTankTypeFactory().getPlaneByDisplayName(planeTypeName);
        String archTypeName = "Unknown";
        if (planeType != null)
        {
            TankArchType planeArchType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(planeType.getArchType());
            archTypeName = TankArchType.getArchTypeDescription(planeArchType.getTankArchTypeName());
        }
        return archTypeName;
    }

    public List<Victory> getAirToAirVictories()
    {
        return new ArrayList<>(airToAirVictories);
    }

    public List<Victory> getTankVictories()
    {
        return new ArrayList<>(tankVictories);
    }

    public List<Victory> getTrainVictories()
    {
        return new ArrayList<>(trainVictories);
    }

    public List<Victory> getGroundVictories()
    {
        return new ArrayList<>(groundVictories);
    }

    public int getAirToAirVictoryCount()
    {
        return airToAirVictories.size();
    }

    public int getTankVictoryCount()
    {
        return tankVictories.size();
    }

    public int getTrainVictoryCount()
    {
        return trainVictories.size();
    }

    public int getGroundVictoryCount()
    {
        return groundVictories.size();
    }

    public Map<String, List<Victory>> getAirVictoriesdInType()
    {
        return airVictoriesdInType;
    }

    public Map<String, List<Victory>> getGroundVictoriesdInType()
    {
        return groundVictoriesdInType;
    }

    public Map<String, List<Victory>> getAirVictoriesType()
    {
        return airVictoriesType;
    }

    public Map<String, List<Victory>> getTankVictoriesType()
    {
        return tankVictoriesType;
    }

    public int getGroundVictoryPointTotal()
    {
        int numCrewMemberGroundVictoryPoints = getGroundVictoryCount() + (getTankVictoryCount() * 3) + (getTrainVictoryCount() * 3);
        return numCrewMemberGroundVictoryPoints;
    }


}
