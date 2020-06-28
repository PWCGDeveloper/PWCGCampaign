package pwcg.gui;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.ContextSpecificImages;

public class UiImageResolver
{

    public static String getImageMain(String leftImageName) throws PWCGException
    {
        String imagePath = "";
        String menuPath = ContextSpecificImages.menuPathForMenus();
        imagePath = menuPath + leftImageName;
        return imagePath;
    }


    public static String getImageMisc(String leftImageName) throws PWCGException
    {
        String imagePath = "";
        String menuPath = ContextSpecificImages.imagesMisc();
        imagePath = menuPath + leftImageName;
        return imagePath;
    }


    public static String getImage(Campaign campaign, String leftImageName) throws PWCGException
    {
        String menuPath = ContextSpecificImages.imagesMisc();
        String imagePath = menuPath + leftImageName;
        if (!imageExists(imagePath))
        {
            menuPath = ContextSpecificImages.menuPathForNation(campaign);
            imagePath = menuPath + leftImageName;
        }
        return imagePath;
    }    
    
    private static boolean imageExists(String imagePath)
    {
        File file = new File(imagePath);
        return file.exists();
    }

}
