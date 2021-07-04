package pwcg.aar;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase1.parse.AARLogSetValidator;
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
        AARPreliminaryData preliminaryData = aarPreliminary.createAARPreliminaryData();
        aarContext.setPreliminaryData(preliminaryData);
        
        AARLogSetValidator logSetValidator = new AARLogSetValidator();
        logSetValidator.isLogSetValid(campaign, preliminaryData);
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
    
    void reset(Campaign campaign) throws PWCGException
    {
        this.campaign = campaign;
        errorBundleFileName = "";
        aarContext = AARFactory.makeAARContext(campaign);
        ATypeBase.reset();
    }

    void performMissionAAR(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        AARCoordinatorMissionHandler missionHandler = new AARCoordinatorMissionHandler(campaign, aarContext);
        missionHandler.tabulateMissionAAR(playerDeclarations);
    }

    void parseLogs() throws PWCGException
    {
        AARLogParser logParser = new AARLogParser(aarContext.getPreliminaryData().getMissionLogFileSet());
        AARMissionLogRawData missionLogRawData = logParser.parseLogFilesForMission(campaign);
        aarContext.setMissionLogRawData(missionLogRawData);
    }
}
