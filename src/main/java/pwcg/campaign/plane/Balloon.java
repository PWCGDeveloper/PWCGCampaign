package pwcg.campaign.plane;

import pwcg.product.fc.plane.FCPlaneAttributeMapping;

public class Balloon
{
    public static int BALLOON_ALTITUDE = 1500;
    
    public static boolean isBalloonName(String name) 
    {
  
       if (name.toLowerCase().contains(new String("parseval").toLowerCase()))
       {
           return true;
       }
       else if (name.toLowerCase().contains(new String("drachen").toLowerCase()))
       {
           return true;
       }
       else if (name.toLowerCase().contains(new String("caquot").toLowerCase()))
       {
           return true;
       }
       else if (name.toLowerCase().contains(new String("aetype").toLowerCase()))
       {
           return true;
       }
       else if (name.toLowerCase().contains(new String(FCPlaneAttributeMapping.BALLOON.getPlaneType()).toLowerCase()))
       {
           return true;
       }

       return false;
    }
}
