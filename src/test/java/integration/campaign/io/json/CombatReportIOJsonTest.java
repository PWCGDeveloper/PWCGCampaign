package integration.campaign.io.json;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CombatReportIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CombatReportIOJsonTest
{
    private Campaign campaign;    
    private CombatReport combatReport = new CombatReport();

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignOnDisk(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void combatReportJsonTest() throws PWCGException
    {
    	combatReport.setCrewMemberSerialNumber(12345);
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
        Map<String, CombatReport> combatReports1 = CombatReportIOJson.readJson(campaign, 12345);
        Assertions.assertTrue (combatReports1.size() > 0);
    }

}
