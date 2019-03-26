package pwcg.campaign.io.json;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CombatReportIOJsonTest
{
    private Campaign campaign;
    private CombatReport combatReport = new CombatReport();

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
        CampaignIOJson.writeJson(campaign);
    }

    @Test
    public void combatReportJsonTest() throws PWCGException
    {
        combatReport.setDate(campaign.getDate());
        writeCombatReport();
        readCombatReport();
    }
    
    public void writeCombatReport() throws PWCGException
    {
        CombatReportIOJson.writeJson(campaign, combatReport);
    }
    
    public void readCombatReport() throws PWCGException
    {
        Map<String, CombatReport> combatReports = CombatReportIOJson.readJson(campaign);
        assert (combatReports.size() > 0);
    }

}
