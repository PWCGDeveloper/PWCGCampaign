package pwcg.aar.campaign.update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.VictoryMaker;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignAceUpdaterTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @Test
    public void testInSquadronAceUpdate() throws PWCGException 
    {

        Map<Integer, List<Victory>> aceVictories = new HashMap<>();
        List<Victory> victories = VictoryMaker.makeMultipleAlliedVictories(1, campaign.getDate());
        victories.get(0).getVictor().setCrewMemberName("Georges Guynemer");
        aceVictories.put(101064, victories);
        
        TankAce aceInCampaign = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101064);   
        int aceVictoriesBefore = aceInCampaign.getCrewMemberVictories().getAirToAirVictoryCount();
        
        CampaignAceUpdater updater = new CampaignAceUpdater(campaign, aceVictories);
        updater.updatesCampaignAces();

        int aceVictoriesAfter = aceInCampaign.getCrewMemberVictories().getAirToAirVictoryCount();
        
        Assertions.assertTrue (aceVictoriesAfter == (aceVictoriesBefore+1));
    }
    
    
    @Test
    public void testOutOfSquadronAceUpdate() throws PWCGException 
    {
        Map<Integer, List<Victory>> aceVictories = new HashMap<>();
        List<Victory> victories = VictoryMaker.makeMultipleCentralVictories(1, campaign.getDate());
        victories.get(0).getVictor().setCrewMemberName("Paul Baumer");
        aceVictories.put(101143, victories);
        
        TankAce aceInCampaign = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(101143);   
        int aceVictoriesBefore = aceInCampaign.getCrewMemberVictories().getAirToAirVictoryCount();
        
        CampaignAceUpdater updater = new CampaignAceUpdater(campaign, aceVictories);
        updater.updatesCampaignAces();

        int aceVictoriesAfter = aceInCampaign.getCrewMemberVictories().getAirToAirVictoryCount();
        
        Assertions.assertTrue (aceVictoriesAfter == (aceVictoriesBefore+1));
    }
}
