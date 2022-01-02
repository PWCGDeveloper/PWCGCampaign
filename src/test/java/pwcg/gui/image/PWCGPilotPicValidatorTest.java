package pwcg.gui.image;

import java.awt.image.BufferedImage;
import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGDirectoryProductManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

@ExtendWith(MockitoExtension.class)
public class PWCGCrewMemberPicValidatorTest
{
    private String crewMemberPicDir;

    @BeforeEach
    public void setup ()
    {
        PWCGDirectoryProductManager directoryManager = new PWCGDirectoryProductManager(PWCGProduct.BOS);
        String imageDir  = directoryManager.getPwcgImagesDir();
        crewMemberPicDir = imageDir + "CrewMemberPictures\\";
    }
    
    @Test
    public void crewMemberPicValidatorAmericanTest () throws PWCGException
    {
        crewMemberPicDir += "American\\";
        for (File crewMemberPicFile : FileUtils.getFilesWithFilter(crewMemberPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(crewMemberPicFile.getPath());
            Assertions.assertTrue (image != null);
        }
    }
    
    @Test
    public void crewMemberPicValidatorBritishTest () throws PWCGException
    {
        crewMemberPicDir += "British\\";
        for (File crewMemberPicFile : FileUtils.getFilesWithFilter(crewMemberPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(crewMemberPicFile.getPath());
            Assertions.assertTrue (image != null);
        }
    }
    
    @Test
    public void crewMemberPicValidatorGermanTest () throws PWCGException
    {
        crewMemberPicDir += "German\\";
        for (File crewMemberPicFile : FileUtils.getFilesWithFilter(crewMemberPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(crewMemberPicFile.getPath());
            Assertions.assertTrue (image != null);
        }
    }
    
    @Test
    public void crewMemberPicValidatorRussianTest () throws PWCGException
    {
        crewMemberPicDir += "Russian\\";
        for (File crewMemberPicFile : FileUtils.getFilesWithFilter(crewMemberPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(crewMemberPicFile.getPath());
            Assertions.assertTrue (image != null);
        }
    }
    
    @Test
    public void crewMemberPicValidatorItalianTest () throws PWCGException
    {
        crewMemberPicDir += "Italian\\";
        for (File crewMemberPicFile : FileUtils.getFilesWithFilter(crewMemberPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(crewMemberPicFile.getPath());
            Assertions.assertTrue (image != null);
        }
    }
}
