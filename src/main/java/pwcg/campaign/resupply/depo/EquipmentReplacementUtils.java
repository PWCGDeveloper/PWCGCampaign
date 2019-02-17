package pwcg.campaign.resupply.depo;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementUtils
{

    public static String getTypeForReplacement(Campaign campaign, PlaneArchType planeArchType) throws PWCGException
    {
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign);
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeArchType.getInProductionMemberPlaneTypes(campaign.getDate()));
        String planeTypeName = equipmentWeightCalculator.getPlaneTypeFromWeight();
        return planeTypeName;
    }

}
