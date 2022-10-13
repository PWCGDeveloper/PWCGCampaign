package pwcg.dev.jsonconvert;

import pwcg.campaign.group.GroundStructureGroup;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.io.json.GroundObjectIOJson;

public class GroundObjectAirfieldSeperator
{
    public static void main(String[] args)
    {
        try
        {
            String mapName = "East1945";
            
            GroundStructureGroup groundStructureGroup = GroundObjectIOJson.readJson(mapName, GroupManager.GROUND_STRUCTURE_FILE_NAME);
            groundStructureGroup.generateAirfieldRelationships();
            GroundObjectIOJson.writeJson(groundStructureGroup, mapName, "NewGroundStructures.json");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}   
