package pwcg.campaign;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGUserException;
import pwcg.gui.utils.ContextSpecificImages;

public class PictureManager 
{

    public static String getPlanePicturePath(String planeName) throws PWCGUserException 
    {
        String picPath = ContextSpecificImages.imagesPlanes() + planeName;
        
        if (!picPath.contains(".jpg"))
        {
            picPath += ".jpg";
        }
        
        File planePic = new File(picPath);
        if (!planePic.exists() || !planePic.isFile())
        {
            throw new PWCGUserException ("Plane picture " + picPath + " not found");
        }
        
        return picPath;
    }

	public static String getPicturePath(SquadronMember pilot) throws PWCGUserException 
	{
		String picPath = null;
		if (pilot instanceof Ace || pilot instanceof HistoricalAce)
		{
			picPath = getAcePicturePath(pilot);
		}
		else
		{
			picPath = getPilotPicturePath(pilot);
		}
		
		return picPath;
	}

	private static File getAcePictureFile(SquadronMember ace) throws PWCGUserException 
	{
		String picPath = PWCGContext.getInstance().getDirectoryManager().getPwcgAcesDir() + "Pictures\\" + ace.getName();
		
		if (!picPath.contains(".jpg"))
		{
			picPath += ".jpg";
		}
		
		File acePic = new File(picPath);
		if (!acePic.exists() || !acePic.isFile())
		{
			throw new PWCGUserException ("Ace picture " + picPath + " not found");
		}

		return acePic;
	}

	private static File getPilotPictureFile(SquadronMember pilot) throws PWCGUserException 
	{
		List<File> picFiles = getFiles();
		for (File picFile : picFiles)
		{
			if (picFile.getName().equals(pilot.getPicName()))
			{
				return picFile;
			}
		}
		
		throw new PWCGUserException ("Pilot picture " + pilot.getPicName() + " not found");
	}

	private static String getPilotPicturePath(SquadronMember pilot) throws PWCGUserException 
	{
		File pic = getPilotPictureFile(pilot);
		
		return pic.getAbsolutePath();
	}

	private static String getAcePicturePath(SquadronMember ace) throws PWCGUserException 
	{
		File pic = getAcePictureFile(ace);
		return pic.getAbsolutePath();
	}

	private static List<File> getFiles() 
	{
        String basePicPath = ContextSpecificImages.imagesPilotPictures();

		List<File> picFiles = new ArrayList<File>();
		List<String>picDirs = getPilotPicDirs(basePicPath);
		for (String dirPath : picDirs)
		{
			File dir = new File(dirPath);
			
			if (dir.isDirectory())
			{
				File[] files = dir.listFiles();
				for (File picFile : files)
				{
					if (picFile.isFile() && picFile.getName().contains(".jpg"))
					{
						picFiles.add(picFile);
					}
				}
			}
		}
		
		return picFiles;
	}
	

	
	/**
	 * @param serviceId
	 * @return
	 */
	private static List<String> getPilotPicDirs(String basePicPath)
	{			
		List<String> picDirs = new ArrayList<String>();

		File dir = new File(basePicPath);
		File[] files = dir.listFiles();

		for(File file : files)
		{
			if (file.isDirectory())
			{
				picDirs.add(file.getAbsolutePath());
				List<String> subPicDirs = getPilotPicDirs(file.getAbsolutePath());
				picDirs.addAll(subPicDirs);
			}
		}
		
		return picDirs;
	}
}
