package pwcg.aar.data;

public enum ExtendedTimeReason
{
    NO_EXTENDED_TIME,
    NO_PILOTS,
    NO_EQUIPMENT,
    WOUND,
    TRANSFER,
    LEAVE;
    
    public static String reasonForExtendedTmeText(ExtendedTimeReason extendedTimeReason)
    {
        String reasonForExtendedTime = "";
        if (extendedTimeReason == NO_PILOTS)
        {
            reasonForExtendedTime = "Squadron deactivated due to lack of personnel";
        }
        else if (extendedTimeReason == NO_EQUIPMENT)
        {
            reasonForExtendedTime = "Squadron deactivated due to lack of aircraft";
        }
        else if (extendedTimeReason == WOUND)
        {
            reasonForExtendedTime = "Pilot our of action due to wound";
        }
        else if (extendedTimeReason == TRANSFER)
        {
            reasonForExtendedTime = "Pilot our of action due to transfer";
        }
        else if (extendedTimeReason == LEAVE)
        {
            reasonForExtendedTime = "Pilot our of action due to leave";
        }
        
        return reasonForExtendedTime;
    }
}
