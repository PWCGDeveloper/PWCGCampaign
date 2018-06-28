package pwcg.campaign.personnel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.gui.utils.ContextSpecificImages;

public class PilotPictureBuilder
{
    private ArmedService service;
    private Map<Integer, SquadronMember> squadronMembersAlreadyAssigned;

    public PilotPictureBuilder (ArmedService service, Map<Integer, SquadronMember> squadronMembersAlreadyAssigned)
    {
        this.service = service;
        this.squadronMembersAlreadyAssigned = squadronMembersAlreadyAssigned;
    }
    
    public String assignPilotPicture() throws PWCGException 
    {
        HashMap<String, String> picturesUsed = getPicturesInUse();
        List<File> picFiles = getFiles(service);

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
        for (SquadronMember squadronMember : squadronMembersAlreadyAssigned.values())
        {
            picsInUse.put(squadronMember.getPicName(), squadronMember.getPicName());
        }
        
        return picsInUse;
    }

    private List<File> getFiles(ArmedService service) 
    {
        String basePicPath = ContextSpecificImages.imagesPilotPictures();

        List<File> picFiles = new ArrayList<File>();
        List<String>picDirs =service.getPicDirs();
        for (String dirName : picDirs)
        {
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
