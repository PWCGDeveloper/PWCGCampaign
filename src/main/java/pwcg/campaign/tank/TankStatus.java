package pwcg.campaign.tank;

public class TankStatus
{
    public static final int NO_STATUS = 0;
    public static final int STATUS_DEPLOYED = 1;
    public static final int STATUS_DEPOT = 2;
    public static final int STATUS_DESTROYED = -1;
    public static final int STATUS_REMOVED_FROM_SERVICE = -2;

    public static final String DEPLOYED_STATUS_DESC = "Deployed to squadron";
    public static final String DEPOT_STATUS_DESC = "Available in depot";
    public static final String DESTROYED_STATUS_DESC = "Destroyed";
    public static final String REMOVED_FROM_SERVICE_DESC = "Removed from service";
    

    public static String crewMemberStatusToStatusDescription(int crewMemberStatus)
    {
        if (crewMemberStatus == STATUS_DEPOT)
        {
            return DEPOT_STATUS_DESC;
        }
        else if (crewMemberStatus == STATUS_DESTROYED)
        {
            return DESTROYED_STATUS_DESC;
        }
        else if (crewMemberStatus == STATUS_REMOVED_FROM_SERVICE)
        {
            return REMOVED_FROM_SERVICE_DESC;
        }
        else
        {
            return DEPLOYED_STATUS_DESC;
        }
    }

    public static int statusDescriptionToStatus(String campaignStatus)
    {
        if (campaignStatus.equals(DEPOT_STATUS_DESC))
        {
            return TankStatus.STATUS_DEPOT;
        }
        else if (campaignStatus.equals(DESTROYED_STATUS_DESC))
        {
            return TankStatus.STATUS_DESTROYED;
        }
        else if (campaignStatus.equals(REMOVED_FROM_SERVICE_DESC))
        {
            return TankStatus.STATUS_REMOVED_FROM_SERVICE;
        }
        else
        {
            return TankStatus.STATUS_DEPLOYED;
        }
    }
}
