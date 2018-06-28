package pwcg.aar;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase1.parse.AARLogEvaluationCoordinator;
import pwcg.aar.inmission.phase1.parse.AARMissionLogRawData;
import pwcg.aar.inmission.phase3.reconcile.victories.PlayerDeclarations;
import pwcg.aar.prelim.AARPhase0Preliminary;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;

public class AARCoordinator
{
    private static AARCoordinator instance = new AARCoordinator();
    
    private Campaign campaign;
    private String errorBundleFileName = "";
	private AARContext aarContext; 

    private AARCoordinator()
    {
    }
    
    public static AARCoordinator getInstance()
    {
        return instance;
    }
    
    public void aarPreliminary(Campaign campaign) throws PWCGException
    {
        reset(campaign);

        AARPhase0Preliminary aarPreliminary = new AARPhase0Preliminary(campaign);
        AARPreliminaryData aarPreliminaryData = aarPreliminary.createAARPreliminaryData();
        aarContext.setPreliminaryData(aarPreliminaryData);
    }

    public void submitAAR(PlayerDeclarations playerDeclarations) throws PWCGException
    {
        try
        {
            parseLogs();
            evaluateInMission(playerDeclarations);
        }
        catch (Exception e)
        {
            Logger.logException(e);
            MissionResultErrorBundleCreator errorBundleCreator = new MissionResultErrorBundleCreator();
            errorBundleFileName = errorBundleCreator.createErrorBundle();
        }
    }
    
    public void parseLogs() throws PWCGException
    {
        AARLogEvaluationCoordinator logEvaluationCoordinator = new AARLogEvaluationCoordinator();
        AARMissionLogRawData missionLogRawData = logEvaluationCoordinator.performAARPhase1Parse(aarContext.getPreliminaryData().getMissionLogFileSet());
        aarContext.setMissionLogRawData(missionLogRawData);
    }

    public void evaluateInMission(PlayerDeclarations playerDeclarations) throws PWCGException
    {
        AARCoordinatorMissionHandler missionHandler = new AARCoordinatorMissionHandler(campaign, aarContext);
        missionHandler.handleInMissionAAR(playerDeclarations);
    }


    public void completeAAR() throws PWCGException
    {
        campaign.write();
        campaign.setCurrentMission(null);
    }
    
    
    public void submitLeave(Campaign campaign, int timePassedDays) throws PWCGException
    {
        try
        {
            this.campaign = campaign;
            reset(campaign);
            AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);       
            extendedTimeHandler.timePassedForLeave(timePassedDays);
            completeAAR();
        }
        catch (Exception e)
        {
            Logger.logException(e);
            MissionResultErrorBundleCreator errorBundleCreator = new MissionResultErrorBundleCreator();
            errorBundleFileName = errorBundleCreator.createErrorBundle();
        }

        campaign.setCurrentMission(null);
   }
    
    public void submitTransfer(Campaign campaign, int timePassedDays) throws PWCGException
    {
        try
        {
            this.campaign = campaign;
            reset(campaign);
            AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);       
            extendedTimeHandler.timePassedForTransfer(timePassedDays);
            completeAAR();
        }
        catch (Exception e)
        {
            Logger.logException(e);
            MissionResultErrorBundleCreator errorBundleCreator = new MissionResultErrorBundleCreator();
            errorBundleFileName = errorBundleCreator.createErrorBundle();
        }

        campaign.setCurrentMission(null);
    }

    public void reset(Campaign campaign)
    {
        this.campaign = campaign;
        errorBundleFileName = "";
        aarContext = new AARContext(campaign);
    }

    public String getErrorBundleFileName()
    {
        return errorBundleFileName;
    }

    public AARContext getAarContext()
    {
        return aarContext;
    }
}
