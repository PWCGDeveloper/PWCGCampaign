package pwcg.aar;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase1.parse.AARLogEvaluationCoordinator;
import pwcg.aar.inmission.phase1.parse.AARMissionLogRawData;
import pwcg.aar.inmission.phase1.parse.event.ATypeBase;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.prelim.AARPhase0Preliminary;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

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

    public void submitAAR(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        try
        {
            parseLogs();
            performMissionAAR(playerDeclarations);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            MissionResultErrorBundleCreator errorBundleCreator = new MissionResultErrorBundleCreator();
            errorBundleFileName = errorBundleCreator.createErrorBundle();
        }
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
            PWCGLogger.logException(e);
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
            PWCGLogger.logException(e);
            MissionResultErrorBundleCreator errorBundleCreator = new MissionResultErrorBundleCreator();
            errorBundleFileName = errorBundleCreator.createErrorBundle();
        }

        campaign.setCurrentMission(null);
    }

    public String getErrorBundleFileName()
    {
        return errorBundleFileName;
    }

    public AARContext getAarContext()
    {
        return aarContext;
    }
    
    void reset(Campaign campaign)
    {
        this.campaign = campaign;
        errorBundleFileName = "";
        aarContext = new AARContext(campaign);
        ATypeBase.reset();
    }

    void performMissionAAR(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        AARCoordinatorMissionHandler missionHandler = new AARCoordinatorMissionHandler(campaign, aarContext);
        missionHandler.tabulateMissionAAR(playerDeclarations);
    }

    void parseLogs() throws PWCGException
    {
        AARLogEvaluationCoordinator logEvaluationCoordinator = new AARLogEvaluationCoordinator();
        AARMissionLogRawData missionLogRawData = logEvaluationCoordinator.performAARPhase1Parse(campaign, aarContext.getPreliminaryData().getMissionLogFileSet());
        aarContext.setMissionLogRawData(missionLogRawData);
    }
}
