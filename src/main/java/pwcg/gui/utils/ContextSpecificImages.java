package pwcg.gui.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class ContextSpecificImages
{
    public static String menuPathForNation(Campaign campaign) throws PWCGException 
    {
		CrewMember referencePlayer = campaign.findReferencePlayer();
        String nationality = referencePlayer.determineSquadron().getCountry().getNationality();
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Menus\\" + nationality + "\\";
        
        return picPath;
    }

    public static String menuPathForMenus() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Menus\\";
        
        return picPath;
    }


    public static String imagesMaps() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Maps\\";
        
        return picPath;
    }

    public static String imagesPaperDoll() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "PaperDoll\\";
        
        return picPath;
    }

    public static String imagesPlayerPaperDoll() 
    {
        String picPath = PWCGDirectoryUserManager.getInstance().getPwcgUserDir() + "PaperDoll\\";
        
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

    public static String imagesCrewMemberPictures() 
    {
        String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "CrewMemberPictures\\";
        
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
