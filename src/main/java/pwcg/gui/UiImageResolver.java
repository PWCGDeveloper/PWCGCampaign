package pwcg.gui;

import java.io.File;

import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.ContextSpecificImages;

public class UiImageResolver
{

    public static String getImage(ScreenIdentifier screenIdentifier) throws PWCGException
    {
        String override = ScreenIdentifierOverrideManager.getInstance().getOverride(screenIdentifier);
        String imageFilePath = findImageInDirectories(override);
        if (!(imageExists(imageFilePath)))
        {
            imageFilePath = findImageInDirectories(screenIdentifier.getDefaultImageName());
            if (!(imageExists(imageFilePath)))
            {
                throw new PWCGException("Failed to locate image " + screenIdentifier.getDefaultImageName());
            }
            else
            {
                return imageFilePath;
            }
        }
        else
        {
            return imageFilePath;
        }
    }

    private static String findImageInDirectories(String filename) throws PWCGException
    {
        String mainFilePath = getImageMain(filename);
        if (!(imageExists(mainFilePath)))
        {
            String miscFilePath = getImageMisc(filename);
            if (!(imageExists(miscFilePath)))
            {
                return "NOT_FOUND";
            }
            else
            {
                return miscFilePath;
            }
        }
        else
        {
            return mainFilePath;
        }
    }

    private static String getImageMain(String leftImageName) throws PWCGException
    {
        String imagePath = "";
        String menuPath = ContextSpecificImages.menuPathForMenus();
        imagePath = menuPath + leftImageName;
        return imagePath;
    }


    private static String getImageMisc(String leftImageName) throws PWCGException
    {
        String imagePath = "";
        String menuPath = ContextSpecificImages.imagesMisc();
        imagePath = menuPath + leftImageName;
        return imagePath;
    }
    
    private static boolean imageExists(String imagePath)
    {
        File file = new File(imagePath);
        return file.exists();
    }

}
