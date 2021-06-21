package pwcg.aar.inmission.phase3.reconcile.victories;

import pwcg.core.exception.PWCGException;

public interface IClaimResolver
{

    ReconciledMissionVictoryData resolvePlayerClaims() throws PWCGException;

}