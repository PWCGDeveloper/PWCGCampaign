package pwcg.campaign.plane;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class PlaneTypeFactoryTest
{
    @Mock Campaign campaign;
    
    @Before 
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420302"));
        ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
        Mockito.when(campaign.determineCountry()).thenReturn(country);
    }

    @Test
    public void testCreatePlane() throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        PlaneType planeType =  planeTypeFactory.createPlaneTypeByType("bf110e2");
        assert(planeType.getType().equals("bf110e2"));
        assert(planeType.getArchType().equals("bf110"));
    }

    @Test
    public void testCreatePlaneByDesc() throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        PlaneType planeType =  planeTypeFactory.createPlaneTypeByAnyName("Bf 109 F-4");
        assert(planeType.getType().equals("bf109f4"));
        assert(planeType.getArchType().equals("bf109"));
    }

    @Test
    public void testCreatePlaneBySide() throws PWCGException
    {
        Date planeDate = DateUtils.getDateYYYYMMDD("19420302");
        
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        PlaneType planeType =  planeTypeFactory.getActivePlaneBySideAndDate(Side.ALLIED, planeDate);
        assert(planeType.getIntroduction().before(planeDate));
        assert(planeType.getWithdrawal().after(planeDate));
        assert(planeType.getSide() == Side.ALLIED);
    }

    @Test
    public void testCreatePlanesForArchType() throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        List<PlaneType> planeTypes =  planeTypeFactory.createPlaneTypesForArchType("bf109");
        assert(planeTypes.size() == 7);
        for (PlaneType planeType : planeTypes)
        {
            assert(planeType.getArchType().equals("bf109"));
        }
    }

    @Test
    public void testCreateActivePlanesForArchType() throws PWCGException
    {
        Date planeDate = DateUtils.getDateYYYYMMDD("19420402");
        
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        List<PlaneType> planeTypes =  planeTypeFactory.createActivePlaneTypesForArchType("bf109", planeDate);
        assert(planeTypes.size() == 2);
        for (PlaneType planeType : planeTypes)
        {
            assert(planeType.getArchType().equals("bf109"));
        }
    }

    @Test
    public void testCreateActiveFightersForCampaign() throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        List<PlaneType> planeTypes =  planeTypeFactory.getAllFightersForCampaign(campaign);
        assert(planeTypes.size() == 5);
    }

}
