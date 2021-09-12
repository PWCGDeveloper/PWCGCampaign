package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class PlayerClaimResolverFuzzy
{
    private PlayerClaimPlaneNameFinder claimPlaneNameFinder = new PlayerClaimPlaneNameFinder();

    public String getShotDownPlaneDisplayNameAsFuzzy (PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        
        if (!resultVictory.isConfirmed())
        {
            PlaneType shotDownPlane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(resultVictory.getVictim().getVehicleType());
            PlaneType claimedPlane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(playerDeclaration.getAircraftType());
                
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

    public String getShotDownPlaneDisplayNameAsFuzzyNotExact(SquadronMember player, PlayerVictoryDeclaration playerDeclaration, LogVictory resultVictory) throws PWCGException
    {
        String shotDownPlaneDisplayName = "";
        if (!resultVictory.isConfirmed())
        {
            if (!VictoryResolverSameSideDetector.isSameSide(player, resultVictory))
            {
                PwcgRole victimApproximateRole = PwcgRole.getApproximateRole(resultVictory.getVictim().getRole());
                PlaneType declaredPlane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(playerDeclaration.getAircraftType());
                if (declaredPlane != null)
                {
                    PwcgRole declarationApproximateRole = PwcgRole.getApproximateRole(declaredPlane.determinePrimaryRole());
                        
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
