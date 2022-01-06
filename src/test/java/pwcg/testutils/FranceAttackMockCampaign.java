package pwcg.testutils;

import java.util.Date;

import org.mockito.Mock;
import org.mockito.Mockito;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.MissionGroundUnitResourceManager;

public class FranceAttackMockCampaign
{
    @Mock
    protected Campaign campaign;

    @Mock
    protected Mission mission;

    @Mock
    protected MissionFlights missionFlightBuilder;

    @Mock
    protected ConfigManagerCampaign configManager;

    protected ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
    protected Coordinate myTestPosition = new Coordinate (100000, 0, 100000);
    protected Coordinate mytargetLocation = new Coordinate (100000, 0, 150000);
    
    protected Date date;
    protected MissionGroundUnitResourceManager missionGroundUnitResourceManager = new MissionGroundUnitResourceManager();
    protected CoordinateBox missionBorders;
    protected Company squadron;

    public void mockCampaignSetup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);

        date = DateUtils.getDateYYYYMMDD("19180501");
        
        squadron = PWCGContext.getInstance().getCompanyManager().getCompany(401010);

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(date);
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
        Mockito.when(mission.getMissionGroundUnitManager()).thenReturn(missionGroundUnitResourceManager);
        
        
        missionBorders = CoordinateBox.coordinateBoxFromCenter(myTestPosition, 100000);
        Mockito.when(mission.getFlights()).thenReturn(missionFlightBuilder);
        Mockito.when(mission.getMissionBorders()).thenReturn(missionBorders);
        Mockito.when(mission.getCampaign()).thenReturn(campaign);        
    }

}
