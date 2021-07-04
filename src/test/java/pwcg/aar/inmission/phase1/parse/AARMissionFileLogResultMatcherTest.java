package pwcg.aar.inmission.phase1.parse;

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
    
    @InjectMocks
    AARMissionFileLogResultMatcher missionFileLogResultMatcher;
    
    @Before
    public void setup() throws PWCGException
    {
        PowerMockito.mockStatic(AARLogSetValidator.class);

        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);
        Mockito.when(campaignData.getName()).thenReturn("Patrik Schorner");
    }
    
    @Test
    public void testMissionResultFileMatch() throws PWCGException
    {        
        Mockito.when(aarHeaderParser.parseHeaderOnly(ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenReturn("Patrik Schorner 1941-10-02");
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Patrik Schorner 1941-10-02");
        
        List<String>logFileSets = new ArrayList<>();
        logFileSets.add("missionReport(2018-05-05_18-20-11)[0]");
        
        AARLogSetValidator logSetValidator = Mockito.spy(new AARLogSetValidator());
        Mockito.doNothing().when(logSetValidator).isLogSetValid(Mockito.any(), Mockito.any());

        AARMissionLogFileSet aarLogFileMissionFile = missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        assert (aarLogFileMissionFile.getLogFileName().equals("missionReport(2018-05-05_18-20-11)[0]"));
    }
    
    @Test
    public void testMissionResultFileChooseLatest() throws PWCGException
    {        
        Mockito.when(aarHeaderParser.parseHeaderOnly(ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenReturn("Patrik Schorner 1941-10-02");
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Patrik Schorner 1941-10-02");
        
        List<String>logFileSets = new ArrayList<>();
        logFileSets.add("missionReport(2018-05-05_18-20-11)[0]");
        logFileSets.add("missionReport(2018-05-05_18-20-10)[0]");
        logFileSets.add("missionReport(2018-05-05_18-20-09)[0]");
        
        AARLogSetValidator logSetValidator = Mockito.spy(new AARLogSetValidator());
        Mockito.doNothing().when(logSetValidator).isLogSetValid(Mockito.any(), Mockito.any());

        AARMissionLogFileSet aarLogFileMissionFile = missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        assert (aarLogFileMissionFile.getLogFileName().equals("missionReport(2018-05-05_18-20-11)[0]"));
    }

    @Test(expected = PWCGException.class)
    public void testMissionResultFileMatchFail() throws PWCGException
    {
        Mockito.when(aarHeaderParser.parseHeaderOnly(ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenReturn("Patrik Schorner 1941-10-02");
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Wrong File");
        
        List<String>logFileSets = new ArrayList<>();
        logFileSets.add("missionReport(2018-05-05_18-20-11)[0]");
        
        missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
    }


    @Test(expected = PWCGException.class)
	public void testMissionResultFileMatchFailEmptyList() throws PWCGException
	{
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Patrik Schorner 1941-10-02");

        List<String>logFileSets = new ArrayList<>();
        missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
	}
}
