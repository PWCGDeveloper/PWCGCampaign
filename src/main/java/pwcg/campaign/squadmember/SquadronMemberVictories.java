package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class SquadronMemberVictories
{
    private List<Victory>  airToAirVictories = new ArrayList<>();
    private List<Victory>  tankVictories = new ArrayList<>();
    private List<Victory>  groundVictories = new ArrayList<>();
    
    public SquadronMemberVictories (List<Victory> victories) throws PWCGException
    {
        for (Victory victory : victories)
        {
            if (victory.getVictim().getAirOrGround() == Victory.AIR_VICTORY || victory.getVictim().getAirOrGround() == Victory.UNSPECIFIED_VICTORY)
            {
                airToAirVictories.add(victory);
            }
            
            if (victory.getVictim().getAirOrGround() == Victory.GROUND_VICTORY)
            {
                VehicleDefinition vehicleDefinitionByName = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionByVehicleName(victory.getVictim().getType());
                if (vehicleDefinitionByName != null && vehicleDefinitionByName.getVehicleClass() == VehicleClass.Tank)
                {
                    tankVictories.add(victory);
                }
                else
                {
                    groundVictories.add(victory);
                }
            }
        }
    }

    public List<Victory> getAirToAirVictories()
    {
        return new ArrayList<>(airToAirVictories);
    }

    public List<Victory> getTankVictories()
    {
        return new ArrayList<>(tankVictories);
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

    public int getGroundVictoryCount()
    {
        return groundVictories.size();
    }

    public int getGroundVictoryPointTotal()
    {
        int numPilotGroundVictoryPoints = getGroundVictoryCount() + (getTankVictoryCount() * 3);
        return numPilotGroundVictoryPoints;
    }


}
