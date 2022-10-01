package pwcg.dev.jsonconvert.orig.io;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.AirfieldDescriptor;
import pwcg.campaign.group.airfield.AirfieldDescriptorSet;
import pwcg.campaign.group.airfield.Runway;
import pwcg.campaign.io.json.AirfieldDescriptorIOJson;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class AirfieldRunwayEndBuilder
{
    public static void main(String[] args)
    {
        try 
        {
            PWCGContext.setProduct(PWCGProduct.BOS);
            PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.NORMANDY_MAP); // Critical that correct map is chosen
            String mapName = PWCGContext.getInstance().getCurrentMap().getMapName();
            AirfieldDescriptorSet airfieldDescriptorSet = AirfieldDescriptorIOJson.readJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + 
                    mapName + "\\", "AirfieldLocations");
            for (AirfieldDescriptor airfieldDescriptor : airfieldDescriptorSet.getLocations())
            {
                for (Runway runway : airfieldDescriptor.getRunways())
                {
                    
                    if (airfieldDescriptor.getName().equalsIgnoreCase("newchurch"))
                    {
                        double angleToEnd = MathUtils.calcAngle(runway.getStartPos(), runway.getEndPos());
                        System.out.println("Newchurch before " + angleToEnd);
                    }

                    double angle = airfieldDescriptor.getOrientation().getyOri();
                    Coordinate startPos = runway.getStartPos().copy();
                    Coordinate endPos = MathUtils.calcNextCoord(startPos, angle, 1200);
                    runway.setEndPos(endPos);
                    
                    if (airfieldDescriptor.getName().equalsIgnoreCase("newchurch"))
                    {
                        double angleToEnd = MathUtils.calcAngle(startPos, runway.getEndPos());
                        System.out.println("Newchurch after " + angleToEnd);
                    }
                }
            }

            AirfieldDescriptorIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "AirfieldLocations.json", airfieldDescriptorSet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
