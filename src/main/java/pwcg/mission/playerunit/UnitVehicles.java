package pwcg.mission.playerunit;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;

public class UnitVehicles
{
    private List<PlayerVehicleMcu> vehicles = new ArrayList<>();

    public UnitVehicles(PlayerUnit unit)
    {
        this.vehicles = unit.getVehicles();
    }

    public List<PlayerVehicleMcu> getAiVehicles() throws PWCGException 
    {
        List<PlayerVehicleMcu> aiVehicles = new ArrayList<>();
        for (PlayerVehicleMcu vehicle : vehicles)
        {
            if (!vehicle.getCrewMember().isPlayer())
            {
                aiVehicles.add(vehicle);
            }
        }

        return aiVehicles;
    }

    public List<PlayerVehicleMcu> getPlayerVehicles() throws PWCGException 
    {
        List<PlayerVehicleMcu> playerVehicles = new ArrayList<>();
        for (PlayerVehicleMcu vehicle : vehicles)
        {
            if (vehicle.getCrewMember().isPlayer())
            {
                playerVehicles.add(vehicle);
            }
        }

        return playerVehicles;
    }

    public PlayerVehicleMcu getVehicleForCrewMember(Integer crewMemberSerialNumber)
    {
        PlayerVehicleMcu crewMemberVehicle = null;
        for (PlayerVehicleMcu vehicle : vehicles)
        {
            if (vehicle.getCrewMember().getSerialNumber() == crewMemberSerialNumber)
            {
                crewMemberVehicle = vehicle;
                break;
            }
        }

        return crewMemberVehicle;
    }

    public PlayerVehicleMcu getVehicleByLinkTrId(Integer vehicleLinkTrId)
    {
        PlayerVehicleMcu crewMemberVehicle = null;
        for (PlayerVehicleMcu vehicle : vehicles)
        {
            if (vehicle.getLinkTrId() == vehicleLinkTrId)
            {
                crewMemberVehicle = vehicle;
                break;
            }
        }

        return crewMemberVehicle;
    }

    public PlayerVehicleMcu getUnitLeader()
    {
        return vehicles.get(0);
    }

    public List<PlayerVehicleMcu> getVehicles()
    {
        return vehicles;
    }

    public void setFuelForUnit(double myFuel) 
    {
        for (PlayerVehicleMcu vehicle : getVehicles())
        {
            vehicle.setFuel(myFuel);
        }
    }

    public int getUnitCruisingSpeed()
    {
        int cruisingSpeed = vehicles.get(0).getCruisingSpeed();
        for (PlayerVehicleMcu vehicle : vehicles)
        {
            if (vehicle.getCruisingSpeed() < cruisingSpeed)
            {
                cruisingSpeed = vehicle.getCruisingSpeed();
            }
        }
        
        return cruisingSpeed;
    }

    public void setVehicles(List<PlayerVehicleMcu> vehicles) throws PWCGException
    {
        this.vehicles = vehicles;        
    }
    
    public List<Integer> getVehicleLinkTrIds()
    {
        List<Integer> vehicleLinkIds = new ArrayList<>();
        for (PlayerVehicleMcu vehicle : vehicles)
        {
            vehicleLinkIds.add(vehicle.getLinkTrId());
        }
        return vehicleLinkIds;        
    }
    
    public void setVehiclePosition(Integer vehicleLinkTrId, Coordinate vehicleCoords, Orientation vehicleOrientation)
    {
        PlayerVehicleMcu vehicle = this.getVehicleByLinkTrId(vehicleLinkTrId);
        vehicle.setPosition(vehicleCoords);
        vehicle.setOrientation(vehicleOrientation);
        vehicle.populateEntity(getUnitLeader());
    }


    public void prepareVehicleForCoop() throws PWCGException
    {
        for (PlayerVehicleMcu vehicle : vehicles)
        {
            if (vehicle.isActivePlayerVehicle())
            {
                vehicle.setCoopStart(1);
                vehicle.setAiLevel(AiSkillLevel.ACE);
            }
            else
            {
                vehicle.setCoopStart(0);
            }
        }
    }
    

    public void write(BufferedWriter writer) throws PWCGException 
    {
        for (int i = 0; i < vehicles.size(); ++i)
        {
            PlayerVehicleMcu vehicle = vehicles.get(i);
            vehicle.write(writer);
        }
    }
    

    public int getUnitSize()
    {
        return vehicles.size();
    }
  
    public void finalize() throws PWCGException
    {
        
    }
}
