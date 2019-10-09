package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.GroundStructureGroup;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

@RunWith(MockitoJUnitRunner.class)
public class GroundObjectIOJsonTest
{
    @Test
    public void readJsonFranceTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        String mapName = "France";
        validateGroundStructuresRoF(mapName);
    }

    @Test
    public void readJsonChannelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        String mapName = "Channel";
        validateGroundStructuresRoF(mapName);
    }

    @Test
    public void readJsonGaliciaTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        String mapName = "Galicia";
        validateGroundStructuresRoF(mapName);
    }
    
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Moscow";
        validateGroundStructuresBoS(mapName);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Stalingrad";
        validateGroundStructuresBoS(mapName);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Kuban";
        validateGroundStructuresBoS(mapName);
    }

    @Test
    public void readJsonBoddenplatteTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Bodenplatte";
        validateGroundStructuresBoS(mapName);        
    }

    private GroundStructureGroup validateGroundStructuresRoF(String mapName) throws PWCGException, PWCGIOException
    {
        GroundStructureGroup groundStructures = GroundObjectIOJson.readJson(mapName);
        assert (groundStructures.getRailroadStations().size() > 0);
        assert (groundStructures.getAirfieldBlocks().size() > 0);
        assert (groundStructures.getBridges().size() > 0);
        assert (groundStructures.getStandaloneBlocks().size() > 0);
        return groundStructures;
    }

    private GroundStructureGroup validateGroundStructuresBoS(String mapName) throws PWCGException, PWCGIOException
    {
        GroundStructureGroup groundStructures = GroundObjectIOJson.readJson(mapName);
        assert (groundStructures.getRailroadStations().size() > 0);
        assert (groundStructures.getBridges().size() > 0);
        assert (groundStructures.getStandaloneBlocks().size() > 0);
        return groundStructures;
    }

}
