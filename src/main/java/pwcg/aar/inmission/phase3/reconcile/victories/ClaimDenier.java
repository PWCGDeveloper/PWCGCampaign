package pwcg.aar.inmission.phase3.reconcile.victories;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class ClaimDenier
{
    private Campaign campaign;
    private PlaneTypeFactory planeFactory;
  
    public ClaimDenier (Campaign campaign, PlaneTypeFactory planeFactory)
    {
        this.campaign = campaign;
        this.planeFactory = planeFactory;
    }
    
    public ClaimDeniedEvent determineClaimDenied(PlayerVictoryDeclaration playerDeclaration) throws PWCGException 
    {
        if (!playerDeclaration.isConfirmed())
        {
            return createPlaneDenied(playerDeclaration);                
        }
        
        return null;
    }

    private ClaimDeniedEvent createPlaneDenied(PlayerVictoryDeclaration playerDeclaration) throws PWCGException
    {
        String planeDesc = getPlaneDescription(playerDeclaration);
        SquadronMember player = campaign.getPlayer();
        
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(); 
        claimDenied.setDate(campaign.getDate());
        claimDenied.setSquadron(player.determineSquadron().determineDisplayName(campaign.getDate()));
        claimDenied.setType(planeDesc);
        claimDenied.setPilot(player);
        
        return claimDenied;
    }

    private String getPlaneDescription(PlayerVictoryDeclaration playerDeclaration)
    {
        String planeDesc = "Unknown";
        if (playerDeclaration.getAircraftType().equals(PlaneType.BALLOON))
        {
            planeDesc = PlaneType.BALLOON;
        }
        else
        {
            PlaneType plane = planeFactory.createPlaneTypeByAnyName(playerDeclaration.getAircraftType());
            if (plane != null)
            {
                planeDesc = plane.getDisplayName();
            }                        
        }
        return planeDesc;
    }
}
