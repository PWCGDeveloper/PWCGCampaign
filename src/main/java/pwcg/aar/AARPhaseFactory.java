package pwcg.aar;

import pwcg.aar.inmission.phase1.parse.AARLogEvaluationCoordinator;

public class AARPhaseFactory
{
    public static AARLogEvaluationCoordinator makePhase1Coordinator()
    {
        AARLogEvaluationCoordinator phase1Coordinator = new AARLogEvaluationCoordinator();
        return phase1Coordinator;
    }
}
