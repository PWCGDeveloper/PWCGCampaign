package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class PlayerClaimPlaneNameFinder
{
    public String getShotDownPlaneDisplayName(PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        TankType shotDownPlane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(resultVictory.getVictim().getVehicleType());
        if (shotDownPlane != null)
        {
            return shotDownPlane.getDisplayName();
        }

        shotDownPlane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(playerDeclaration.getAircraftType());
        if (shotDownPlane != null)
        {
            return shotDownPlane.getDisplayName();
        }
        
       return "";
    }

}
