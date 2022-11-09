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
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.CampaignPilotGeneratorModel;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class SquadronMemberFactoryTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private CampaignAces campaignAces;
    
    private Date campaignDate;
    private SquadronPersonnel squadronPersonnel;
    private Squadron squadron;
    private SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
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
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignPilotGeneratorModel pilotModel = new CampaignPilotGeneratorModel();
        pilotModel.setPlayerName("Johnny Player");
        pilotModel.setPlayerRank(rankName);
        pilotModel.setPlayerRegion("");
        pilotModel.setService(service);

        SquadronMemberFactory squadronMemberFactory = new  SquadronMemberFactory (campaign, squadron, squadronPersonnel);
        SquadronMember player = squadronMemberFactory.createPlayer(pilotModel);
        
        assert(player.isPlayer() == true);
        assert(player.getSerialNumber() >= SerialNumber.PLAYER_STARTING_SERIAL_NUMBER && player.getSerialNumber() < SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(player.getName().equals(pilotModel.getPlayerName()));
        assert(player.getPicName() != null && !player.getPicName().isEmpty());
        assert(player.getPlayerRegion().equals(pilotModel.getPlayerRegion()));
        assert(player.getRank().equals(pilotModel.getPlayerRank()));
        assert(player.getInactiveDate().equals(DateUtils.getEndOfWar()));
    }

    @Test
    public void testCreateAiPilot() throws PWCGException
    {
        SquadronMemberFactory squadronMemberFactory = new  SquadronMemberFactory (campaign, squadron, squadronPersonnel);
        SquadronMember pilot = squadronMemberFactory.createInitialAIPilot("Sergent");
        
        assert(pilot.isPlayer() == false);
        assert(pilot.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
        assert(pilot.getName() != null && !pilot.getName().isEmpty());
        assert(pilot.getPicName() != null && !pilot.getPicName().isEmpty());
        assert(pilot.getRank().equals("Sergent"));
        assert(pilot.getInactiveDate().equals(DateUtils.getEndOfWar()));
    }
}
