package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class PlaneArchType
{
    private String planeArchTypeName = "";
    private Map<String, PlaneType> memberPlaneTypes = new HashMap<>();
    
    public PlaneArchType(String planeArchTypeName)
    {
        this.planeArchTypeName = planeArchTypeName;
    }
    
    public void addPlaneTypeToArchType(PlaneType memberPlaneType)
    {
        memberPlaneTypes.put(memberPlaneType.getType(), memberPlaneType);
    }

    public String getPlaneArchTypeName()
    {
        return planeArchTypeName;
    }

    public List<PlaneType> getActiveMemberPlaneTypes(Date date) throws PWCGException
    {
        List<PlaneType> activePlaneTypes = new ArrayList<>();
        for (PlaneType planeType : memberPlaneTypes.values())
        {
            if (DateUtils.isDateInRange(date, planeType.getIntroduction(), planeType.getWithdrawal()))
            {
                activePlaneTypes.add(planeType);
            }
        }
        return activePlaneTypes;
    }   
}
