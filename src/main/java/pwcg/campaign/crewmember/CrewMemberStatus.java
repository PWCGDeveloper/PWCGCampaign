package pwcg.campaign.crewmember;

public class CrewMemberStatus
{
    public static final int STATUS_ACTIVE = 0;
    public static final int STATUS_ON_LEAVE = -1;
    public static final int STATUS_TRANSFERRED = -2;
    public static final int STATUS_WOUNDED = -3;
    public static final int STATUS_SERIOUSLY_WOUNDED = -4;
    public static final int STATUS_CAPTURED = -5;
    public static final int STATUS_KIA = -6;
    public static final int STATUS_RETIRED = -7;

    public static String ACTIVE_STATUS = "Active";
    public static String ON_LEAVE_STATUS = "On Leave";
    public static String TRANSFERRED_STATUS = "Transferred";
    public static String WOUNDED_STATUS = "Wounded";
    public static String SERIOUSLY_WOUNDED_STATUS = "Seriously Wounded";
    public static String CAPTURED_STATUS = "Captured";
    public static String KIA_STATUS = "Killed in Action";
    public static String RETIRED_STATUS = "Retired";
    

    public static String crewMemberStatusToStatusDescription(int crewMemberStatus)
    {
        if (crewMemberStatus == STATUS_ACTIVE)
        {
            return ACTIVE_STATUS;
        }
        else if (crewMemberStatus == STATUS_ON_LEAVE)
        {
            return ACTIVE_STATUS;
        }
        else if (crewMemberStatus == STATUS_TRANSFERRED)
        {
            return TRANSFERRED_STATUS;
        }
        else if (crewMemberStatus == STATUS_WOUNDED)
        {
            return WOUNDED_STATUS;
        }
        else if (crewMemberStatus == STATUS_SERIOUSLY_WOUNDED)
        {
            return SERIOUSLY_WOUNDED_STATUS;
        }
        else if (crewMemberStatus == STATUS_CAPTURED)
        {
            return CAPTURED_STATUS;
        }
        else if (crewMemberStatus == STATUS_KIA)
        {
            return KIA_STATUS;
        }
        else if (crewMemberStatus == STATUS_RETIRED)
        {
            return RETIRED_STATUS;
        }
        else
        {
            return "";
        }
    }

    public static int statusDescriptionToStatus(String campaignStatus)
    {
        if (campaignStatus.equals(ACTIVE_STATUS))
        {
            return CrewMemberStatus.STATUS_ACTIVE;
        }
        else if (campaignStatus.equals(WOUNDED_STATUS))
        {
            return CrewMemberStatus.STATUS_WOUNDED;
        }
        else if (campaignStatus.equals(SERIOUSLY_WOUNDED_STATUS))
        {
            return CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED;
        }
        else if (campaignStatus.equals(CAPTURED_STATUS))
        {
            return CrewMemberStatus.STATUS_CAPTURED;
        }
        else if (campaignStatus.equals(KIA_STATUS))
        {
            return CrewMemberStatus.STATUS_KIA;
        }
        else
        {
            return CrewMemberStatus.STATUS_ACTIVE;
        }
    }
}
