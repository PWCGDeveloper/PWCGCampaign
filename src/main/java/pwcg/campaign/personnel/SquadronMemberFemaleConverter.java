package pwcg.campaign.personnel;

import java.util.HashMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.PilotNames;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.product.bos.country.BoSServiceManager;

public class SquadronMemberFemaleConverter
{
    public static SquadronMember possiblyConvertToFemale(ArmedService service, SquadronMember squadronMember, HashMap<String, String> namesUsed) throws PWCGException
    {
        if (service.getServiceId() != BoSServiceManager.VVS)
        {
            return squadronMember;
        }
        
        int femaleOdds = 3;
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < femaleOdds)
        {
            convertToFemaleName(squadronMember, namesUsed);
            convertToFemalePicture(service, squadronMember);
        }
        
        return squadronMember;
    }

    public static SquadronMember convertNightWitchesToFemale(Campaign campaign, ArmedService service, SquadronMember squadronMember) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronMember.getSquadronId());
        if (squadron != null)
        {
            if (squadron.getSquadronId() == 10111588)
            {
                convertToFemaleName(squadronMember,squadron.getNamesInUse(campaign));
                convertToFemalePicture(service, squadronMember);
            }
        }
        return squadronMember;
    }
    
    private static void convertToFemaleName(SquadronMember squadronMember, HashMap<String, String> namesUsed) throws PWCGException
    {
        String femaleName = PilotNames.getInstance().getFemaleName(namesUsed);
        squadronMember.setName(femaleName);
    }
    

    private static void convertToFemalePicture(ArmedService service, SquadronMember squadronMember) throws PWCGException
    {
        PilotPictureBuilder pilotPictureBuilder = new PilotPictureBuilder(service, new SquadronMembers());
        String picPath = pilotPictureBuilder.assignFemalePilotPicture();
        squadronMember.setPicName(picPath);        
    }
}
