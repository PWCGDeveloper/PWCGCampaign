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

    public List<PlaneType> getInProductionMemberPlaneTypes(Date date) throws PWCGException
    {
        List<PlaneType> activePlaneTypes = new ArrayList<>();
        for (PlaneType planeType : memberPlaneTypes.values())
        {
            if (DateUtils.isDateInRange(date, planeType.getIntroduction(), planeType.getEndProduction()))
            {
                activePlaneTypes.add(planeType);
            }
        }
        return activePlaneTypes;
    }

    public static String getArchTypeDescription(String archType)
    {
        String archTypeDescription = archType;
        
        if (archType.equals("a20")) 
        {
            archTypeDescription = "A-20";
        }
        else if (archType.equals("b25")) 
        {
            archTypeDescription = "B-25";
        }
        else if (archType.contains("bf109")) 
        {
            archTypeDescription = "Me-109";
        }
        else if (archType.contains("bf110")) 
        {
            archTypeDescription = "Me-110";
        }
        else if (archType.contains("fw109")) 
        {
            archTypeDescription = "FW-190";
        }
        else if (archType.contains("he111")) 
        {
            archTypeDescription = "He-111";
        }
        else if (archType.contains("hs129")) 
        {
            archTypeDescription = "Hs-129";
        }
        else if (archType.contains("i16")) 
        {
            archTypeDescription = "I-16";
        }
        else if (archType.contains("il2")) 
        {
            archTypeDescription = "IL-2";
        }
        else if (archType.contains("ju52")) 
        {
            archTypeDescription = "Ju-52";
        }
        else if (archType.contains("ju87")) 
        {
            archTypeDescription = "Ju-87";
        }
        else if (archType.contains("ju88")) 
        {
            archTypeDescription = "Ju-88";
        }
        else if (archType.contains("lagg")) 
        {
            archTypeDescription = "LaaG";
        }
        else if (archType.contains("mc200")) 
        {
            archTypeDescription = "MC-200";
        }
        else if (archType.contains("mig3")) 
        {
            archTypeDescription = "MiG-3";
        }
        else if (archType.contains("p38")) 
        {
            archTypeDescription = "P-38";
        }
        else if (archType.contains("p39")) 
        {
            archTypeDescription = "P-39";
        }
        else if (archType.contains("p40")) 
        {
            archTypeDescription = "P-40";
        }
        else if (archType.contains("p47")) 
        {
            archTypeDescription = "P-47";
        }
        else if (archType.contains("p51")) 
        {
            archTypeDescription = "P-51";
        }
        else if (archType.contains("pe2")) 
        {
            archTypeDescription = "Pe-2";
        }
        else if (archType.contains("spitfire")) 
        {
            archTypeDescription = "Spitfire";
        }
        else if (archType.contains("tempest")) 
        {
            archTypeDescription = "Tempest";
        }
        else if (archType.contains("yak")) 
        {
            archTypeDescription = "Yak";
        }
        
        return archTypeDescription;
    }
}
