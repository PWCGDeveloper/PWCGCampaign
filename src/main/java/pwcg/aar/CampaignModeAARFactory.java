package pwcg.aar;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.coop.ClaimResolverCompetitiveCoop;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.ClaimDenier;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.ClaimResolverSinglePlayer;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.VerifiedVictoryGenerator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignModeChooser;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class CampaignModeAARFactory
{
    public static IClaimResolver createClaimResolver(Campaign campaign, AARContext aarContext, Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        if (CampaignModeChooser.isCampaignModeCompetitive(campaign))
        {
            return new ClaimResolverCompetitiveCoop(campaign, aarContext.getMissionEvaluationData().getVictoryResults());
        }
        else
        {
            ClaimDenier claimDenier = new ClaimDenier(campaign, PWCGContext.getInstance().getTankTypeFactory());
            VerifiedVictoryGenerator verifiedVictoryGenerator = new VerifiedVictoryGenerator(campaign, aarContext);
            return new ClaimResolverSinglePlayer(campaign, verifiedVictoryGenerator, claimDenier, playerDeclarations);
        }
    }
}
