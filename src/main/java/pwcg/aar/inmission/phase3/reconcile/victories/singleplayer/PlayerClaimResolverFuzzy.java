package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;

public class PlayerClaimResolverFuzzy
{
    private PlayerClaimPlaneNameFinder claimPlaneNameFinder = new PlayerClaimPlaneNameFinder();

    public String getShotDownPlaneDisplayNameAsFuzzy (PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        
        if (!resultVictory.isConfirmed())
        {
            PlaneType shotDownPlane = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(resultVictory.getVictim().getVehicleType());
            PlaneType claimedPlane = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(playerDeclaration.getAircraftType());
                
            if (shotDownPlane != null && claimedPlane != null)
            {
                if (shotDownPlane.getType().equals(claimedPlane.getType()))
                {
                    shotDownPlaneDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                }
            }
        }
        
        return shotDownPlaneDisplayName;
    }

    public String getShotDownPlaneDisplayNameAsFuzzyNotExact(PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        if (!resultVictory.isConfirmed())
        {
            Role victimApproximateRole = Role.getApproximateRole(resultVictory.getVictim().getRole());
            PlaneType declaredPlane = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(playerDeclaration.getAircraftType());
            if (declaredPlane != null)
            {
                Role declarationApproximateRole = Role.getApproximateRole(declaredPlane.determinePrimaryRole());
                    
                if (declarationApproximateRole == victimApproximateRole)
                {
                    shotDownPlaneDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                }
            }
        }
        
        return shotDownPlaneDisplayName;
    }


}
