package pwcg.campaign.personnel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.plane.Equipment;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class EnemySquadronFinderTest
{
    @Mock private Company squadron;
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private CampaignEquipmentManager equipmentManager;
    @Mock private Equipment equipment;
    @Mock private CompanyPersonnel squadronPersonnel;
    @Mock private Airfield squadronAirfield;
    @Mock private ConfigManagerCampaign configManagerCampaign;
    
    @BeforeEach
    public void setupTest() throws PWCGException
     {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.STALINGRAD_MAP);

        ICountry squadronCountry = CountryFactory.makeCountryByCountry(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManagerCampaign);
        
        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.RemoveNonHistoricalSquadronsKey)).thenReturn(0);

        Mockito.when(personnelManager.getCompanyPersonnel(Mockito.any())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.isSquadronPersonnelViable()).thenReturn(true);

        Mockito.when(campaign.getEquipmentManager()).thenReturn(equipmentManager);
        Mockito.when(equipmentManager.getEquipmentForCompany(Mockito.any())).thenReturn(equipment);
        Mockito.when(equipment.isCompanyEquipmentViable()).thenReturn(true);
        
        Mockito.when(squadron.getCountry()).thenReturn(squadronCountry);
    }
    

    
    @Test
    public void findEnemySquadronFromCorner () throws PWCGException
    {     
        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Company enemySquadron = enemySquadronFinder.getEnemyForOutOfMission(squadron, DateUtils.getDateYYYYMMDD("19421001"));
        assert(enemySquadron != null);
    }

    @Test
    public void findEnemySquadronNearby () throws PWCGException
    {     
        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Company enemySquadron = enemySquadronFinder.getEnemyForOutOfMission(squadron, DateUtils.getDateYYYYMMDD("19421001"));
        assert(enemySquadron != null);
    }

    @Test
    public void findEnemySquadronAny () throws PWCGException
    {     
        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Company enemySquadron = enemySquadronFinder.getEnemyForOutOfMission(squadron, DateUtils.getDateYYYYMMDD("19421001"));
        assert(enemySquadron != null);
    }
}
