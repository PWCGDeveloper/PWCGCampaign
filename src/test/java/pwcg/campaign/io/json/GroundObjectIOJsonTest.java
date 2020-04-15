package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.GroundStructureGroup;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

@RunWith(MockitoJUnitRunner.class)
public class GroundObjectIOJsonTest
{
    @Test
    public void readJsonArrasTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        String mapName = "Arras";
        validateGroundStructures(mapName);
    }
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Moscow";
        validateGroundStructures(mapName);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Stalingrad";
        validateGroundStructures(mapName);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Kuban";
        validateGroundStructures(mapName);
    }

    @Test
    public void readJsonBoddenplatteTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Bodenplatte";
        validateGroundStructures(mapName);        
    }

    private GroundStructureGroup validateGroundStructures(String mapName) throws PWCGException, PWCGIOException
    {
        GroundStructureGroup groundStructures = GroundObjectIOJson.readJson(mapName);
        assert (groundStructures.getRailroadStations().size() > 0);
        assert (groundStructures.getBridges().size() > 0);
        assert (groundStructures.getStandaloneBlocks().size() > 0);
        return groundStructures;
    }

}
