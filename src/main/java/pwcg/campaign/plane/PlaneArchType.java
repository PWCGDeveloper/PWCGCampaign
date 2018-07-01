package pwcg.campaign.plane;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, PlaneType> getMemberPlaneTypes()
    {
        return memberPlaneTypes;
    }   
}
