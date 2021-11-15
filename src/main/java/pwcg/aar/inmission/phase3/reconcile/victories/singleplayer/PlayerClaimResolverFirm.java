package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class PlayerClaimResolverFirm
{    
    private PlayerClaimPlaneNameFinder claimPlaneNameFinder = new PlayerClaimPlaneNameFinder();
    
    public String getShotDownPlaneDisplayNameAsFirm (SquadronMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        
        if (!resultVictory.isConfirmed())
        {
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                if (resultVictory.getVictor() instanceof LogPlane)
                {
                    LogPlane victorPlanePlane = (LogPlane)resultVictory.getVictor();
                    if (PlayerVictoryResolver.isPlayerVictory(player, victorPlanePlane.getPilotSerialNumber()))
                    {
                        if (resultVictory.getVictim() instanceof LogPlane)
                        {
                            LogPlane victimPlane = (LogPlane)resultVictory.getVictim();
                            PlaneType shotDownPlane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(victimPlane.getVehicleType());
                            PlaneType claimedPlane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(playerDeclaration.getAircraftType());
            
                            if (shotDownPlane == null || claimedPlane == null)
                            {
                                PWCGLogger.log(LogLevel.ERROR, 
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
        }
        
        return shotDownPlaneDisplayName;
    }
        

    public String getShotDownPlaneDisplayNameAsFirmNotExact(SquadronMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        if (!resultVictory.isConfirmed())
        {
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                LogPlane victorPlanePlane = (LogPlane)resultVictory.getVictor();
                if (PlayerVictoryResolver.isPlayerVictory(player, victorPlanePlane.getPilotSerialNumber()))
                {
                    PwcgRoleCategory victimApproximateRole = resultVictory.getVictim().getRoleCategory();
                    
                    PlaneType declaredPlane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(playerDeclaration.getAircraftType());
                    if (declaredPlane != null)
                    {
                        PwcgRoleCategory declarationApproximateRole = declaredPlane.determinePrimaryRoleCategory();
                        
                        if (declarationApproximateRole == victimApproximateRole)
                        {
                            shotDownPlaneDisplayName = claimPlaneNameFinder.getShotDownPlaneDisplayName(playerDeclaration, resultVictory);
                        }
                    }
                }
            }
        }
        
        return shotDownPlaneDisplayName;
    }
}
