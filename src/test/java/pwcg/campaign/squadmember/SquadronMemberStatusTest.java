package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCacheRoF;

@RunWith(MockitoJUnitRunner.class)
public class SquadronMemberStatusTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private CampaignAces campaignAces;

    private Date campaignDate;
    private SquadronPersonnel squadronPersonnel;
    private Squadron squadron;
    private SerialNumber serialNumber = new SerialNumber();
    
    private SquadronMemberFactory squadronMemberFactory;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        campaignDate = DateUtils.getDateYYYYMMDD("19170601");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getCampaignAces()).thenReturn(campaignAces);
        List<Ace> aces = new ArrayList<>();
        Mockito.when(campaignAces.getCampaignAcesBySquadron(Mockito.anyInt())).thenReturn(aces);

        squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(101003); 
        squadronPersonnel = new SquadronPersonnel(campaign, squadron);

        squadronMemberFactory = new  SquadronMemberFactory (campaign, squadron, squadronPersonnel);
    }
    
    @Test
    public void testUpdateStatusActive() throws PWCGException
    {
        SquadronMember pilot = squadronMemberFactory.createAIPilot("Sergent");
        pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, campaign.getDate(), null);
        assert(pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE);
        assert(pilot.getRecoveryDate() == null);
        assert(pilot.getInactiveDate() == null);
    }

    @Test
    public void testUpdateStatusKilled() throws PWCGException
    {
        SquadronMember pilot = squadronMemberFactory.createAIPilot("Sergent");
        pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        assert(pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_KIA);
        assert(pilot.getRecoveryDate() == null);
        assert(pilot.getInactiveDate().equals(campaign.getDate()));
    }
    
    @Test
    public void testUpdateStatusWounded() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 21);
        SquadronMember pilot = squadronMemberFactory.createAIPilot("Sergent");
        pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_WOUNDED, campaign.getDate(), returnDate);
        assert(pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_WOUNDED);
        assert(pilot.getRecoveryDate().after(campaign.getDate()));
        assert(pilot.getInactiveDate() == null);
    }
    
    @Test
    public void testUpdateStatusAiSeriousWound() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        SquadronMember pilot = squadronMemberFactory.createAIPilot("Sergent");
        pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        assert(pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        assert(pilot.getRecoveryDate() == null);
        assert(pilot.getInactiveDate().equals(campaign.getDate()));
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
        generatorModel.setPlayerName(CampaignCacheRoF.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        SquadronMember player = squadronMemberFactory.createPlayer(generatorModel);
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        player.setPilotActiveStatus(SquadronMemberStatus.STATUS_WOUNDED, campaign.getDate(), returnDate);
        assert(player.getPilotActiveStatus() == SquadronMemberStatus.STATUS_WOUNDED);
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
        generatorModel.setPlayerName(CampaignCacheRoF.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        SquadronMember player = squadronMemberFactory.createPlayer(generatorModel);
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        player.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        assert(player.getPilotActiveStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        assert(player.getRecoveryDate().after(campaign.getDate()));
        assert(player.getInactiveDate() == null);
    }
}
