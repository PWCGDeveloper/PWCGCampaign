package pwcg.campaign.skin;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class SkinManagerTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
    }

    @Test
    public void skinLoaderInitializeTest() throws PWCGException
    {
        SkinManager skinManager = PWCGContext.getInstance().getSkinManager();
        List<Skin> testSkins;
        String planeType = "fokkerd7";
        ICountry iCountry = CountryFactory.makeCountryByCountry(Country.GERMANY);
        
        /*
         * No skins for now so nothing to test
         */
        testSkins = skinManager.getLooseSkinByPlane(planeType);
        //assert (testSkins.size() == 0);
        
        testSkins = skinManager.getPersonalSkinsByPlaneCountryDateInUse(planeType, iCountry.getCountryName(), DateUtils.getDateYYYYMMDD("19180901"));
        //assert (testSkins.size() == 0);
        
        testSkins = skinManager.getPersonalSkinsByPlaneCountryDateInUse("s16", iCountry.getCountryName(), DateUtils.getDateYYYYMMDD("19180901"));
        //assert (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadron(planeType, 501011);
        //assert (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByPlaneCountry(planeType, iCountry.getCountryName());
        //assert (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadronDateInUse(planeType, 501011, DateUtils.getDateYYYYMMDD("19180901"));
        //assert (testSkins.size() == 0);
    }

}
