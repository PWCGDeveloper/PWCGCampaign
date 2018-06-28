package pwcg.campaign.io.json;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class CombatReportIOJsonTest
{
    @Mock
    Campaign campaign;
    
    @Test
    public void readJsonTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        Mockito.when(campaign.getCampaignPath()).thenReturn("E:\\PWCG\\workspacePWCGGradle\\PWCGCampaign\\Campaigns\\Patrik Schorner\\");
        Map<String, CombatReport> combatReports = CombatReportIOJson.readJson(campaign);
        assert (combatReports.size() > 0);
    }
    
    @Test
    public void readJsonFailedTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        Mockito.when(campaign.getCampaignPath()).thenReturn("E:\\PWCG\\workspacePWCGGradle\\PWCGCampaign\\Campaigns\\Foo\\");
        Map<String, CombatReport> combatReports = CombatReportIOJson.readJson(campaign);
        assert (combatReports.size() == 0);
    }

}
