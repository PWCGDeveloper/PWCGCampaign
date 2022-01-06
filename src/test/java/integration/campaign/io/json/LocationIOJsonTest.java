package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;

@ExtendWith(MockitoExtension.class)
public class LocationIOJsonTest
{
    @Test
    public void readJsonArrasTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\FCData\\Input\\Arras\\19170601\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        Assertions.assertTrue (locationSet.getLocations().size() > 0);
    }
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Moscow\\19411001\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        Assertions.assertTrue (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Stalingrad\\19421011\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        Assertions.assertTrue (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Kuban\\19420601\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        Assertions.assertTrue (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonBodenplatteTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String directory = System.getProperty("user.dir") + "\\BoSData\\Input\\Bodenplatte\\19440901\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        Assertions.assertTrue (locationSet.getLocations().size() > 0);
    }
}
