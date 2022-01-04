package pwcg.campaign.resupply.depot;

import java.util.Date;

import pwcg.campaign.tank.TankArchType;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementUtils
{

    public static String getTypeForReplacement(Date campaignDate, TankArchType planeArchType) throws PWCGException
    {
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaignDate);
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeArchType.getInProductionMemberTankTypes(campaignDate));
        String planeTypeName = equipmentWeightCalculator.getTankTypeFromWeight();
        return planeTypeName;
    }

}
