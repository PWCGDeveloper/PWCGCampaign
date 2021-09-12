package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.IWeight;
import pwcg.core.utils.WeightCalculator;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;

public class GroundVictimGenerator
{    
    private SquadronMember squadronMember;
    private Date date;
    
    public GroundVictimGenerator (Date date, SquadronMember squadronMember) throws PWCGException
    {
        this.date = date;
        this.squadronMember = squadronMember;
    }

    public IVehicle generateVictimVehicle() throws PWCGException 
    {
        ICountry victimCountry = PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(squadronMember.getSide().getOppositeSide());
        
        VehicleClass victimType = determineVehicleClass();
        IVehicle victimVehicle = VehicleFactory.createVehicle(victimCountry, date, victimType);
        return victimVehicle;
    }

    private VehicleClass determineVehicleClass() throws PWCGException
    {
        List<IWeight> possibleVictimTypes = new ArrayList<>();
        for (VehicleClass vehicleClass : VehicleClass.values()) 
        {
            if (vehicleClass.getWeight() > 0)
            {
                possibleVictimTypes.add(vehicleClass);
            }
        }
        
        WeightCalculator weightCalculator = new WeightCalculator(possibleVictimTypes);
        int index = weightCalculator.getItemFromWeight();
        VehicleClass victimType = (VehicleClass)possibleVictimTypes.get(index);
        return victimType;
    }
    
    public static boolean shouldUse(PwcgRole role)
    {
        if (role == PwcgRole.ROLE_ATTACK || role == PwcgRole.ROLE_DIVE_BOMB)
        {
            return true;
        }
        return false;
    }
}
