package pwcg.mission.mcu;

public enum McuIconIdType
{
    ICON_ID_NORMAL(0),
    ICON_ID_ENEMY_BALLOON(506),
	ICON_ID_WAYPOINT(901),
	ICON_ID_ACTION_POINT(902),
	ICON_ID_TAKEOFF(903),
	ICON_ID_LAND(904),
    ICON_ID_AIRFIELD(905);

	int iconLineIdValue;

	McuIconIdType(int iconLineIdValue) 
    {
        this.iconLineIdValue = iconLineIdValue;
    }

    public int getIconLineIdValue() 
    {
        return iconLineIdValue;
    }
}
