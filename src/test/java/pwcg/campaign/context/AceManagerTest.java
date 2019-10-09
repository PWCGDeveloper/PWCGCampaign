package pwcg.campaign.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class AceManagerTest
{
	@Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private SquadronPersonnel squadronPersonnel;
	@Mock private Ace wernerVoss;
	@Mock private Ace georgesGuynemer;

	private AceManager aceManager;
	private CampaignAces campaignAces;

	
    @Before
    public void setup() throws PWCGException
    {
    	campaignAces = new CampaignAces();
    	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170501"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getCampaignAces()).thenReturn(campaignAces);
        Mockito.when(personnelManager.getSquadronPersonnel(Mockito.anyInt())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.isPlayerSquadron()).thenReturn(false);
    	
    	Mockito.when(wernerVoss.getSerialNumber()).thenReturn(101175);
    	Mockito.when(wernerVoss.getSquadronId()).thenReturn(501010);
    	Mockito.when(wernerVoss.getName()).thenReturn("Werner Voss");
    	Mockito.when(wernerVoss.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);
    	Mockito.when(wernerVoss.copy()).thenReturn(wernerVoss);

    	Mockito.when(georgesGuynemer.getSquadronId()).thenReturn(101003);
    	Mockito.when(georgesGuynemer.getSerialNumber()).thenReturn(101064);
    	Mockito.when(georgesGuynemer.getName()).thenReturn("Georges Guynemer");
    	Mockito.when(georgesGuynemer.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);
    	Mockito.when(georgesGuynemer.copy()).thenReturn(georgesGuynemer);

    	Map<Integer, Ace> acesInCampaign = new HashMap<>();
    	acesInCampaign.put(wernerVoss.getSerialNumber(), wernerVoss);
    	acesInCampaign.put(georgesGuynemer.getSerialNumber(), georgesGuynemer);
    	campaignAces.setCampaignAces(acesInCampaign);


        PWCGContext.setProduct(PWCGProduct.ROF);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);
    	aceManager = new AceManager();
     	aceManager.configure();
    }

    @Test
    public void testAcesKilled () throws PWCGException
    {            	
    	List<Ace> aces = aceManager.acesKilledHistoricallyInTimePeriod(DateUtils.getDateYYYYMMDD("19170801"), DateUtils.getDateYYYYMMDD("19171001"));
    	assert (aces.size() > 0);
    }

    @Test
    public void testAceByName () throws PWCGException
    {            	
    	Ace ace = aceManager.getAceWithCampaignAdjustment(campaign, campaign.getPersonnelManager().getCampaignAces(), 101175, campaign.getDate());
    	assert (ace.getName().equals("Werner Voss"));
    }

    @Test
    public void testSquadronsCommandedByAce () throws PWCGException
    {            	
    	Set<Integer> squadronsCommandedByAce = aceManager.getAceCommandedSquadrons();
    	assert (squadronsCommandedByAce.size() > 0);
    }

    @Test
    public void testActiveAcesForSquadron () throws PWCGException
    {            	
    	Mockito.when(georgesGuynemer.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);
    	List<Ace> aces = aceManager.getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), DateUtils.getDateYYYYMMDD("19170701"), 101003);
    	assert (aces.size() > 0);
    }

    @Test
    public void testActiveAcesForSquadronAceOnLeave () throws PWCGException
    {            	
    	Mockito.when(georgesGuynemer.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);
    	List<Ace> aces = aceManager.getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), DateUtils.getDateYYYYMMDD("19160318"), 101003);
    	assert (aces.size() == 0);
    }

    @Test
    public void testActiveAcesForSquadronButAceIsDead () throws PWCGException
    {            	
    	Mockito.when(georgesGuynemer.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_KIA);
    	List<Ace> aces = aceManager.getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), DateUtils.getDateYYYYMMDD("19171001"), 101003);
    	assert (aces.size() == 0);
    }

    @Test
    public void testAllAcesForSquadron () throws PWCGException
    {            	
    	List<Ace> aces = aceManager.getAllAcesForSquadron(campaign.getPersonnelManager().getCampaignAces().getCampaignAces().values(), 101003);
    	assert (aces.size() == 1);
    }

    @Test
    public void testHistoricalAceByName () throws PWCGException
    {            	
    	HistoricalAce ace = aceManager.getHistoricalAceBySerialNumber(101175);
    	assert (ace.getName().equals("Werner Voss"));
    }

    @Test
    public void testHistoricalAces () throws PWCGException
    {            	
    	List<HistoricalAce> aces = aceManager.getHistoricalAces();
    	assert (aces.size() > 0);
    }

}
