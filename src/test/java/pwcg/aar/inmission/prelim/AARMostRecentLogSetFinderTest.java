package pwcg.aar.inmission.prelim;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARMissionFileLogResultMatcher;
import pwcg.aar.inmission.phase1.parse.AARMissionLogFileSet;
import pwcg.aar.prelim.AARMostRecentLogSetFinder;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARMostRecentLogSetFinderTest
{

    @Mock
    private AARMissionFileLogResultMatcher matcher;

    @Mock
    private PwcgMissionData evalPwcgMissionData1;

    @Mock
    private PwcgMissionData evalPwcgMissionData2;

    @Mock
    private AARMissionLogFileSet aarLogFileMissionFile;
    
    private List<String> sortedLogSets = new ArrayList<String>();
    
    @Before
    public void setup() throws PWCGException
    {
        Mockito.when(matcher.matchMissionFileAndLogFile(evalPwcgMissionData1, sortedLogSets)).thenReturn(aarLogFileMissionFile);
        Mockito.when(evalPwcgMissionData1.getMissionDescription()).thenReturn("Mission1");
        Mockito.when(evalPwcgMissionData2.getMissionDescription()).thenReturn("Mission2");
    }
    
    @Test
    public void testGetMostRecentLogSet () throws PWCGException
    {
        List<PwcgMissionData> sortedPwcgMissionDataForCampaign = new ArrayList<>();
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData2);
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData1);

        sortedLogSets.add("LogSet1");
        sortedLogSets.add("LogSet2");
        sortedLogSets.add("LogSet3");

        AARMostRecentLogSetFinder logSetFinder = new AARMostRecentLogSetFinder(matcher);
        logSetFinder.getMostRecentAARLogFileMissionDataSetForCampaign(sortedLogSets, sortedPwcgMissionDataForCampaign);
        PwcgMissionData missionData = logSetFinder.getPwcgMissionData();
        String missionDescription = missionData.getMissionDescription();
        assert(missionDescription.equals("Mission2"));
    }

}
