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
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;

@RunWith(MockitoJUnitRunner.class)
public class AARMostRecentLogSetFinderTest
{

    @Mock private AARMissionFileLogResultMatcher matcher;
    @Mock private PwcgMissionData evalPwcgMissionData1;
    @Mock private PwcgMissionData evalPwcgMissionData2;
    @Mock private MissionHeader missionHeader1;
    @Mock private MissionHeader missionHeader2;
    @Mock private AARMissionLogFileSet aarLogFileMissionFile;
    @Mock private Campaign campaign;
    
    private List<String> sortedLogSets = new ArrayList<String>();
    
    @Before
    public void setup() throws PWCGException
    {
        Mockito.when(matcher.matchMissionFileAndLogFile(evalPwcgMissionData1, sortedLogSets)).thenReturn(aarLogFileMissionFile);
        Mockito.when(evalPwcgMissionData1.getMissionDescription()).thenReturn("Mission1");
        Mockito.when(evalPwcgMissionData2.getMissionDescription()).thenReturn("Mission2");
        Mockito.when(evalPwcgMissionData1.getMissionHeader()).thenReturn(missionHeader1);
        Mockito.when(evalPwcgMissionData2.getMissionHeader()).thenReturn(missionHeader2);
        
        Mockito.when(missionHeader1.getDate()).thenReturn("19420101");
        Mockito.when(missionHeader2.getDate()).thenReturn("19420103");
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420101"));
    }
    
    @Test
    public void testGetMostRecentLogSet () throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420103"));

        List<PwcgMissionData> sortedPwcgMissionDataForCampaign = new ArrayList<>();
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData2);
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData1);

        sortedLogSets.add("LogSet1");
        sortedLogSets.add("LogSet2");
        sortedLogSets.add("LogSet3");

        AARMostRecentLogSetFinder logSetFinder = new AARMostRecentLogSetFinder(campaign, matcher);
        logSetFinder.getMostRecentAARLogFileMissionDataSetForCampaign(sortedLogSets, sortedPwcgMissionDataForCampaign);
        PwcgMissionData missionData = logSetFinder.getPwcgMissionData();
        String missionDescription = missionData.getMissionDescription();
        assert(missionDescription.equals("Mission2"));
    }
    
    @Test
    public void testGetOlderSetLogSet () throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420101"));

        List<PwcgMissionData> sortedPwcgMissionDataForCampaign = new ArrayList<>();
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData2);
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData1);

        sortedLogSets.add("LogSet1");
        sortedLogSets.add("LogSet2");
        sortedLogSets.add("LogSet3");

        AARMostRecentLogSetFinder logSetFinder = new AARMostRecentLogSetFinder(campaign, matcher);
        logSetFinder.getMostRecentAARLogFileMissionDataSetForCampaign(sortedLogSets, sortedPwcgMissionDataForCampaign);
        PwcgMissionData missionData = logSetFinder.getPwcgMissionData();
        String missionDescription = missionData.getMissionDescription();
        assert(missionDescription.equals("Mission1"));
    }
    
    @Test (expected = PWCGException.class)
    public void testNoLogSetFound () throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420104"));

        List<PwcgMissionData> sortedPwcgMissionDataForCampaign = new ArrayList<>();
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData2);
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData1);

        sortedLogSets.add("LogSet1");
        sortedLogSets.add("LogSet2");
        sortedLogSets.add("LogSet3");

        AARMostRecentLogSetFinder logSetFinder = new AARMostRecentLogSetFinder(campaign, matcher);
        logSetFinder.getMostRecentAARLogFileMissionDataSetForCampaign(sortedLogSets, sortedPwcgMissionDataForCampaign);
        assert(false);
    }

}
