package pwcg.gui.image;

import java.awt.image.BufferedImage;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
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
	    
        Campaign campaign = PWCGContext.getInstance().getCampaign();
	    String mapOverlayPath = getOverlayPath(campaign, mapImageFileName);	     
	    if(mapOverlayPath != null)
	    {
	        overlay = ImageRetriever.getImageFromFile(mapOverlayPath);
	    }
        
        return overlay;
	}

    private static String getOverlayPath(Campaign campaign, String mapFileName) throws PWCGException
    {
        if (campaign == null)
        {
            return null;
        }
        
        SquadronMember referencePlayer = campaign.getPersonnelManager().getAnyCampaignMember(campaign.getCampaignData().getReferencePlayerSerialNumber());
        if (referencePlayer == null)
        {
            return null;
        }
        
        String mapOverlayFullPath = ContextSpecificImages.imagesMaps() + "Overlay\\" + PWCGContext.getInstance().getCurrentMap().getMapName() + "\\";
        mapOverlayFullPath += DateUtils.getDateStringYYYYMMDD(campaign.getDate()) + "\\" + mapFileName + ".png";

        return mapOverlayFullPath;
    }
}
