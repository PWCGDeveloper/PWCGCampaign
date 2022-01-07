package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberFactory;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class CrewMemberStatusTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private CampaignAces campaignAces;

    private Date campaignDate;
    private CompanyPersonnel squadronPersonnel;
    private Company squadron;
    private SerialNumber serialNumber = new SerialNumber();
    
    private CrewMemberFactory squadronMemberFactory;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaignDate = DateUtils.getDateYYYYMMDD("19170801");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getCampaignAces()).thenReturn(campaignAces);
        List<TankAce> aces = new ArrayList<>();
        Mockito.when(campaignAces.getActiveCampaignAcesBySquadron(Mockito.anyInt())).thenReturn(aces);

        squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.ESC_103_PROFILE.getCompanyId()); 
        squadronPersonnel = new CompanyPersonnel(campaign, squadron);

        squadronMemberFactory = new  CrewMemberFactory (campaign, squadron, squadronPersonnel);
    }
    
    @Test
    public void testUpdateStatusActive() throws PWCGException
    {
        CrewMember crewMember = squadronMemberFactory.createInitialAICrewMember("Sergent");
        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, campaign.getDate(), null);
        assert(crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE);
        assert(crewMember.getRecoveryDate() == null);
        assert(crewMember.getInactiveDate() == null);
    }

    @Test
    public void testUpdateStatusKilled() throws PWCGException
    {
        CrewMember crewMember = squadronMemberFactory.createInitialAICrewMember("Sergent");
        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        assert(crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_KIA);
        assert(crewMember.getRecoveryDate() == null);
        assert(crewMember.getInactiveDate().equals(campaign.getDate()));
    }
    
    @Test
    public void testUpdateStatusWounded() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 21);
        CrewMember crewMember = squadronMemberFactory.createInitialAICrewMember("Sergent");
        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_WOUNDED, campaign.getDate(), returnDate);
        assert(crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_WOUNDED);
        assert(crewMember.getRecoveryDate().after(campaign.getDate()));
        assert(crewMember.getInactiveDate() == null);
    }
    
    @Test
    public void testUpdateStatusAiSeriousWound() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        CrewMember crewMember = squadronMemberFactory.createInitialAICrewMember("Sergent");
        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        assert(crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        assert(crewMember.getRecoveryDate() == null);
        assert(crewMember.getInactiveDate().equals(campaign.getDate()));
    }
    
    @Test
    public void testUpdateStatusPlayerWound() throws PWCGException
    {
        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setPlayerName(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        CrewMember player = squadronMemberFactory.createPlayer(generatorModel);
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        player.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_WOUNDED, campaign.getDate(), returnDate);
        assert(player.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_WOUNDED);
        assert(player.getRecoveryDate().after(campaign.getDate()));
        assert(player.getInactiveDate() == null);
    }

    @Test
    public void testUpdateStatusPlayerSeriousWound() throws PWCGException
    {
        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setPlayerName(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        CrewMember player = squadronMemberFactory.createPlayer(generatorModel);
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        player.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        assert(player.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        assert(player.getRecoveryDate().after(campaign.getDate()));
        assert(player.getInactiveDate() == null);
    }
}
