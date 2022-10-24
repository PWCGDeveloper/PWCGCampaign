package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SkinManagerTest
{
    private SkinManager skinManager;
    

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        skinManager = PWCGContext.getInstance().getSkinManager();
    }

    @Test
    public void skinLoaderInitializeTest() throws PWCGException
    {
        List<Skin> testSkins;
        String planeType = "bf109f4";
        ICountry iCountry = CountryFactory.makeCountryByCountry(Country.GERMANY);

        testSkins = skinManager.getLooseSkinByPlane(planeType);
        Assertions.assertTrue (testSkins.size() > 0);
        
        testSkins = skinManager.getPersonalSkinsByPlaneCountryDateInUse(planeType, iCountry.getCountryName(), DateUtils.getDateYYYYMMDD("19420401"));
        Assertions.assertTrue (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadron(planeType, 20111051);
        Assertions.assertTrue (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadron(planeType, 20111052);
        Assertions.assertTrue (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneCountry(planeType, iCountry.getCountryName());
        Assertions.assertTrue (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneSquadronDateInUse(planeType, 20111052, DateUtils.getDateYYYYMMDD("19420401"));
        Assertions.assertTrue (testSkins.size() > 0);
    }

    @Test
    public void blankSkinTest() throws PWCGException
    {
        List<String> hasNoBlanks = new ArrayList<>();
        hasNoBlanks.add("bf109g6late");
        
        for (String planeName : skinManager.getAllSkinsByPlane().keySet())
        {
            boolean baseForPlane = false;
            boolean blankForPlane = false;
            for (Skin skin : skinManager.getSkinsForPlane(planeName).getConfiguredSkins().getSkins().values())
            {
                if (skin.getSkinName().toLowerCase().contains("blank"))
                {
                    Assertions.assertEquals (-2, skin.getSquadId());
                    Assertions.assertEquals (true, skin.isUseTacticalCodes());
                    blankForPlane = true;
                }
                else if (skin.getSkinName().toLowerCase().equals(planeName.toLowerCase()))
                {
                    Assertions.assertEquals (-2, skin.getSquadId());
                    Assertions.assertEquals (true, skin.isUseTacticalCodes());
                    baseForPlane = true;
                }
                else
                {
                    Assertions.assertNotEquals (-2, skin.getSquadId());
                }
            }
            
            Assertions.assertTrue (baseForPlane);
            if (!hasNoBlanks.contains(planeName))
            {
                Assertions.assertTrue (blankForPlane);
            }
        }
    }

    @Test
    public void factorySkinAlwaysAvailableTest() throws PWCGException
    {
        List<String> hasNoBlanks = new ArrayList<>();
        hasNoBlanks.add("bf109g6late");
        
        for (String planeName : skinManager.getAllSkinsByPlane().keySet())
        {
            Date today = DateUtils.getBeginningOfGame();
            while (today.before(DateUtils.getEndOfWar()))
            {
                List<Skin> factorySkins = skinManager.getSkinsByPlaneSquadronDateInUse(planeName, -2, today);
                Assertions.assertFalse (factorySkins.isEmpty());
                today = DateUtils.advanceTimeDays(today, 1);
            }
        }
    }

    @Test
    public void validDateRangeTest() throws PWCGException
    {
        List<String> hasNoBlanks = new ArrayList<>();
        hasNoBlanks.add("bf109g6late");
        
        for (String planeName : skinManager.getAllSkinsByPlane().keySet())
        {
            for (Skin skin : skinManager.getSkinsForPlane(planeName).getAllUsedByPWCG())
            {
                Assertions.assertTrue (skin.getStartDate().before(skin.getEndDate()));
            }
        }
    }

}
