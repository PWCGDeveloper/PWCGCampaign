package pwcg.aar.inmission.phase3.reconcile.victories;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;

public class PlayerClaimPlaneNameFinder
{
    public String getShotDownPlaneDisplayName(PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        PlaneType shotDownPlane = PWCGContextManager.getInstance().getPlaneTypeFactory().getPlaneTypeByAnyName(resultVictory.getVictim().getVehicleType());
        if (shotDownPlane != null)
        {
            return shotDownPlane.getDisplayName();
        }

        shotDownPlane = PWCGContextManager.getInstance().getPlaneTypeFactory().getPlaneTypeByAnyName(playerDeclaration.getAircraftType());
        if (shotDownPlane != null)
        {
            return shotDownPlane.getDisplayName();
        }
        
       return "";
    }

}
