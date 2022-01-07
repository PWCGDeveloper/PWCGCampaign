package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.tabulate.debrief.MedalPanelEventTabulator;
import pwcg.aar.ui.display.model.AARMedalPanelData;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.medals.IMedalManager;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.medals.FrenchMedalManager;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MedalPanelEventTabulatorTest extends AARTestSetup
{
    @Mock
    private Company squadron1;
    
    @Mock
    private Company squadron2;

    @Mock 
    private ICountry country;

    protected IMedalManager medalManager;

    private Map<Integer, Map<String, Medal>> medalsAwarded = new HashMap<>();

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        setupAARMocks();

        Mockito.when(squadron1.determineDisplayName(Mockito.any())).thenReturn("Esc 103");
        Mockito.when(squadron2.determineDisplayName(Mockito.any())).thenReturn("Esc 3");
        
        Mockito.when(country.isCountry(Country.FRANCE)).thenReturn(true);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);

        medalsAwarded.clear();
    }

    @Test
    public void testMedalsAwardedInMission() throws PWCGException 
    {
        Mockito.when(crewMember1.getCompanyId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        Mockito.when(crewMember2.getCompanyId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        Mockito.when(crewMember1.determineSquadron()).thenReturn(squadron1);
        Mockito.when(crewMember2.determineSquadron()).thenReturn(squadron1);

        medalsAwarded.put(crewMember1.getSerialNumber(), new HashMap<String, Medal>());
        medalsAwarded.put(crewMember2.getSerialNumber(), new HashMap<String, Medal>());
        Mockito.when(personnelAwards.getCampaignMemberMedals()).thenReturn(medalsAwarded);

        Map<Integer, Medal> frenchMedals = medalManager.getMedals();
        Medal cdg = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE);
        Medal cdgBronzeStar = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_STAR);
        Medal cdgSilverPalm = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_PALM);
        medalsAwarded.get(crewMember1.getSerialNumber()).put(cdg.getMedalName(), cdg);
        medalsAwarded.get(crewMember1.getSerialNumber()).put(cdgBronzeStar.getMedalName(), cdgBronzeStar);
        medalsAwarded.get(crewMember2.getSerialNumber()).put(cdgSilverPalm.getMedalName(), cdgSilverPalm);
       
        MedalPanelEventTabulator medalPanelEventTabulator = new MedalPanelEventTabulator(campaign, aarContext);
        AARMedalPanelData medalPanelData = medalPanelEventTabulator.tabulateForAARMedalPanel();
        
        assert(medalPanelData.getMedalsAwarded().size() == 3);
    }

    @Test
    public void testMedalsAwarded() throws PWCGException 
    {
        Mockito.when(crewMember1.getCompanyId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        Mockito.when(crewMember2.getCompanyId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        Mockito.when(crewMember1.determineSquadron()).thenReturn(squadron1);
        Mockito.when(crewMember2.determineSquadron()).thenReturn(squadron1);

        medalsAwarded.put(crewMember1.getSerialNumber(), new HashMap<String, Medal>());
        medalsAwarded.put(crewMember2.getSerialNumber(), new HashMap<String, Medal>());
        Mockito.when(personnelAwards.getCampaignMemberMedals()).thenReturn(medalsAwarded);

        Map<Integer, Medal> frenchMedals = medalManager.getMedals();
        Medal cdg = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE);
        Medal cdgBronzeStar = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_STAR);
        Medal cdgSilverPalm = frenchMedals.get(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_PALM);
        medalsAwarded.get(crewMember1.getSerialNumber()).put(cdg.getMedalName(), cdg);
        medalsAwarded.get(crewMember1.getSerialNumber()).put(cdgBronzeStar.getMedalName(), cdgBronzeStar);
        medalsAwarded.get(crewMember2.getSerialNumber()).put(cdgSilverPalm.getMedalName(), cdgSilverPalm);
       
        MedalPanelEventTabulator medalPanelEventTabulator = new MedalPanelEventTabulator(campaign, aarContext);
        AARMedalPanelData medalPanelData = medalPanelEventTabulator.tabulateForAARMedalPanel();
        
        assert(medalPanelData.getMedalsAwarded().size() == 3);
    }

    @Test
    public void testMedalsAwardedButOneIsNotInSquadron() throws PWCGException 
    {
        Mockito.when(crewMember1.getCompanyId()).thenReturn(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        Mockito.when(crewMember2.getCompanyId()).thenReturn(SquadronTestProfile.ESC_3_PROFILE.getCompanyId());
        Mockito.when(crewMember1.determineSquadron()).thenReturn(squadron1);
        Mockito.when(crewMember2.determineSquadron()).thenReturn(squadron2);

        medalsAwarded.put(crewMember1.getSerialNumber(), new HashMap<String, Medal>());
        medalsAwarded.put(crewMember2.getSerialNumber(), new HashMap<String, Medal>());
        Mockito.when(personnelAwards.getCampaignMemberMedals()).thenReturn(medalsAwarded);

        FrenchMedalManager frenchMedalManager = new FrenchMedalManager(campaign);
        Medal cdg = frenchMedalManager.getMedal(FrenchMedalManager.CROIX_DE_GUERRE);
        Medal cdgBronzeStar = frenchMedalManager.getMedal(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_STAR);
        Medal cdgSilverPalm = frenchMedalManager.getMedal(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_PALM);
        medalsAwarded.get(crewMember1.getSerialNumber()).put(cdg.getMedalName(), cdg);
        medalsAwarded.get(crewMember1.getSerialNumber()).put(cdgBronzeStar.getMedalName(), cdgBronzeStar);
        medalsAwarded.get(crewMember2.getSerialNumber()).put(cdgSilverPalm.getMedalName(), cdgSilverPalm);

        MedalPanelEventTabulator medalPanelEventTabulator = new MedalPanelEventTabulator(campaign, aarContext);
        AARMedalPanelData medalPanelData = medalPanelEventTabulator.tabulateForAARMedalPanel();
        
        assert(medalPanelData.getMedalsAwarded().size() == 3);
    }

}
