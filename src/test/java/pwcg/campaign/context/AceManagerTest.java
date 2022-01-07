package pwcg.campaign.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.HistoricalAce;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AceManagerTest
{
	@Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private CompanyPersonnel squadronPersonnel;
	@Mock private TankAce wernerVoss;
    @Mock private TankAce georgesGuynemer;
    @Mock private TankAce renefonck;

	private AceManager aceManager;
	private CampaignAces campaignAces;

	
    @BeforeEach
    public void setupTest() throws PWCGException
    {
    	campaignAces = new CampaignAces();
    	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170501"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getCampaignAces()).thenReturn(campaignAces);
        Mockito.when(personnelManager.getCompanyPersonnel(Mockito.anyInt())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.isPlayerSquadron()).thenReturn(false);
    	
    	Mockito.when(wernerVoss.getSerialNumber()).thenReturn(101175);
    	Mockito.when(wernerVoss.getCompanyId()).thenReturn(401010);
    	Mockito.when(wernerVoss.getName()).thenReturn("Werner Voss");
    	Mockito.when(wernerVoss.getCrewMemberActiveStatus()).thenReturn(CrewMemberStatus.STATUS_ACTIVE);
    	Mockito.when(wernerVoss.copy()).thenReturn(wernerVoss);

        Mockito.when(georgesGuynemer.getCompanyId()).thenReturn(301003);
        Mockito.when(georgesGuynemer.getSerialNumber()).thenReturn(101064);
        Mockito.when(georgesGuynemer.getCrewMemberActiveStatus()).thenReturn(CrewMemberStatus.STATUS_ACTIVE);
        Mockito.when(georgesGuynemer.copy()).thenReturn(georgesGuynemer);

        Mockito.when(renefonck.getCompanyId()).thenReturn(301103);
        Mockito.when(renefonck.getSerialNumber()).thenReturn(101154);
        Mockito.when(renefonck.getCrewMemberActiveStatus()).thenReturn(CrewMemberStatus.STATUS_ACTIVE);
        Mockito.when(renefonck.copy()).thenReturn(renefonck);

    	Map<Integer, TankAce> acesInCampaign = new HashMap<>();
    	acesInCampaign.put(wernerVoss.getSerialNumber(), wernerVoss);
        acesInCampaign.put(georgesGuynemer.getSerialNumber(), georgesGuynemer);
        acesInCampaign.put(renefonck.getSerialNumber(), renefonck);
    	campaignAces.setCampaignAces(acesInCampaign);


        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);
    	aceManager = new AceManager();
     	aceManager.configure();
    }

    @Test
    public void testAcesKilled () throws PWCGException
    {            	
    	List<TankAce> aces = aceManager.acesKilledHistoricallyInTimePeriod(DateUtils.getDateYYYYMMDD("19170801"), DateUtils.getDateYYYYMMDD("19171001"));
    	Assertions.assertTrue (aces.size() > 0);
    }

    @Test
    public void testAceByName () throws PWCGException
    {            	
    	TankAce ace = aceManager.getAceWithCampaignAdjustment(campaign, campaign.getPersonnelManager().getCampaignAces(), 101175, campaign.getDate());
    	Assertions.assertTrue (ace.getName().equals("Werner Voss"));
    }

    @Test
    public void testSquadronsCommandedByAce () throws PWCGException
    {            	
    	Set<Integer> squadronsCommandedByAce = aceManager.getAceCommandedSquadrons();
    	Assertions.assertTrue (squadronsCommandedByAce.size() > 0);
    }

    @Test
    public void testActiveAcesForSquadron () throws PWCGException
    {            	
    	Mockito.when(georgesGuynemer.getCrewMemberActiveStatus()).thenReturn(CrewMemberStatus.STATUS_ACTIVE);
    	List<TankAce> aces = aceManager.getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), DateUtils.getDateYYYYMMDD("19170801"), SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
    	Assertions.assertTrue (aces.size() > 0);
    }

    @Test
    public void testActiveAcesForSquadronAceOnLeave () throws PWCGException
    {            	
    	Mockito.when(georgesGuynemer.getCrewMemberActiveStatus()).thenReturn(CrewMemberStatus.STATUS_ACTIVE);
    	List<TankAce> aces = aceManager.getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), DateUtils.getDateYYYYMMDD("19160318"), SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
    	Assertions.assertTrue (aces.size() == 0);
    }

    @Test
    public void testActiveAcesForSquadronButAceIsDead () throws PWCGException
    {            	
    	Mockito.when(georgesGuynemer.getCrewMemberActiveStatus()).thenReturn(CrewMemberStatus.STATUS_KIA);
    	List<TankAce> aces = aceManager.getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), DateUtils.getDateYYYYMMDD("19171001"), SquadronTestProfile.ESC_3_PROFILE.getCompanyId());
    	Assertions.assertTrue (aces.size() == 0);
    }

    @Test
    public void testAllAcesForSquadron () throws PWCGException
    {            	
    	List<TankAce> aces = aceManager.getAllAcesForSquadron(campaign.getPersonnelManager().getCampaignAces().getAllCampaignAces().values(), SquadronTestProfile.ESC_3_PROFILE.getCompanyId());
    	Assertions.assertTrue (aces.size() == 1);
    }

    @Test
    public void testHistoricalAceByName () throws PWCGException
    {            	
    	HistoricalAce ace = aceManager.getHistoricalAceBySerialNumber(101175);
    	Assertions.assertTrue (ace.getName().equals("Werner Voss"));
    }

    @Test
    public void testHistoricalAces () throws PWCGException
    {            	
    	List<HistoricalAce> aces = aceManager.getHistoricalAces();
    	Assertions.assertTrue (aces.size() > 0);
    }

}
