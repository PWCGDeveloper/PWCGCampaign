package pwcg.aar;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.inmission.phase1.parse.AARMissionFileLogResultMatcher;
import pwcg.aar.inmission.phase2.logeval.AARBotVehicleMapper;
import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.AARVehiclePlaneLanded;
import pwcg.aar.outofmission.phase2.awards.CampaignMemberAwardsGenerator;
import pwcg.aar.prelim.AARMostRecentLogSetFinder;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.AARPwcgMissionFinder;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.LogFileHeaderParser;
import pwcg.core.logfiles.LogParser;
import pwcg.core.logfiles.LogSetFinder;
import pwcg.core.utils.DirectoryReader;

public class AARFactory
{
    public static AARMostRecentLogSetFinder makeMostRecentLogSetFinder(Campaign campaign) throws PWCGException
    {
        LogSetFinder logSetFinder = makeLogSorter();
        AARPwcgMissionFinder pwcgMissionFinder = new AARPwcgMissionFinder(campaign);
        LogFileHeaderParser logHeaderParser = new LogFileHeaderParser();        
        LogParser logParser = new LogParser();
        AARMissionFileLogResultMatcher matcher = new AARMissionFileLogResultMatcher(campaign, logHeaderParser, logParser);
        return new AARMostRecentLogSetFinder(campaign, matcher, logSetFinder, pwcgMissionFinder);
    }
    
    public static AARVehicleBuilder makeAARVehicleBuilder(Campaign campaign, AARPreliminaryData preliminaryData, LogEventData logEventData) throws PWCGException
    {
        AARBotVehicleMapper botPlaneMapper = new AARBotVehicleMapper(logEventData);
        AARVehiclePlaneLanded landedMapper = new AARVehiclePlaneLanded(logEventData);
        PwcgMissionDataEvaluator pwcgMissionDataEvaluator = new PwcgMissionDataEvaluator(campaign, preliminaryData);
        return new AARVehicleBuilder(botPlaneMapper, landedMapper, pwcgMissionDataEvaluator);
    }

    public static LogSetFinder makeLogSorter() throws PWCGException
    {
        DirectoryReader directoryReader = new DirectoryReader();
        LogSetFinder logSetFinder = new LogSetFinder(directoryReader);
        return logSetFinder;
    }
    
    public static AARContext makeAARContext(Campaign campaign) throws PWCGException
    {
        return new AARContext(campaign);
    }
    
    public static AAROutOfMissionStepper makeAAROutOfMissionStepper(Campaign campaign, AARContext aarContext)
    {
        return new AAROutOfMissionStepper(campaign, aarContext);
    }
    
    public static CampaignMemberAwardsGenerator makeCampaignMemberAwardsGenerator(Campaign campaign, AARContext aarContext)
    {
        return new CampaignMemberAwardsGenerator(campaign, aarContext);
    }
    
    public static AARPersonnelAwards makeAARPersonnelAwards()
    {
        return new AARPersonnelAwards();
    }    
    
    public static AARPersonnelAcheivements makeAARPersonnelAcheivements()
    {
        return new AARPersonnelAcheivements();
    }
}
