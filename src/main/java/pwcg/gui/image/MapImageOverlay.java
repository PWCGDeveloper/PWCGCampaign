package pwcg.gui.image;

import java.awt.image.BufferedImage;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.utils.ContextSpecificImages;

public class MapImageOverlay 
{
	private MapImageOverlay ()
	{
	}

	public static BufferedImage getMapImage(String mapImageFileName) throws PWCGException 
	{
	    BufferedImage overlay = null;
	    
	    String mapOverlayPath = getOverlayPath(mapImageFileName);	     
	    if(mapOverlayPath != null)
	    {
	        overlay = ImageRetriever.getImageFromFile(mapOverlayPath);
	    }
        
        return overlay;
	}

    private static String getOverlayPath(String mapFileName) throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        if (campaign == null)
        {
            return null;
        }
        
        CrewMember referencePlayer = campaign.getPersonnelManager().getAnyCampaignMember(campaign.getCampaignData().getReferencePlayerSerialNumber());
        if (referencePlayer == null)
        {
            return null;
        }
        
        String mapOverlayFullPath = ContextSpecificImages.imagesMaps() + "Overlay\\" + PWCGContext.getInstance().getCurrentMap().getMapName() + "\\";
        mapOverlayFullPath += DateUtils.getDateStringYYYYMMDD(campaign.getDate()) + "\\" + mapFileName + ".png";

        return mapOverlayFullPath;
    }
}
