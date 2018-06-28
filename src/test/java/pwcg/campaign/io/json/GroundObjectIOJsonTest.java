package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.GroundStructureGroup;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

@RunWith(MockitoJUnitRunner.class)
public class GroundObjectIOJsonTest
{
    @Test
    public void readJsonFranceTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        String mapName = "France";
        validateGroundStructuresRoF(mapName);
    }

    @Test
    public void readJsonChannelTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        String mapName = "Channel";
        validateGroundStructuresRoF(mapName);
    }

    @Test
    public void readJsonGaliciaTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        String mapName = "Galicia";
        validateGroundStructuresRoF(mapName);
    }
    
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        String mapName = "Moscow";
        validateGroundStructuresBoS(mapName);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        String mapName = "Stalingrad";
        validateGroundStructuresBoS(mapName);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        String mapName = "Kuban";
        validateGroundStructuresBoS(mapName);
    }

    private void validateGroundStructuresRoF(String mapName) throws PWCGException, PWCGIOException
    {
        GroundStructureGroup groundStructures = GroundObjectIOJson.readJson(mapName);
        assert (groundStructures.getRailroadStations().size() > 0);
        assert (groundStructures.getAirfieldBlocks().size() > 0);
        assert (groundStructures.getBridges().size() > 0);
        assert (groundStructures.getStandaloneBlocks().size() > 0);
    }

    private void validateGroundStructuresBoS(String mapName) throws PWCGException, PWCGIOException
    {
        GroundStructureGroup groundStructures = GroundObjectIOJson.readJson(mapName);
        assert (groundStructures.getRailroadStations().size() > 0);
        assert (groundStructures.getBridges().size() > 0);
        assert (groundStructures.getStandaloneBlocks().size() > 0);
    }

}
