package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;

@RunWith(MockitoJUnitRunner.class)
public class LocationIOJsonTest
{
    @Test
    public void readJsonFranceTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        String directory = System.getProperty("user.dir") + "\\RoFData\\Input\\France\\19160101\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonChannelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        String directory = System.getProperty("user.dir") + "\\RoFData\\Input\\Channel\\19160101\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonGaliciaTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        String directory = System.getProperty("user.dir") + "\\RoFData\\Input\\Galicia\\19160101\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Moscow\\19411001\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Stalingrad\\19421011\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Kuban\\19420601\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        assert (locationSet.getLocations().size() > 0);
    }
}
