package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AARTestSetup;
import pwcg.aar.tabulate.debrief.MedalPanelEventTabulator;
import pwcg.aar.ui.display.model.AARMedalPanelData;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.medals.IMedalManager;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadron.Squadron;
import pwcg.product.rof.medals.FrenchMedalManager;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class MedalPanelEventTabulatorTest extends AARTestSetup
{
    @Mock
    private Squadron squadron1;
    
    @Mock
    private Squadron squadron2;

    @Mock 
    private ICountry country;

    protected IMedalManager medalManager;

    private Map<Integer, Map<String, Medal>> medalsAwarded = new HashMap<>();

    @Before
    public void setup() throws PWCGException
    {
        setupAARMocks();

        Mockito.when(squadron1.determineDisplayName(Mockito.any())).thenReturn("Esc 103");
        Mockito.when(squadron2.determineDisplayName(Mockito.any())).thenReturn("Esc 48");
        
        Mockito.when(country.isCountry(Country.FRANCE)).thenReturn(true);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);

        medalsAwarded.clear();
    }

    @Test
    public void testMedalsAwardedInMission() throws PWCGException 
    {
        Mockito.when(pilot1.getSquadronId()).thenReturn(101103);
        Mockito.when(pilot2.getSquadronId()).thenReturn(101103);
        Mockito.when(pilot1.determineSquadron()).thenReturn(squadron1);
        Mockito.when(pilot2.determineSquadron()).thenReturn(squadron1);

        medalsAwarded.put(pilot1.getSerialNumber(), new HashMap<String, Medal>());
        medalsAwarded.put(pilot2.getSerialNumber(), new HashMap<String, Medal>());
        Mockito.when(campaignMemberAwardsInMission.getCampaignMemberMedals()).thenReturn(medalsAwarded);

        Map<Integer, Medal> frenchMedals = medalManager.getMedals();
        Medal cdg = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE);
        Medal cdgBronzeStar = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_STAR);
        Medal cdgSilverPalm = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_PALM);
        medalsAwarded.get(pilot1.getSerialNumber()).put(cdg.getMedalName(), cdg);
        medalsAwarded.get(pilot1.getSerialNumber()).put(cdgBronzeStar.getMedalName(), cdgBronzeStar);
        medalsAwarded.get(pilot2.getSerialNumber()).put(cdgSilverPalm.getMedalName(), cdgSilverPalm);
       
        MedalPanelEventTabulator medalPanelEventTabulator = new MedalPanelEventTabulator(campaign, aarContext);
        AARMedalPanelData medalPanelData = medalPanelEventTabulator.tabulateForAARMedalPanel();
        
        assert(medalPanelData.getMedalsAwarded().size() == 3);
    }

    @Test
    public void testMedalsAwarded() throws PWCGException 
    {
        Mockito.when(pilot1.getSquadronId()).thenReturn(101103);
        Mockito.when(pilot2.getSquadronId()).thenReturn(101103);
        Mockito.when(pilot1.determineSquadron()).thenReturn(squadron1);
        Mockito.when(pilot2.determineSquadron()).thenReturn(squadron1);

        medalsAwarded.put(pilot1.getSerialNumber(), new HashMap<String, Medal>());
        medalsAwarded.put(pilot2.getSerialNumber(), new HashMap<String, Medal>());
        Mockito.when(campaignMemberAwardsOutOfMission.getCampaignMemberMedals()).thenReturn(medalsAwarded);

        Map<Integer, Medal> frenchMedals = medalManager.getMedals();
        Medal cdg = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE);
        Medal cdgBronzeStar = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_STAR);
        Medal cdgSilverPalm = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_PALM);
        medalsAwarded.get(pilot1.getSerialNumber()).put(cdg.getMedalName(), cdg);
        medalsAwarded.get(pilot1.getSerialNumber()).put(cdgBronzeStar.getMedalName(), cdgBronzeStar);
        medalsAwarded.get(pilot2.getSerialNumber()).put(cdgSilverPalm.getMedalName(), cdgSilverPalm);
       
        MedalPanelEventTabulator medalPanelEventTabulator = new MedalPanelEventTabulator(campaign, aarContext);
        AARMedalPanelData medalPanelData = medalPanelEventTabulator.tabulateForAARMedalPanel();
        
        assert(medalPanelData.getMedalsAwarded().size() == 3);
    }

    @Test
    public void testMedalsAwardedButOneIsNotInSquadron() throws PWCGException 
    {
        Mockito.when(pilot1.getSquadronId()).thenReturn(101103);
        Mockito.when(pilot2.getSquadronId()).thenReturn(101048);
        Mockito.when(pilot1.determineSquadron()).thenReturn(squadron1);
        Mockito.when(pilot2.determineSquadron()).thenReturn(squadron2);

        medalsAwarded.put(pilot1.getSerialNumber(), new HashMap<String, Medal>());
        medalsAwarded.put(pilot2.getSerialNumber(), new HashMap<String, Medal>());
        Mockito.when(campaignMemberAwardsOutOfMission.getCampaignMemberMedals()).thenReturn(medalsAwarded);

        FrenchMedalManager frenchMedalManager = new FrenchMedalManager(campaign);
        Medal cdg = frenchMedalManager.getMedal(FrenchMedalManager.CROIX_DE_GUERRE);
        Medal cdgBronzeStar = frenchMedalManager.getMedal(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_STAR);
        Medal cdgSilverPalm = frenchMedalManager.getMedal(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_PALM);
        medalsAwarded.get(pilot1.getSerialNumber()).put(cdg.getMedalName(), cdg);
        medalsAwarded.get(pilot1.getSerialNumber()).put(cdgBronzeStar.getMedalName(), cdgBronzeStar);
        medalsAwarded.get(pilot2.getSerialNumber()).put(cdgSilverPalm.getMedalName(), cdgSilverPalm);

        MedalPanelEventTabulator medalPanelEventTabulator = new MedalPanelEventTabulator(campaign, aarContext);
        AARMedalPanelData medalPanelData = medalPanelEventTabulator.tabulateForAARMedalPanel();
        
        assert(medalPanelData.getMedalsAwarded().size() == 3);
    }

}
