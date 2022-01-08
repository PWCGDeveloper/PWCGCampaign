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
    private List<TankMcu> tanks = new ArrayList<>();

    public UnitVehicles(PlayerUnit unit)
    {
        this.tanks = unit.getTanks();
    }

    public List<TankMcu> getAiTanks() throws PWCGException 
    {
        List<TankMcu> aiVehicles = new ArrayList<>();
        for (TankMcu tank : tanks)
        {
            if (!tank.getCrewMember().isPlayer())
            {
                aiVehicles.add(tank);
            }
        }

        return aiVehicles;
    }

    public List<TankMcu> getPlayerTanks() throws PWCGException 
    {
        List<TankMcu> playerVehicles = new ArrayList<>();
        for (TankMcu tank : tanks)
        {
            if (tank.getCrewMember().isPlayer())
            {
                playerVehicles.add(tank);
            }
        }

        return playerVehicles;
    }

    public TankMcu getTankForCrewMember(Integer crewMemberSerialNumber)
    {
        TankMcu crewMemberVehicle = null;
        for (TankMcu tank : tanks)
        {
            if (tank.getCrewMember().getSerialNumber() == crewMemberSerialNumber)
            {
                crewMemberVehicle = tank;
                break;
            }
        }

        return crewMemberVehicle;
    }

    public TankMcu getTankByLinkTrId(Integer tankLinkTrId)
    {
        TankMcu crewMemberVehicle = null;
        for (TankMcu tank : tanks)
        {
            if (tank.getLinkTrId() == tankLinkTrId)
            {
                crewMemberVehicle = tank;
                break;
            }
        }

        return crewMemberVehicle;
    }

    public TankMcu getUnitLeader()
    {
        return tanks.get(0);
    }

    public List<TankMcu> getTanks()
    {
        return tanks;
    }

    public void setFuelForUnit(double myFuel) 
    {
        for (TankMcu tank : getTanks())
        {
            tank.setFuel(myFuel);
        }
    }

    public int getUnitCruisingSpeed()
    {
        int cruisingSpeed = tanks.get(0).getCruisingSpeed();
        for (TankMcu tank : tanks)
        {
            if (tank.getCruisingSpeed() < cruisingSpeed)
            {
                cruisingSpeed = tank.getCruisingSpeed();
            }
        }
        
        return cruisingSpeed;
    }

    public void setTanks(List<TankMcu> tanks) throws PWCGException
    {
        this.tanks = tanks;        
    }
    
    public List<Integer> getTankLinkTrIds()
    {
        List<Integer> tankLinkIds = new ArrayList<>();
        for (TankMcu tank : tanks)
        {
            tankLinkIds.add(tank.getLinkTrId());
        }
        return tankLinkIds;        
    }
    
    public void setTankPosition(Integer tankLinkTrId, Coordinate tankCoords, Orientation tankOrientation)
    {
        TankMcu tank = this.getTankByLinkTrId(tankLinkTrId);
        tank.setPosition(tankCoords);
        tank.setOrientation(tankOrientation);
        tank.populateEntity(getUnitLeader());
    }


    public void prepareTankForCoop() throws PWCGException
    {
        for (TankMcu tank : tanks)
        {
            if (tank.isActivePlayerTank())
            {
                tank.setCoopStart(1);
                tank.setAiLevel(AiSkillLevel.ACE);
            }
            else
            {
                tank.setCoopStart(0);
            }
        }
    }
    

    public void write(BufferedWriter writer) throws PWCGException 
    {
        for (int i = 0; i < tanks.size(); ++i)
        {
            TankMcu tank = tanks.get(i);
            tank.write(writer);
        }
    }
    

    public int getUnitSize()
    {
        return tanks.size();
    }
  
    public void finalize() throws PWCGException
    {
        
    }
}
