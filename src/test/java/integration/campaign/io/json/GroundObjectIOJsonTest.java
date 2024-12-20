package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.GroundStructureGroup;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.io.json.GroundObjectIOJson;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class GroundObjectIOJsonTest
{
    @Test
    public void readJsonWesternFrontTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        String mapName = "WesternFront";
        validateGroundStructures(mapName);
    }
    
    @Test
    public void readJsonNormandyTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Normandy";
        validateGroundStructures(mapName);
    }
    
    @Test
    public void readJsonNormandyAtDDayTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Normandy";
        validateGroundStructures(mapName);
    }
    
    @Test
    public void readJsonNormandyAfterDDayTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        String mapName = "Normandy";
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

    private GroundStructureGroup validateGroundStructures(String mapName) throws PWCGException, PWCGException
    {
        GroundStructureGroup groundStructures = GroundObjectIOJson.readJson(mapName, GroupManager.GROUND_STRUCTURE_FILE_NAME);
        Assertions.assertTrue (groundStructures.getRailroadStations().size() > 0);
        Assertions.assertTrue (groundStructures.getBridges().size() > 0);
        Assertions.assertTrue (groundStructures.getStandaloneBlocks().size() > 0);
                
        return groundStructures;
    }

}
