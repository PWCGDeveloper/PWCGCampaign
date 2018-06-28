package pwcg.gui.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGDirectoryManager;
import pwcg.core.exception.PWCGException;

public class ContextSpecificImages
{
    public static String menuPathForNation() throws PWCGException 
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        
        String nationality = campaign.determineCountry().getNationality();
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "Menus\\" + nationality + "\\";
        
        return picPath;
    }

    public static String menuPathForGeneric() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "Menus\\Generic\\";
        
        return picPath;
    }

    public static String menuPathMain() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "Menus\\Main\\";
        
        return picPath;
    }


    public static String imagesMaps() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "Maps\\";
        
        return picPath;
    }

    public static String imagesMedals() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "Medals\\";
        
        return picPath;
    }

    public static String imagesMisc() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "Misc\\";
        
        return picPath;
    }

    public static String imagesNational() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "National\\";
        
        return picPath;
    }

    public static String imagesNewspaper() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "Newspaper\\";
        
        return picPath;
    }

    public static String imagesPilotPictures() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "PilotPictures\\";
        
        return picPath;
    }

    public static String imagesPlanes() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "Planes\\";
        
        return picPath;
    }

    public static String imagesProfiles() 
    {
        String picPath = PWCGDirectoryManager.getInstance().getPwcgImagesDir() + "Profiles\\";
        
        return picPath;
    }

}
