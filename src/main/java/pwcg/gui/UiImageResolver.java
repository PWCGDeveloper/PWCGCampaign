package pwcg.gui;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.ContextSpecificImages;

public class UiImageResolver
{

    public static String getSideImageMain(String leftImageName) throws PWCGException
    {
        String imagePath = "";

        int useGenericUI = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.UseGenericUIKey);
        if (useGenericUI == 1)
        {
            String sideImageName = "GenericSide.jpg";
            String menuPath = ContextSpecificImages.menuPathForGeneric();
            imagePath = menuPath + sideImageName;
        }
        else
        {
            String menuPath = ContextSpecificImages.menuPathMain();
            imagePath = menuPath + leftImageName;
        }
        
        return imagePath;
    }


    public static String getSideImage(Campaign campaign, String leftImageName) throws PWCGException
    {
        String imagePath = "";

        int useGenericUI = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.UseGenericUIKey);

        if (useGenericUI == 1)
        {
            String sideImageName = "GenericSide.jpg";
            String menuPath = ContextSpecificImages.menuPathForGeneric();
            imagePath = menuPath + sideImageName;
        }
        else
        {
            String menuPath = ContextSpecificImages.imagesMisc();
            imagePath = menuPath + leftImageName;
            if (!imageExists(imagePath))
            {
                menuPath = ContextSpecificImages.menuPathForNation(campaign);
                imagePath = menuPath + leftImageName;
            }
        }
        
        return imagePath;
    }    
    
    private static boolean imageExists(String imagePath)
    {
        File file = new File(imagePath);
        return file.exists();
    }

}
