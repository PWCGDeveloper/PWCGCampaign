package pwcg.campaign.personnel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.gui.utils.ContextSpecificImages;

public class PilotPictureBuilder
{
    private ArmedService service;
    private SquadronMembers squadronMembersAlreadyAssigned;

    public PilotPictureBuilder (ArmedService service, SquadronMembers squadronMembersAlreadyAssigned)
    {
        this.service = service;
        this.squadronMembersAlreadyAssigned = squadronMembersAlreadyAssigned;
    }
    
    public String assignPilotPicture() throws PWCGException 
    {
        List<File> picFiles = getFiles();
        return assignPilotPictureFromList(picFiles);
    }
    
    public String assignFemalePilotPicture() throws PWCGException 
    {
        List<File> picFiles = getFemaleFiles();
        return assignPilotPictureFromList(picFiles);
    }
    
    private String assignPilotPictureFromList(List<File> picFiles) throws PWCGException 
    {
        HashMap<String, String> picturesUsed = getPicturesInUse();

        int index = RandomNumberGenerator.getRandom(picFiles.size());
        File pic = picFiles.get(index);
        String picName = pic.getName();
        while (picturesUsed.containsKey(picName) && (picturesUsed.size() < (picFiles.size() - 5)))
        {
            index = RandomNumberGenerator.getRandom(picFiles.size());
            pic = picFiles.get(index);
            picName = pic.getName();
        }
        
        picturesUsed.put(picName, picName);
        
        return picName;
    }

    private HashMap<String, String> getPicturesInUse() throws PWCGException
    {
        HashMap<String, String> picsInUse = new HashMap<String, String>();
        for (SquadronMember squadronMember : squadronMembersAlreadyAssigned.getSquadronMemberList())
        {
            picsInUse.put(squadronMember.getPicName(), squadronMember.getPicName());
        }
        
        return picsInUse;
    }

    private List<File> getFiles() 
    {
        List<String>picDirs = service.getPicDirs();
        List<File> picFiles = getPictureFilesFromDirectories(picDirs);
        return picFiles;
    }
    
    private List<File> getFemaleFiles() 
    {
        List<String> picDirs = service.getPicDirs();
        List<String> femalePicDirs = new ArrayList<>();;
        for (String dirName : picDirs)
        {
            String femaleDirName = dirName + "\\Female";
            femalePicDirs.add(femaleDirName);
        }
        
        List<File> picFiles = getPictureFilesFromDirectories(femalePicDirs);
        return picFiles;
    }

    private List<File> getPictureFilesFromDirectories(List<String> picDirs)
    {
        List<File> picFiles = new ArrayList<File>();
        for (String dirName : picDirs)
        {
            String basePicPath = ContextSpecificImages.imagesPilotPictures();
            String dirPath = basePicPath + dirName;
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
}
