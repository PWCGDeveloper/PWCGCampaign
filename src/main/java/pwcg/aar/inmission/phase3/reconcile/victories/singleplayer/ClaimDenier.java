package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class ClaimDenier
{
    public static final String UNKNOWN = "Unknown";
    private Campaign campaign;
    private PlaneTypeFactory planeFactory;
  
    public ClaimDenier (Campaign campaign, PlaneTypeFactory planeFactory)
    {
        this.campaign = campaign;
        this.planeFactory = planeFactory;
    }
    
    public ClaimDeniedEvent determineClaimDenied(Integer playerSerialNumber, PlayerVictoryDeclaration declaration) throws PWCGException 
    {
        if (!declaration.isConfirmed())
        {
            return createPlaneDenied(playerSerialNumber, declaration);                
        }
        
        return null;
    }

    private ClaimDeniedEvent createPlaneDenied(Integer playerSerialNumber, PlayerVictoryDeclaration declaration) throws PWCGException
    {
        String planeDesc = getPlaneDescription(declaration);
        SquadronMember player = campaign.getPersonnelManager().getAnyCampaignMember(playerSerialNumber);
        
        boolean isNewsworthy = false;
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(campaign, planeDesc, player.getSquadronId(), player.getSerialNumber(), campaign.getDate(), isNewsworthy);
        
        return claimDenied;
    }

    private String getPlaneDescription(PlayerVictoryDeclaration playerDeclaration)
    {
        String planeDesc = UNKNOWN;
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
