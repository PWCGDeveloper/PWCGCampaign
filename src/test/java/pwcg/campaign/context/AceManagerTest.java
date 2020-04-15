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
import org.mockito.junit.MockitoJUnitRunner;

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
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AceManagerTest
{
	@Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private SquadronPersonnel squadronPersonnel;
	@Mock private Ace wernerVoss;
    @Mock private Ace georgesGuynemer;
    @Mock private Ace renefonck;

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
    	Mockito.when(wernerVoss.getSquadronId()).thenReturn(401010);
    	Mockito.when(wernerVoss.getName()).thenReturn("Werner Voss");
    	Mockito.when(wernerVoss.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);
    	Mockito.when(wernerVoss.copy()).thenReturn(wernerVoss);

        Mockito.when(georgesGuynemer.getSquadronId()).thenReturn(301003);
        Mockito.when(georgesGuynemer.getSerialNumber()).thenReturn(101064);
        Mockito.when(georgesGuynemer.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);
        Mockito.when(georgesGuynemer.copy()).thenReturn(georgesGuynemer);

        Mockito.when(renefonck.getSquadronId()).thenReturn(301103);
        Mockito.when(renefonck.getSerialNumber()).thenReturn(101154);
        Mockito.when(renefonck.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);
        Mockito.when(renefonck.copy()).thenReturn(renefonck);

    	Map<Integer, Ace> acesInCampaign = new HashMap<>();
    	acesInCampaign.put(wernerVoss.getSerialNumber(), wernerVoss);
        acesInCampaign.put(georgesGuynemer.getSerialNumber(), georgesGuynemer);
        acesInCampaign.put(renefonck.getSerialNumber(), renefonck);
    	campaignAces.setCampaignAces(acesInCampaign);


        PWCGContext.setProduct(PWCGProduct.FC);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);
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
    	List<Ace> aces = aceManager.getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), DateUtils.getDateYYYYMMDD("19170801"), SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
    	assert (aces.size() > 0);
    }

    @Test
    public void testActiveAcesForSquadronAceOnLeave () throws PWCGException
    {            	
    	Mockito.when(georgesGuynemer.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);
    	List<Ace> aces = aceManager.getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), DateUtils.getDateYYYYMMDD("19160318"), SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
    	assert (aces.size() == 0);
    }

    @Test
    public void testActiveAcesForSquadronButAceIsDead () throws PWCGException
    {            	
    	Mockito.when(georgesGuynemer.getPilotActiveStatus()).thenReturn(SquadronMemberStatus.STATUS_KIA);
    	List<Ace> aces = aceManager.getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), DateUtils.getDateYYYYMMDD("19171001"), SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
    	assert (aces.size() == 0);
    }

    @Test
    public void testAllAcesForSquadron () throws PWCGException
    {            	
    	List<Ace> aces = aceManager.getAllAcesForSquadron(campaign.getPersonnelManager().getCampaignAces().getCampaignAces().values(), SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
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
