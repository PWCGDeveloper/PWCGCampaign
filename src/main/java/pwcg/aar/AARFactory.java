package pwcg.aar;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.inmission.phase1.parse.AARLogEvaluationCoordinator;
import pwcg.aar.inmission.phase1.parse.AARMissionFileLogResultMatcher;
import pwcg.aar.outofmission.phase2.awards.CampaignMemberAwardsGenerator;
import pwcg.aar.prelim.AARHeaderParser;
import pwcg.aar.prelim.AARLogSetFinder;
import pwcg.aar.prelim.AARMostRecentLogSetFinder;
import pwcg.aar.prelim.AARPwcgMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;

public class AARFactory
{
    public static AARLogEvaluationCoordinator makePhase1Coordinator()
    {
        AARLogEvaluationCoordinator phase1Coordinator = new AARLogEvaluationCoordinator();
        return phase1Coordinator;
    }
    
    public static AARMostRecentLogSetFinder makeMostRecentLogSetFinder(Campaign campaign) throws PWCGException
    {
        AARLogSetFinder logSetFinder = makeLogSorter();
        AARHeaderParser aarHeaderParser = new AARHeaderParser();        
        AARPwcgMissionFinder pwcgMissionFinder = new AARPwcgMissionFinder(campaign);
        AARMissionFileLogResultMatcher matcher = new AARMissionFileLogResultMatcher(campaign, aarHeaderParser);
        return new AARMostRecentLogSetFinder(campaign, matcher, logSetFinder, pwcgMissionFinder);
    }
    
    public static AARLogSetFinder makeLogSorter() throws PWCGException
    {
        DirectoryReader directoryReader = new DirectoryReader();
        AARLogSetFinder logSetFinder = new AARLogSetFinder(directoryReader);
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
