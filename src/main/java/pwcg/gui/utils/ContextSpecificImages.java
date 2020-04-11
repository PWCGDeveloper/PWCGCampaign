package pwcg.gui.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class ContextSpecificImages
{
    public static String menuPathForNation(Campaign campaign) throws PWCGException 
    {
		SquadronMember referencePlayer = campaign.findReferencePlayer();
        String nationality = referencePlayer.determineSquadron().getCountry().getNationality();
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Menus\\" + nationality + "\\";
        
        return picPath;
    }

    public static String menuPathForGeneric() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Menus\\Generic\\";
        
        return picPath;
    }

    public static String menuPathMain() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Menus\\Main\\";
        
        return picPath;
    }


    public static String imagesMaps() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Maps\\";
        
        return picPath;
    }

    public static String imagesMedals() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Medals\\";
        
        return picPath;
    }

    public static String imagesMisc() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Misc\\";
        
        return picPath;
    }

    public static String imagesNational() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "National\\";
        
        return picPath;
    }

    public static String imagesNewspaper() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Newspaper\\";
        
        return picPath;
    }

    public static String imagesPilotPictures() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "PilotPictures\\";
        
        return picPath;
    }

    public static String imagesPlanes() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Planes\\";
        
        return picPath;
    }

    public static String imagesProfiles() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Profiles\\";
        
        return picPath;
    }

}
