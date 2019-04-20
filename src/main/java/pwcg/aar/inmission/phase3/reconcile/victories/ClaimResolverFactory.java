package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase3.reconcile.victories.coop.ClaimResolverCoop;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.ClaimDenier;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.ClaimResolverSinglePlayer;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.VerifiedVictoryGenerator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;

public class ClaimResolverFactory
{
    public static IClaimResolver createClaimResolver(Campaign campaign, AARContext aarContext, Map<Integer, PlayerDeclarations> playerDeclarations)
    {
        if (campaign.getCampaignData().isCoop())
        {
            return new ClaimResolverCoop(campaign);
        }
        else
        {
            ClaimDenier claimDenier = new ClaimDenier(campaign, PWCGContextManager.getInstance().getPlaneTypeFactory());
            VerifiedVictoryGenerator verifiedVictoryGenerator = new VerifiedVictoryGenerator(campaign, aarContext);
            return new ClaimResolverSinglePlayer(campaign, verifiedVictoryGenerator, claimDenier, playerDeclarations);
        }
    }
}
