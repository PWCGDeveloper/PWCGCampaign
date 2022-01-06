package pwcg.aar.outofmission.phase2.resupply;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.outofmission.phase3.resupply.HistoricalAceTransferHandler;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.resupply.personnel.SquadronTransferData;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class HistoricalAceTransferHandlerTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private CampaignPersonnelManager campaignPersonnelManager;
    
    @Mock
    private AARPersonnelLosses lossesInMissionData;

    private CampaignAces campaignAces;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170430"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        
        campaignAces = PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(DateUtils.getDateYYYYMMDD("19170430")); 
        Mockito.when(campaignPersonnelManager.getCampaignAces()).thenReturn(campaignAces);
        
    }

    @Test
    public void testKarlSchaferTransferToJasta28() throws PWCGException
    {
        HistoricalAceTransferHandler historicalAceTransferHandler = new HistoricalAceTransferHandler(campaign, DateUtils.getDateYYYYMMDD("19170503"));
        SquadronTransferData acesTransferred =  historicalAceTransferHandler.determineAceTransfers();
        Assertions.assertTrue (acesTransferred.getCrewMembersTransferred().size() > 0);
        
        boolean karlSchaferFound = false;
        for (TransferRecord transferRecord : acesTransferred.getCrewMembersTransferred())
        {
            if (transferRecord.getCrewMember().getSerialNumber() == 101112)
            {
                karlSchaferFound = true;
            }
        }
        Assertions.assertTrue (karlSchaferFound); // Karl Schafer
    }
}
