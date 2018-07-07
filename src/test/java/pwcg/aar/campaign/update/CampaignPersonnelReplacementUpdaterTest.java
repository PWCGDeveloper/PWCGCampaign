package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.ww2.country.BoSServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class CampaignPersonnelReplacementUpdaterTest 
{
	@Mock
	Campaign campaign;
	
	@Mock
	AARContext aarContext;
	
	@Mock
    private CampaignPersonnelManager personnelManager;
	
	private List<PersonnelReplacementsService> replacementServices = new ArrayList<>();
	
	private SerialNumber serialNumber = new SerialNumber();
	
    @Before
    public void setup() throws PWCGException
    {
    	PWCGContextManager.setRoF(false);
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
    	
    	CampaignPersonnelReplacementUpdater replacementUpdater = new CampaignPersonnelReplacementUpdater(campaign, aarContext);
    	replacementUpdater.updateCampaignPersonnelReplacements();
    	
    	assert (replacementService.getReplacementPoints() == 30);
    	assert (replacementService.getReplacements().getActiveCount(campaign.getDate()) == 4);
    	assert (replacementService.getLastReplacementDate().equals(campaign.getDate()));
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
    	
    	CampaignPersonnelReplacementUpdater replacementUpdater = new CampaignPersonnelReplacementUpdater(campaign, aarContext);
    	replacementUpdater.updateCampaignPersonnelReplacements();
    	
    	assert (replacementService.getReplacementPoints() == 90);
    	assert (replacementService.getReplacements().getActiveCount(campaign.getDate()) == 4);
    	assert (replacementService.getLastReplacementDate().equals(campaign.getDate()));
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
    	
    	CampaignPersonnelReplacementUpdater replacementUpdater = new CampaignPersonnelReplacementUpdater(campaign, aarContext);
    	replacementUpdater.updateCampaignPersonnelReplacements();
    	
    	assert (replacementService.getReplacementPoints() == 130);
    	assert (replacementService.getReplacements().getActiveCount(campaign.getDate()) == 0);
    	assert (replacementService.getLastReplacementDate().equals(DateUtils.removeTimeDays(campaign.getDate(), 6)));
    }
}
