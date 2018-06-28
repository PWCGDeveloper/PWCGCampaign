package pwcg.campaign.squadmember;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCacheRoF;

@RunWith(MockitoJUnitRunner.class)
public class SquadronMemberFactoryTest
{
    @Mock 
    private Campaign campaign;
    private Date campaignDate;
    private SquadronPersonnel squadronPersonnel;
    private Squadron squadron;
    private SerialNumber serialNumber = new SerialNumber();
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        campaignDate = DateUtils.getDateYYYYMMDD("19170601");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.determineCountry()).thenReturn(CountryFactory.makeCountryByCode(101));
        Mockito.when(campaign.determineCountry()).thenReturn(CountryFactory.makeCountryByCode(101));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        
        squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(101003); 
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
        generatorModel.setPlayerName(CampaignCacheRoF.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);

        SquadronMemberFactory squadronMemberFactory = new  SquadronMemberFactory (campaign, squadron, squadronPersonnel);
        SquadronMember player = squadronMemberFactory.createPlayer(generatorModel);
        
        assert(player.isPlayer() == true);
        assert(player.getSerialNumber() == SerialNumber.PLAYER_SERIAL_NUMBER);
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
