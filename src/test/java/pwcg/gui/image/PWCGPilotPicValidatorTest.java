package pwcg.gui.image;

import java.awt.image.BufferedImage;
import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGDirectoryProductManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

@RunWith(MockitoJUnitRunner.class)
public class PWCGPilotPicValidatorTest
{
    private String pilotPicDir;

    @Before
    public void setup ()
    {
        PWCGDirectoryProductManager directoryManager = new PWCGDirectoryProductManager(PWCGProduct.BOS);
        String imageDir  = directoryManager.getPwcgImagesDir();
        pilotPicDir = imageDir + "PilotPictures\\";
    }
    
    @Test
    public void pilotPicValidatorAmericanTest () throws PWCGException
    {
        pilotPicDir += "American\\";
        for (File pilotPicFile : FileUtils.getFilesWithFilter(pilotPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(pilotPicFile.getPath());
            assert (image != null);
        }
    }
    
    @Test
    public void pilotPicValidatorBritishTest () throws PWCGException
    {
        pilotPicDir += "British\\";
        for (File pilotPicFile : FileUtils.getFilesWithFilter(pilotPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(pilotPicFile.getPath());
            assert (image != null);
        }
    }
    
    @Test
    public void pilotPicValidatorGermanTest () throws PWCGException
    {
        pilotPicDir += "German\\";
        for (File pilotPicFile : FileUtils.getFilesWithFilter(pilotPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(pilotPicFile.getPath());
            assert (image != null);
        }
    }
    
    @Test
    public void pilotPicValidatorRussianTest () throws PWCGException
    {
        pilotPicDir += "Russian\\";
        for (File pilotPicFile : FileUtils.getFilesWithFilter(pilotPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(pilotPicFile.getPath());
            assert (image != null);
        }
    }
    
    @Test
    public void pilotPicValidatorItalianTest () throws PWCGException
    {
        pilotPicDir += "Italian\\";
        for (File pilotPicFile : FileUtils.getFilesWithFilter(pilotPicDir, ".jpg"))
        {
            BufferedImage image = ImageCache.getImageFromFile(pilotPicFile.getPath());
            assert (image != null);
        }
    }
}
