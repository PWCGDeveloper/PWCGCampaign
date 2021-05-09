package pwcg.dev.jsonconvert;

import pwcg.campaign.group.GroundStructureGroup;
import pwcg.campaign.io.json.GroundObjectIOJson;

public class groundObjectAirfieldSeperator
{
    public static void main(String[] args)
    {
        try
        {
            String mapName = "East1945";
            
            GroundStructureGroup groundStructureGroup = GroundObjectIOJson.readJson(mapName);
            groundStructureGroup.generateAirfieldRelationships();
            GroundObjectIOJson.writeJson(groundStructureGroup, mapName, "NewGroundStructures.json");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}   
