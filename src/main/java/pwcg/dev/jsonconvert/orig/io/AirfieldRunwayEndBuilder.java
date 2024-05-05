package pwcg.dev.jsonconvert.orig.io;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.AirfieldDescriptor;
import pwcg.campaign.group.airfield.AirfieldDescriptorMapSet;
import pwcg.campaign.group.airfield.Runway;
import pwcg.campaign.io.json.AirfieldDescriptorIOMapJson;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class AirfieldRunwayEndBuilder
{
    public static void main(String[] args)
    {
        try 
        {
            PWCGContext.setProduct(PWCGProduct.BOS);
            String mapName = PWCGContext.getInstance().getMap(FrontMapIdentifier.NORMANDY_MAP).getMapName();
            AirfieldDescriptorMapSet airfieldDescriptorSet = AirfieldDescriptorIOMapJson.readJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + 
                    mapName + "\\", "AirfieldLocations");
            for (AirfieldDescriptor airfieldDescriptor : airfieldDescriptorSet.getLocations())
            {
                for (Runway runway : airfieldDescriptor.getRunways())
                {
                    double angle = airfieldDescriptor.getOrientation().getyOri();
                    Coordinate startPos = runway.getStartPos().copy();
                    Coordinate endPos = MathUtils.calcNextCoord(FrontMapIdentifier.NORMANDY_MAP, startPos, angle, 1200);
                    runway.setEndPos(endPos);
                }
            }

            AirfieldDescriptorIOMapJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "AirfieldLocations.json", airfieldDescriptorSet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
