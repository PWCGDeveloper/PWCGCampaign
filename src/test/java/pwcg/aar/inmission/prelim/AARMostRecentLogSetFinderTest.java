package pwcg.aar.inmission.prelim;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase1.parse.AARMissionFileLogResultMatcher;
import pwcg.aar.prelim.AARMostRecentLogSetFinder;
import pwcg.aar.prelim.AARPwcgMissionFinder;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogFileSet;
import pwcg.core.logfiles.LogSetFinder;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARMostRecentLogSetFinderTest
{

    @Mock private AARMissionFileLogResultMatcher matcher;
    @Mock private LogSetFinder logSetFinder;
    @Mock private AARPwcgMissionFinder pwcgMissionFinder;
    @Mock private PwcgMissionData evalPwcgMissionData1;
    @Mock private PwcgMissionData evalPwcgMissionData2;
    @Mock private MissionHeader missionHeader1;
    @Mock private MissionHeader missionHeader2;
    @Mock private LogFileSet aarLogFileMissionFile;

    @Mock private Campaign campaign;
    
    private List<String> sortedLogSets = new ArrayList<String>();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        Mockito.when(logSetFinder.getSortedLogFileSets()).thenReturn(sortedLogSets);
        Mockito.when(evalPwcgMissionData1.getMissionDescription()).thenReturn("Mission1");
        Mockito.when(evalPwcgMissionData2.getMissionDescription()).thenReturn("Mission2");
        Mockito.when(evalPwcgMissionData1.getMissionHeader()).thenReturn(missionHeader1);
        Mockito.when(evalPwcgMissionData2.getMissionHeader()).thenReturn(missionHeader2);
        
        Mockito.when(missionHeader1.getDate()).thenReturn("19420101");
        Mockito.when(missionHeader2.getDate()).thenReturn("19420103");
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420101"));

        List<PwcgMissionData> missionDataForCampaign = new ArrayList<>();
        missionDataForCampaign.add(evalPwcgMissionData2);
        missionDataForCampaign.add(evalPwcgMissionData1);
        Mockito.when(pwcgMissionFinder.getSortedPwcgMissionsForCampaign()).thenReturn(missionDataForCampaign);
    }
    
    @Test
    public void testGetMostRecentLogSet () throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420103"));
        Mockito.when(matcher.matchMissionFileAndLogFile(evalPwcgMissionData2, sortedLogSets)).thenReturn(aarLogFileMissionFile);

        List<PwcgMissionData> sortedPwcgMissionDataForCampaign = new ArrayList<>();
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData2);
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData1);

        sortedLogSets.add("LogSet1");
        sortedLogSets.add("LogSet2");
        sortedLogSets.add("LogSet3");

        AARMostRecentLogSetFinder mostRecentogSetFinder = new AARMostRecentLogSetFinder(campaign, matcher, logSetFinder, pwcgMissionFinder);
        mostRecentogSetFinder.determineMostRecentAARLogFileMissionDataSetForCampaign();
        PwcgMissionData missionData = mostRecentogSetFinder.getPwcgMissionData();
        String missionDescription = missionData.getMissionDescription();
        assert(missionDescription.equals("Mission2"));
        assert(mostRecentogSetFinder.isLogSetComplete() == true);
    }
    
    @Test
    public void testGetOlderSetLogSet () throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420101"));
        Mockito.when(matcher.matchMissionFileAndLogFile(evalPwcgMissionData1, sortedLogSets)).thenReturn(aarLogFileMissionFile);

        List<PwcgMissionData> sortedPwcgMissionDataForCampaign = new ArrayList<>();
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData2);
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData1);

        sortedLogSets.add("LogSet1");
        sortedLogSets.add("LogSet2");
        sortedLogSets.add("LogSet3");

        AARMostRecentLogSetFinder mostRecentogSetFinder = new AARMostRecentLogSetFinder(campaign, matcher, logSetFinder, pwcgMissionFinder);
        mostRecentogSetFinder.determineMostRecentAARLogFileMissionDataSetForCampaign();
        PwcgMissionData missionData = mostRecentogSetFinder.getPwcgMissionData();
        String missionDescription = missionData.getMissionDescription();
        assert(missionDescription.equals("Mission1"));
        assert(mostRecentogSetFinder.isLogSetComplete() == true);
    }
    
    @Test
    public void testNoLogSetFound () throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420104"));

        List<PwcgMissionData> sortedPwcgMissionDataForCampaign = new ArrayList<>();
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData2);
        sortedPwcgMissionDataForCampaign.add(evalPwcgMissionData1);

        sortedLogSets.add("LogSet1");
        sortedLogSets.add("LogSet2");
        sortedLogSets.add("LogSet3");

        AARMostRecentLogSetFinder mostRecentogSetFinder = new AARMostRecentLogSetFinder(campaign, matcher, logSetFinder, pwcgMissionFinder);
        mostRecentogSetFinder.determineMostRecentAARLogFileMissionDataSetForCampaign();
        assert(mostRecentogSetFinder.isLogSetComplete() == false);
    }

}
