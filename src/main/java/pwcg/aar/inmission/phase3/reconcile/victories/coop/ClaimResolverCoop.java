package pwcg.aar.inmission.phase3.reconcile.victories.coop;

import pwcg.aar.inmission.phase3.reconcile.victories.IClaimResolver;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class ClaimResolverCoop implements IClaimResolver
{
    private Campaign campaign;
    private ReconciledVictoryData reconciledMissionData = new ReconciledVictoryData();

    public ClaimResolverCoop(Campaign campaign) 
    {        
        this.campaign = campaign;
    }

    @Override
    public ReconciledVictoryData resolvePlayerClaims() throws PWCGException
    {
        return reconciledMissionData;
    }
}
