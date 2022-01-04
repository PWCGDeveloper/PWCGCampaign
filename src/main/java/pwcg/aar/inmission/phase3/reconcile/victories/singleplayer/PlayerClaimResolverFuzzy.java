package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class PlayerClaimResolverFuzzy
{
    private PlayerClaimPlaneNameFinder claimPlaneNameFinder = new PlayerClaimPlaneNameFinder();

    public String getShotDownPlaneDisplayNameAsFuzzy (PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        
        if (!resultVictory.isConfirmed())
        {
            TankType shotDownPlane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(resultVictory.getVictim().getVehicleType());
            TankType claimedPlane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(playerDeclaration.getAircraftType());
                
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

    public String getShotDownPlaneDisplayNameAsFuzzyNotExact(CrewMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        if (!resultVictory.isConfirmed())
        {
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                PwcgRoleCategory victimApproximateRole = resultVictory.getVictim().getRoleCategory();
                TankType declaredPlane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(playerDeclaration.getAircraftType());
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
        
        return shotDownPlaneDisplayName;
    }


}
