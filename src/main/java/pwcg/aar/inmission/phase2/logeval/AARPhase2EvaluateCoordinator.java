package pwcg.aar.inmission.phase2.logeval;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARPhase2EvaluateCoordinator
{
    private Campaign campaign;
    private AARContext aarContext; 

    public AARPhase2EvaluateCoordinator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public AARMissionEvaluationData evaluateLogEvents() throws PWCGException
    {
        AAREvaluator evaluator = new AAREvaluator(campaign, aarContext);
        AARMissionEvaluationData evaluationData = evaluator.determineMissionResults();
        return evaluationData;
    }


}
