package pwcg.campaign.skin;

import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class SkinManagerTest
{
    public SkinManagerTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void skinLoaderInitializeTest() throws PWCGException
    {
        SkinManager skinManager = PWCGContext.getInstance().getSkinManager();
        List<Skin> testSkins;
        String planeType = "bf109f4";
        ICountry iCountry = CountryFactory.makeCountryByCountry(Country.GERMANY);

        testSkins = skinManager.getLooseSkinByPlane(planeType);
        assert (testSkins.size() > 0);
        
        testSkins = skinManager.getPersonalSkinsByPlaneCountryDateInUse(planeType, iCountry.getCountryName(), DateUtils.getDateYYYYMMDD("19420401"));
        assert (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadron(planeType, 20111051);
        assert (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadron(planeType, 20111052);
        assert (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneCountry(planeType, iCountry.getCountryName());
        assert (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadronDateInUse(planeType, 20111052, DateUtils.getDateYYYYMMDD("19420401"));
        assert (testSkins.size() > 0);
    }

}
