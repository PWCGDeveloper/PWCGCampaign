package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.IWeight;
import pwcg.core.utils.WeightCalculator;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;

public class GroundVictimGenerator
{    
    private SquadronMember squadronMember;
    private Campaign campaign;
    
    public GroundVictimGenerator (Campaign campaign, SquadronMember squadronMember) throws PWCGException
    {
        this.campaign = campaign;
        this.squadronMember = squadronMember;
    }

    public IVehicle generateVictimVehicle() throws PWCGException 
    {
        ICountry victimCountry = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroundCountryForMapBySide(squadronMember.getSide().getOppositeSide());
        
        VehicleClass victimType = determineVehicleClass();
        IVehicle victimVehicle = VehicleFactory.createVehicle(victimCountry, campaign.getDate(), victimType);
        return victimVehicle;
    }

    private VehicleClass determineVehicleClass() throws PWCGException
    {
        List<IWeight> possibleVictimTypes = new ArrayList<>();
        for (VehicleClass vehicleClass : VehicleClass.values()) 
        {
            if (vehicleClass.getWeight() <= 0)
            {
                continue;
            }
            
            if (vehicleClass == VehicleClass.Tank && campaign.getDate().before(DateUtils.getDateYYYYMMDD("19171001")))
            {
                continue;
            }
                
            possibleVictimTypes.add(vehicleClass);
        }
        
        WeightCalculator weightCalculator = new WeightCalculator(possibleVictimTypes);
        int index = weightCalculator.getItemFromWeight();
        VehicleClass victimType = (VehicleClass)possibleVictimTypes.get(index);
        return victimType;
    }
    
    public static boolean shouldUse(PwcgRoleCategory squadronPrimaryRoleCategory)
    {
        if (squadronPrimaryRoleCategory == PwcgRoleCategory.ATTACK)
        {
            return true;
        }
        return false;
    }
}
