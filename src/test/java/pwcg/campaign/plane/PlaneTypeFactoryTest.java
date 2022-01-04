package pwcg.campaign.plane;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class TankTypeFactoryTest
{
    @Mock Campaign campaign;
    
    public TankTypeFactoryTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void testCreatePlane() throws PWCGException
    {
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankType planeType =  planeTypeFactory.createTankTypeByType("bf110e2");
        assert(planeType.getType().equals("bf110e2"));
        assert(planeType.getArchType().equals("bf110"));
    }

    @Test
    public void testCreatePlaneByDesc() throws PWCGException
    {
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankType planeType =  planeTypeFactory.createTankTypeByAnyName("Bf 109 F-4");
        assert(planeType.getType().equals("bf109f4"));
        assert(planeType.getArchType().equals("bf109"));
    }

    @Test
    public void getAvailableTankTypesTest() throws PWCGException
    {
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        
        List<TankType> availableGermanTankTypes = planeTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.GERMANY), PwcgRoleCategory.FIGHTER, DateUtils.getDateYYYYMMDD("19430101"));        
        assert(availableGermanTankTypes.size() == 8);

        List<TankType> availableBritishTankTypes = planeTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.BRITAIN), PwcgRoleCategory.FIGHTER, DateUtils.getDateYYYYMMDD("19430101"));        
        assert(availableBritishTankTypes.size() == 2);

        List<TankType> availableAmericanTankTypes = planeTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.USA), PwcgRoleCategory.FIGHTER, DateUtils.getDateYYYYMMDD("19430101"));        
        assert(availableAmericanTankTypes.size() == 2);

        List<TankType> availableRussianTankTypes = planeTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.RUSSIA), PwcgRoleCategory.FIGHTER, DateUtils.getDateYYYYMMDD("19430101"));        
        assert(availableRussianTankTypes.size() == 8);
        
        List<TankType> availableGermanAttackTankTypes = planeTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.GERMANY), PwcgRoleCategory.ATTACK, DateUtils.getDateYYYYMMDD("19430101"));        
        assert(availableGermanAttackTankTypes.size() == 9);
    }

    @Test
    public void testAllTankTypes() throws PWCGException
    {
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        Map<String, TankType> planeTypes =  planeTypeFactory.getTankTypes();
        assert(planeTypes.size() > 30);
        for (TankType planeType : planeTypes.values())
        {
            assert(planeType.getArchType() != null);
            assert(planeType.getIntroduction().after(DateUtils.getDateYYYYMMDD("19390901")));
            assert(planeType.getWithdrawal().before(DateUtils.getDateYYYYMMDD("1945601")));
            assert(planeType.getEndProduction().before(DateUtils.getDateYYYYMMDD("1945601")));
            assert(planeType.getRoleCategories().size() > 0);
            assert(planeType.getPrimaryUsedBy().size() > 0);
        }
    }

    @Test
    public void testCreatePlanesForArchType() throws PWCGException
    {
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        List<TankType> planeTypes =  planeTypeFactory.createTankTypesForArchType("bf109");
        assert(planeTypes.size() == 8);
        for (TankType planeType : planeTypes)
        {
            assert(planeType.getArchType().equals("bf109"));
        }
    }

    @Test
    public void testCreateActivePlanesForArchType() throws PWCGException
    {
        Date planeDate = DateUtils.getDateYYYYMMDD("19420402");
        
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        List<TankType> planeTypes =  planeTypeFactory.createActiveTankTypesForArchType("bf109", planeDate);
        assert(planeTypes.size() == 3);
        for (TankType planeType : planeTypes)
        {
            assert(planeType.getArchType().equals("bf109"));
        }
    }

    @Test
    public void testCreateActiveFightersForCampaign() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420302"));
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        List<TankType> planeTypes =  planeTypeFactory.getAllFightersForCampaign(campaign);
        assert(planeTypes.size() == 14);
    }

}
