package pwcg.aar.inmission.phase3.reconcile.victories;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class PlayerClaimResolverFirm
{    
    private PlayerClaimPlaneNameFinder claimPlaneNameFinder = new PlayerClaimPlaneNameFinder();
    
    public String getShotDownPlaneDisplayNameAsFirm (SquadronMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        
        if (!resultVictory.isConfirmed())
        {
            if (resultVictory.getVictor() instanceof LogPlane)
            {
                LogPlane victorPlanePlane = (LogPlane)resultVictory.getVictor();
                if (PlayerVictoryResolver.isPlayerVictory(player, victorPlanePlane.getSerialNumber()))
                {
                    if (resultVictory.getVictim() instanceof LogPlane)
                    {
                        LogPlane victimPlane = (LogPlane)resultVictory.getVictim();
                        PlaneType shotDownPlane = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(victimPlane.getVehicleType());
                        PlaneType claimedPlane = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(playerDeclaration.getAircraftType());
        
                        if (shotDownPlane == null || claimedPlane == null)
                        {
                            Logger.log(LogLevel.ERROR, 
                                            "resolveAsFirmVictory: No plane found for claimed type " + playerDeclaration.getAircraftType() );
                            
                        }
                        else if (shotDownPlane.getType().equals(claimedPlane.getType()))
                        {
                            shotDownPlaneDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                        }
                    }
                }
            }
        }
        
        return shotDownPlaneDisplayName;
    }
        

    public String getShotDownPlaneDisplayNameAsFirmNotExact(PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
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
