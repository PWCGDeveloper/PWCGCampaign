package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.TankType;
import pwcg.campaign.tank.TankTypeFactory;
import pwcg.core.exception.PWCGException;

public class ClaimDenier
{
    private Campaign campaign;
    private TankTypeFactory tankFactory;

    public ClaimDenier(Campaign campaign, TankTypeFactory tankFactory)
    {
        this.campaign = campaign;
        this.tankFactory = tankFactory;
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
        String tankDesc = getPlaneDescription(declaration);
        CrewMember player = campaign.getPersonnelManager().getAnyCampaignMember(playerSerialNumber);

        boolean isNewsworthy = false;
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(campaign, tankDesc, player.getCompanyId(), player.getSerialNumber(), campaign.getDate(),
                isNewsworthy);

        return claimDenied;
    }

    private String getPlaneDescription(PlayerVictoryDeclaration playerDeclaration)
    {
        String tankDesc = "Unknown";
        TankType tank = tankFactory.createTankTypeByAnyName(playerDeclaration.getAircraftType());
        if (tank != null)
        {
            tankDesc = tank.getDisplayName();
        }
        return tankDesc;
    }
}
