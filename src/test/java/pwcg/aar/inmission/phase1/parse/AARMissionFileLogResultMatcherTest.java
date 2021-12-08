package pwcg.aar.inmission.phase1.parse;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogFileSet;
import pwcg.core.logfiles.LogFileHeaderParser;
import pwcg.mission.data.MissionHeader;

@ExtendWith(MockitoExtension.class)
public class AARMissionFileLogResultMatcherTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignData campaignData;
    @Mock private PwcgMissionData pwcgMissionData;       
    @Mock private MissionHeader missionHeader;
    @Mock private LogFileHeaderParser aarHeaderParser;        
    
    @InjectMocks
    AARMissionFileLogResultMatcher missionFileLogResultMatcher;
    
    public AARMissionFileLogResultMatcherTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void testMissionResultFileMatch() throws PWCGException
    {        
        Mockito.when(aarHeaderParser.parseHeaderOnly(ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenReturn("Patrik Schorner 1941-10-02");
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Patrik Schorner 1941-10-02");
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);
        Mockito.when(campaignData.getName()).thenReturn("Patrik Schorner");

        List<String>logFileSets = new ArrayList<>();
        logFileSets.add("missionReport(2018-05-05_18-20-11)[0]");
        
        LogFileSet aarLogFileMissionFile = missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        Assertions.assertTrue (aarLogFileMissionFile.getLogFileName().equals("missionReport(2018-05-05_18-20-11)[0]"));
    }
    
    @Test
    public void testMissionResultFileChooseLatest() throws PWCGException
    {        
        Mockito.when(aarHeaderParser.parseHeaderOnly(ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenReturn("Patrik Schorner 1941-10-02");
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMissionFileName()).thenReturn("Patrik Schorner 1941-10-02");
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);
        Mockito.when(campaignData.getName()).thenReturn("Patrik Schorner");
        
        List<String>logFileSets = new ArrayList<>();
        logFileSets.add("missionReport(2018-05-05_18-20-11)[0]");
        logFileSets.add("missionReport(2018-05-05_18-20-10)[0]");
        logFileSets.add("missionReport(2018-05-05_18-20-09)[0]");
        
        LogFileSet aarLogFileMissionFile = missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        Assertions.assertTrue (aarLogFileMissionFile.getLogFileName().equals("missionReport(2018-05-05_18-20-11)[0]"));
    }

    @Test
    public void testMissionResultFileMatchFail() throws PWCGException
    {
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);
        Mockito.when(campaignData.getName()).thenReturn("Patrik Schorner");

        assertThrows(PWCGException.class, () -> 
        {
            Mockito.when(aarHeaderParser.parseHeaderOnly(ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenReturn("Patrik Schorner 1941-10-02");
            Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
            Mockito.when(missionHeader.getMissionFileName()).thenReturn("Wrong File");
            
            List<String>logFileSets = new ArrayList<>();
            logFileSets.add("missionReport(2018-05-05_18-20-11)[0]");
            
            missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        });
    }

    @Test
	public void testMissionResultFileMatchFailEmptyList() throws PWCGException
	{
        assertThrows(PWCGException.class, () -> 
        {
            Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
            Mockito.when(missionHeader.getMissionFileName()).thenReturn("Patrik Schorner 1941-10-02");
    
            List<String>logFileSets = new ArrayList<>();
            missionFileLogResultMatcher.matchMissionFileAndLogFile(pwcgMissionData, logFileSets);
        });
	}
}
