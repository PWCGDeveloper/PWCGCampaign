package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.BoSServiceManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignPersonnelReplacementUpdaterTest 
{
	@Mock
	private Campaign campaign;
	
	@Mock
	private AARContext aarContext;
	
	@Mock
    private CampaignPersonnelManager personnelManager;
	
	private List<PersonnelReplacementsService> replacementServices = new ArrayList<>();
	
	private SerialNumber serialNumber = new SerialNumber();
	
    @BeforeEach
    public void setupTest() throws PWCGException
    {
    	PWCGContext.setProduct(PWCGProduct.BOS);
    	replacementServices.clear();
    	
    	Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
    	Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
    	Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
    	Mockito.when(personnelManager.getAllPersonnelReplacements()).thenReturn(replacementServices);
    	Mockito.when(aarContext.getNewDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420804"));
    }
    
    
    @Test
    public void testUpdateWithReplacementsGermans() throws PWCGException 
    {
    	PersonnelReplacementsService replacementService = new PersonnelReplacementsService();
    	replacementService.setReplacementPoints(40);
    	replacementService.setDailyReplacementRate(10);
    	replacementService.setLastReplacementDate(DateUtils.removeTimeDays(campaign.getDate(), 8));
    	replacementService.setServiceId(BoSServiceManager.LUFTWAFFE);
    	
    	replacementServices.add(replacementService);
    	
    	PersonnelReplacementUpdater replacementUpdater = new PersonnelReplacementUpdater(campaign, aarContext);
    	replacementUpdater.updateCampaignPersonnelReplacements();
    	
    	Assertions.assertTrue (replacementService.getReplacementPoints() == 30);
    	Assertions.assertTrue (replacementService.getReplacements().getActiveCount(campaign.getDate()) == 4);
    	Assertions.assertTrue (replacementService.getLastReplacementDate().equals(campaign.getDate()));
    }
    
    @Test
    public void testUpdateWithReplacementsRussians() throws PWCGException 
    {
    	PersonnelReplacementsService replacementService = new PersonnelReplacementsService();
    	replacementService.setReplacementPoints(40);
    	replacementService.setDailyReplacementRate(30);
    	replacementService.setLastReplacementDate(DateUtils.removeTimeDays(campaign.getDate(), 8));
    	replacementService.setServiceId(BoSServiceManager.VVS);
    	
    	replacementServices.add(replacementService);
    	
    	PersonnelReplacementUpdater replacementUpdater = new PersonnelReplacementUpdater(campaign, aarContext);
    	replacementUpdater.updateCampaignPersonnelReplacements();
    	
    	Assertions.assertTrue (replacementService.getReplacementPoints() == 90);
    	Assertions.assertTrue (replacementService.getReplacements().getActiveCount(campaign.getDate()) == 4);
    	Assertions.assertTrue (replacementService.getLastReplacementDate().equals(campaign.getDate()));
    }
    
    @Test
    public void testNotTimeToReplace() throws PWCGException 
    {
    	PersonnelReplacementsService replacementService = new PersonnelReplacementsService();
    	replacementService.setReplacementPoints(40);
    	replacementService.setDailyReplacementRate(30);
    	replacementService.setLastReplacementDate(DateUtils.removeTimeDays(campaign.getDate(), 6));
    	replacementService.setServiceId(BoSServiceManager.VVS);
    	
    	replacementServices.add(replacementService);
    	
    	PersonnelReplacementUpdater replacementUpdater = new PersonnelReplacementUpdater(campaign, aarContext);
    	replacementUpdater.updateCampaignPersonnelReplacements();
    	
    	Assertions.assertTrue (replacementService.getReplacementPoints() == 130);
    	Assertions.assertTrue (replacementService.getReplacements().getActiveCount(campaign.getDate()) == 0);
    	Assertions.assertTrue (replacementService.getLastReplacementDate().equals(DateUtils.removeTimeDays(campaign.getDate(), 6)));
    }
}
