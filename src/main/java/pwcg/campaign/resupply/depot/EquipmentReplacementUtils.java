package pwcg.campaign.resupply.depot;

import java.util.Date;

import pwcg.campaign.plane.PlaneArchType;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementUtils
{

    public static String getTypeForReplacement(Date campaignDate, PlaneArchType planeArchType) throws PWCGException
    {
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaignDate);
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeArchType.getInProductionMemberPlaneTypes(campaignDate));
        String planeTypeName = equipmentWeightCalculator.getPlaneTypeFromWeight();
        return planeTypeName;
    }

}
