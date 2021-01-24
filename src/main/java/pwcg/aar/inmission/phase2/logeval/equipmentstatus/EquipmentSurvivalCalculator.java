package pwcg.aar.inmission.phase2.logeval.equipmentstatus;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class EquipmentSurvivalCalculator
{
    private Coordinate downAt;
    private Airfield field;

    public EquipmentSurvivalCalculator(Coordinate downAt, Airfield field)
    {
        this.downAt = downAt;
        this.field = field;
    }

    public boolean isPlaneDestroyed() throws PWCGException
    {
        boolean isPlaneDestroyed = true;
        if (downAt != null && field != null)
        {
            if (MathUtils.calcDist(downAt, field.getPosition()) < 5000.0)
            {
                isPlaneDestroyed = false;
            }
        }
        return isPlaneDestroyed;
    }

}
