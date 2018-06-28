package pwcg.aar.inmission.phase1.parse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARMissionFileLogResultMatcher;
import pwcg.aar.inmission.phase1.parse.AARMissionLogFileSet;
import pwcg.aar.prelim.AARMostRecentLogSetFinder;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARMostRecentFinderTest
{
    @Mock
    AARMissionFileLogResultMatcher aarLogFileMatcher;

    @Mock
    AARMissionLogFileSet aarMissionLogFileSet;

    @Mock
    PwcgMissionData pwcgMissionData;

    @Mock
    List<String> logFileSets;
    
    @InjectMocks
    AARMostRecentLogSetFinder mostRecentFinder;
        
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
    }
    
    @Test
    public void testGetLatestMissionLogSet() throws PWCGException 
    {
        String expected = "missionReportCCC";        
        Mockito.when(aarMissionLogFileSet.getLogFileName()).thenReturn(expected);
        Mockito.when(aarLogFileMatcher.matchMissionFileAndLogFile(Matchers.<PwcgMissionData>any(), Matchers.<List<String>>any())).thenReturn(aarMissionLogFileSet);
                
        List<PwcgMissionData> pwcgMissionDataForCampaign = new ArrayList<>();
        pwcgMissionDataForCampaign.add(pwcgMissionData);
        mostRecentFinder.getMostRecentAARLogFileMissionDataSetForCampaign(logFileSets, pwcgMissionDataForCampaign);
        AARMissionLogFileSet missionLogSet = mostRecentFinder.getAarLogFileMissionFile();
        assert (missionLogSet != null);
    }
    
    @Test
    public void testGetLatestMissionLogSetNotFound() throws PWCGException 
    {
        Mockito.when(aarLogFileMatcher.matchMissionFileAndLogFile(Matchers.<PwcgMissionData>any(), Matchers.<List<String>>any())).thenReturn(null);
        
        List<PwcgMissionData> pwcgMissionDataForCampaign = new ArrayList<>();
        pwcgMissionDataForCampaign.add(pwcgMissionData);
        mostRecentFinder.getMostRecentAARLogFileMissionDataSetForCampaign(logFileSets, pwcgMissionDataForCampaign);
        AARMissionLogFileSet missionLogSet = mostRecentFinder.getAarLogFileMissionFile();
        
        assert (missionLogSet == null);
    }
    
    @Test
    public void testGetLatestMissionLogSetNoMatchingSet() throws PWCGException 
    {
        Mockito.when(aarLogFileMatcher.matchMissionFileAndLogFile(Matchers.<PwcgMissionData>any(), Matchers.<List<String>>any())).thenReturn(new AARMissionLogFileSet());
        
        List<PwcgMissionData> pwcgMissionDataForCampaign = new ArrayList<>();
        pwcgMissionDataForCampaign.add(pwcgMissionData);
        mostRecentFinder.getMostRecentAARLogFileMissionDataSetForCampaign(logFileSets, pwcgMissionDataForCampaign);
        AARMissionLogFileSet missionLogSet = mostRecentFinder.getAarLogFileMissionFile();
        
        assert (missionLogSet.getLogFileName().equals(AARMissionLogFileSet.NOT_AVAILABLE));
    }
}
