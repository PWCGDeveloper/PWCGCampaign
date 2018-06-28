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
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.ww1.medals.FrenchMedalManager;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class MedalPanelEventTabulatorTest extends AARTestSetup
{
    @Mock
    private Squadron squadron1;
    
    @Mock
    private Squadron squadron2;

    private Map<Integer, Map<String, Medal>> medalsAwarded = new HashMap<>();
    
    @Before
    public void setup() throws PWCGException
    {
        setupAARMocks();

        Mockito.when(campaignMemberAwards.getCampaignMemberMedals()).thenReturn(medalsAwarded);

        Mockito.when(squadron1.determineDisplayName(Mockito.any())).thenReturn("Esc 103");
        Mockito.when(squadron2.determineDisplayName(Mockito.any())).thenReturn("Esc 48");

        medalsAwarded.clear();
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
        
        Map<Integer, Medal> frenchMedals = FrenchMedalManager.getMedals();
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
        
        FrenchMedalManager frenchMedalManager = new FrenchMedalManager(campaign);
        Medal cdg = frenchMedalManager.getMedal(FrenchMedalManager.CROIX_DE_GUERRE);
        Medal cdgBronzeStar = frenchMedalManager.getMedal(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_STAR);
        Medal cdgSilverPalm = frenchMedalManager.getMedal(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_PALM);
        medalsAwarded.get(pilot1.getSerialNumber()).put(cdg.getMedalName(), cdg);
        medalsAwarded.get(pilot1.getSerialNumber()).put(cdgBronzeStar.getMedalName(), cdgBronzeStar);
        medalsAwarded.get(pilot2.getSerialNumber()).put(cdgSilverPalm.getMedalName(), cdgSilverPalm);

        MedalPanelEventTabulator medalPanelEventTabulator = new MedalPanelEventTabulator(campaign, aarContext);
        AARMedalPanelData medalPanelData = medalPanelEventTabulator.tabulateForAARMedalPanel();
        
        assert(medalPanelData.getMedalsAwarded().size() == 2);
    }

}
