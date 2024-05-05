package pwcg.campaign;

public class CampaignNameValidator
{

    public static boolean validateCampaignName(String campaignName)
    {
        if (campaignName == null || campaignName.length() < 5 || !startsWithALetter(campaignName)) 
        {
            return false;
        }
        
        return true;
    }

    private static boolean startsWithALetter(String string)
    {
        char startsWith = string.charAt(0);
        if (startsWith < 'A' || startsWith > 'Z') 
        {
            if (startsWith < 'a' || startsWith > 'z') 
            {
                return false;
            }
        }
        return true;
    }

}
