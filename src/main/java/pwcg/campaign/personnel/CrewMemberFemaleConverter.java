package pwcg.campaign.personnel;

import java.util.HashMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberNames;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.product.bos.country.BoSServiceManager;

public class CrewMemberFemaleConverter
{
    public static CrewMember possiblyConvertToFemale(ArmedService service, CrewMember crewMember, HashMap<String, String> namesUsed) throws PWCGException
    {
        if (service.getServiceId() != BoSServiceManager.SVV)
        {
            return crewMember;
        }
        
        int femaleOdds = 3;
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < femaleOdds)
        {
            convertToFemaleName(crewMember, namesUsed);
            convertToFemalePicture(service, crewMember);
        }
        
        return crewMember;
    }
    
    private static void convertToFemaleName(CrewMember crewMember, HashMap<String, String> namesUsed) throws PWCGException
    {
        String femaleName = CrewMemberNames.getInstance().getFemaleName(namesUsed);
        crewMember.setName(femaleName);
    }
    

    private static void convertToFemalePicture(ArmedService service, CrewMember crewMember) throws PWCGException
    {
        CrewMemberPictureBuilder crewMemberPictureBuilder = new CrewMemberPictureBuilder(service, new CrewMembers());
        String picPath = crewMemberPictureBuilder.assignFemaleCrewMemberPicture();
        crewMember.setPicName(picPath);        
    }
}
