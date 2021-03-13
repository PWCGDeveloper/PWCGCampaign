package pwcg.aar.inmission.phase1.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pwcg.aar.prelim.AARHeaderParser;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.MissionHeader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AARLogSetValidator.class})
public class AARMissionFileLogResultMatcherTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignData campaignData;
    @Mock private PwcgMissionData pwcgMissionData;       
    @Mock private MissionHeader missionHeader;
    @Mock private AARHeaderParser aarHeaderParser;        
    
    @Mock private File file_2016_10_25;
    @Mock private File file_2016_10_26;
    @Mock private File file_2016_10_27;

    @InjectMocks
    AARMissionFileLogResultMatcher missionFileLogResultMatcher;
    
    @Before
    public void setup() throws PWCGException
    {
        PowerMockito.mockStatic(AARLogSetValidator.class);

        Mockito.when(file_2016_10_25.getName()).thenReturn("missionReport(2016-10-25-22-51-08)[0].txt");
        Mockito.when(file_2016_10_26.getName()).thenReturn("missionReport(2016-10-26-22-51-08)[0].txt");
        Mockito.when(file_2016_10_27.getName()).thenReturn("missionReport(2016-10-27-22-51-08)[0].txt");

        Mockito.when(file_2016_10_25.exists()).thenReturn(true);
        Mockito.when(file_2016_10_26.exists()).thenReturn(true);
        Mockito.when(file_2016_10_27.exists()).thenReturn(true);

        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);
        Mockito.when(campaignData.getName()).thenReturn("Patrik Schorner");
        
        Mockito.when(aarHeaderParser.parseHeaderOnly(ArgumentMatchers.<String>any(), ArgumentMatchers.<File>any())).thenReturn("Patrik Schorner 1941-10-02");
    }
    
    @Test
    public void testMissionResultFileMatch() throws PWCGException
    {        
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Patrik Schorner 1941-10-02");
        
        List<File>logFileSets = new ArrayList<>();
        logFileSets.add(file_2016_10_25);
        
        Mockito.when(AARLogSetValidator.isLogSetValid(Mockito.any(), Mockito.any())).thenReturn(true);

        AARMissionLogFileSet aarLogFileMissionFile = missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        assert (aarLogFileMissionFile.getLogFileName().equals("missionReport(2016-10-25-22-51-08)[0]"));
    }
    
    @Test
    public void testMissionResultFileChooseLatest() throws PWCGException
    {        
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Patrik Schorner 1941-10-02");
        
        List<File>logFileSets = new ArrayList<>();
        logFileSets.add(file_2016_10_27);
        logFileSets.add(file_2016_10_26);
        logFileSets.add(file_2016_10_25);
        
        Mockito.when(AARLogSetValidator.isLogSetValid(Mockito.any(), Mockito.any())).thenReturn(true);

        AARMissionLogFileSet aarLogFileMissionFile = missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        assert (aarLogFileMissionFile.getLogFileName().equals("missionReport(2016-10-27-22-51-08)[0]"));
    }
    
    @Test
    public void testMissionResultFileMatchButInvalid() throws PWCGException
    {        
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Patrik Schorner 1941-10-02");
        
        List<File>logFileSets = new ArrayList<>();
        logFileSets.add(file_2016_10_25);
        
        Mockito.when(AARLogSetValidator.isLogSetValid(Mockito.any(), Mockito.any())).thenReturn(false);

        AARMissionLogFileSet aarLogFileMissionFile = missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        assert (aarLogFileMissionFile == null);
    }
    
    @Test 
    public void testMissionResultFileMatchFail() throws PWCGException
    {
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Wrong File");
        
        List<File>logFileSets = new ArrayList<>();
        logFileSets.add(file_2016_10_25);
        
        AARMissionLogFileSet aarLogFileMissionFile = missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        assert (aarLogFileMissionFile == null);
    }


	@Test 
	public void testMissionResultFileMatchFailEmptyList() throws PWCGException
	{
        
        List<File>logFileSets = new ArrayList<>();
        AARMissionLogFileSet aarLogFileMissionFile = missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        assert (aarLogFileMissionFile == null);
	}
}
