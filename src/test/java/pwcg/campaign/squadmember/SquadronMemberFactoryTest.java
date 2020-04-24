package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class SquadronMemberFactoryTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private CampaignAces campaignAces;
    
    private Date campaignDate;
    private SquadronPersonnel squadronPersonnel;
    private Squadron squadron;
    private SerialNumber serialNumber = new SerialNumber();
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaignDate = DateUtils.getDateYYYYMMDD("19170801");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getCampaignAces()).thenReturn(campaignAces);
        List<Ace> aces = new ArrayList<>();
        Mockito.when(campaignAces.getActiveCampaignAcesBySquadron(Mockito.anyInt())).thenReturn(aces);
        
        squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.ESC_3_PROFILE.getSquadronId()); 
        squadronPersonnel = new SquadronPersonnel(campaign, squadron);
    }

    @Test
    public void testCreatePlayer() throws PWCGException
    {                
        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setCampaignName(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerName(CampaignCacheBase.TEST_PLAYER_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);

        SquadronMemberFactory squadronMemberFactory = new  SquadronMemberFactory (campaign, squadron, squadronPersonnel);
        SquadronMember player = squadronMemberFactory.createPlayer(generatorModel);
        
        assert(player.isPlayer() == true);
        assert(player.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER && player.getSerialNumber() < SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(player.getName().equals(generatorModel.getPlayerName()));
        assert(player.getPicName() != null && !player.getPicName().isEmpty());
        assert(player.getPlayerRegion().equals(generatorModel.getPlayerRegion()));
        assert(player.getRank().equals(generatorModel.getPlayerRank()));
        assert(player.getInactiveDate().equals(DateUtils.getEndOfWar()));
    }

    @Test
    public void testCreateAiPilot() throws PWCGException
    {
        SquadronMemberFactory squadronMemberFactory = new  SquadronMemberFactory (campaign, squadron, squadronPersonnel);
        SquadronMember pilot = squadronMemberFactory.createAIPilot("Sergent");
        
        assert(pilot.isPlayer() == false);
        assert(pilot.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(pilot.getName() != null && !pilot.getName().isEmpty());
        assert(pilot.getPicName() != null && !pilot.getPicName().isEmpty());
        assert(pilot.getRank().equals("Sergent"));
        assert(pilot.getInactiveDate().equals(DateUtils.getEndOfWar()));
    }
}
