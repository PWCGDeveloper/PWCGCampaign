package pwcg.mission.mcu;

public enum McuIconLineType
{
    ICON_LINE_TYPE_NONE(0),
    ICON_LINE_TYPE_BORDER(2),
    ICON_LINE_TYPE_POSITION(13),
    ICON_LINE_TYPE_THIN(22);
	
	int iconLineTypeValue;

	McuIconLineType(int iconLineTypeValue) 
    {
        this.iconLineTypeValue = iconLineTypeValue;
    }

    public int getIconLineTypeValue() 
    {
        return iconLineTypeValue;
    }

}
