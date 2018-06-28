package pwcg.campaign.skin;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class SkinManagerTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

    @Test
    public void skinLoaderInitializeTest() throws PWCGException
    {
        SkinManager skinManager = PWCGContextManager.getInstance().getSkinManager();
        List<Skin> testSkins;
        Skin testSkin;
        String planeType = "fokkerd7";
        ICountry iCountry = CountryFactory.makeCountryByCountry(Country.GERMANY);
        
        testSkin = skinManager.getConfiguredSkinByName("halberstadtcl2", "HCL2_PW_S12_10_M");
        assert (testSkin.getSkinName().equals("HCL2_PW_S12_10_M"));
        
        testSkins = skinManager.getLooseSkinByPlane(planeType);
        assert (testSkins.size() > 0);
        
        testSkins = skinManager.getPersonalSkinsByPlaneCountryDateInUse(planeType, iCountry.getCountryName(), DateUtils.getDateYYYYMMDD("19180901"));
        assert (testSkins.size() > 0);
        
        testSkins = skinManager.getPersonalSkinsByPlaneCountryDateInUse("s16", iCountry.getCountryName(), DateUtils.getDateYYYYMMDD("19180901"));
        assert (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadron(planeType, 501011);
        assert (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneCountry(planeType, iCountry.getCountryName());
        assert (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadronDateInUse(planeType, 501011, DateUtils.getDateYYYYMMDD("19180901"));
        assert (testSkins.size() > 0);
    }

}
