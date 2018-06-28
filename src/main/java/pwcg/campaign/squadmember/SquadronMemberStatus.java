package pwcg.campaign.squadmember;

public class SquadronMemberStatus
{
    public static final int STATUS_ACTIVE = 0;
    public static final int STATUS_ON_LEAVE = -1;
    public static final int STATUS_TRANSFERRED = -2;
    public static final int STATUS_WOUNDED = -3;
    public static final int STATUS_SERIOUSLY_WOUNDED = -4;
    public static final int STATUS_CAPTURED = -5;
    public static final int STATUS_KIA = -6;

    public static String ACTIVE_STATUS = "Active";
    public static String ON_LEAVE_STATUS = "On Leave";
    public static String TRANSFERRED_STATUS = "Transferred";
    public static String WOUNDED_STATUS = "Wounded";
    public static String SERIOUSLY_WOUNDED_STATUS = "Seriously Wounded";
    public static String CAPTURED_STATUS = "Captured";
    public static String KIA_STATUS = "Killed in Action";
    

    public static String pilotStatusToStatusDescription(int pilotStatus)
    {
        if (pilotStatus == STATUS_ACTIVE)
        {
            return ACTIVE_STATUS;
        }
        else if (pilotStatus == STATUS_ON_LEAVE)
        {
            return ACTIVE_STATUS;
        }
        else if (pilotStatus == STATUS_TRANSFERRED)
        {
            return TRANSFERRED_STATUS;
        }
        else if (pilotStatus == STATUS_WOUNDED)
        {
            return WOUNDED_STATUS;
        }
        else if (pilotStatus == STATUS_SERIOUSLY_WOUNDED)
        {
            return SERIOUSLY_WOUNDED_STATUS;
        }
        else if (pilotStatus == STATUS_CAPTURED)
        {
            return CAPTURED_STATUS;
        }
        else if (pilotStatus == STATUS_KIA)
        {
            return KIA_STATUS;
        }
        else
        {
            return ACTIVE_STATUS;
        }
    }

    public static int statusDescriptionToStatus(String campaignStatus)
    {
        if (campaignStatus.equals(ACTIVE_STATUS))
        {
            return SquadronMemberStatus.STATUS_ACTIVE;
        }
        else if (campaignStatus.equals(WOUNDED_STATUS))
        {
            return SquadronMemberStatus.STATUS_WOUNDED;
        }
        else if (campaignStatus.equals(SERIOUSLY_WOUNDED_STATUS))
        {
            return SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED;
        }
        else if (campaignStatus.equals(CAPTURED_STATUS))
        {
            return SquadronMemberStatus.STATUS_CAPTURED;
        }
        else if (campaignStatus.equals(KIA_STATUS))
        {
            return SquadronMemberStatus.STATUS_KIA;
        }
        else
        {
            return SquadronMemberStatus.STATUS_ACTIVE;
        }
    }
}
