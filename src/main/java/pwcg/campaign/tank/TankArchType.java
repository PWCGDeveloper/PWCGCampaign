package pwcg.campaign.tank;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class TankArchType
{
    private String tankArchTypeName = "";
    private Map<String, TankType> memberTankTypes = new HashMap<>();
    
    public TankArchType(String tankArchTypeName)
    {
        this.tankArchTypeName = tankArchTypeName;
    }
    
    public void addTankTypeToArchType(TankType memberTankType)
    {
        memberTankTypes.put(memberTankType.getType(), memberTankType);
    }

    public String getTankArchTypeName()
    {
        return tankArchTypeName;
    }

    public List<TankType> getAllMemberTankTypes()
    {
        return new ArrayList<>(memberTankTypes.values());
    }

    public List<TankType> getActiveMemberTankTypes(Date date) throws PWCGException
    {
        List<TankType> activeTankTypes = new ArrayList<>();
        for (TankType tankType : memberTankTypes.values())
        {
            if (DateUtils.isDateInRange(date, tankType.getIntroduction(), tankType.getWithdrawal()))
            {
                activeTankTypes.add(tankType);
            }
        }
        return activeTankTypes;
    }

    public List<TankType> getInProductionMemberTankTypes(Date date) throws PWCGException
    {
        List<TankType> activeTankTypes = new ArrayList<>();
        for (TankType tankType : memberTankTypes.values())
        {
            if (DateUtils.isDateInRange(date, tankType.getIntroduction(), tankType.getEndProduction()))
            {
                activeTankTypes.add(tankType);
            }
        }
        return activeTankTypes;
    }

    public static String getArchTypeDescription(String archType)
    {
        String archTypeDescription = archType;
        
        if (archType.equals("a20")) 
        {
            archTypeDescription = "A-20";
        }
        else if (archType.contains("b25")) 
        {
            archTypeDescription = "B-25";
        }
        else if (archType.contains("c47")) 
        {
            archTypeDescription = "C-47";
        }
        else if (archType.contains("bf109")) 
        {
            archTypeDescription = "Me-109";
        }
        else if (archType.contains("bf110")) 
        {
            archTypeDescription = "Me-110";
        }
        else if (archType.contains("fw190")) 
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
            archTypeDescription = "LaGG";
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
        else if (archType.contains("hurricane")) 
        {
            archTypeDescription = "Hurricane";
        }
        else if (archType.contains("spitfire")) 
        {
            archTypeDescription = "Spitfire";
        }
        else if (archType.contains("tempest")) 
        {
            archTypeDescription = "Tempest";
        }
        else if (archType.contains("typhoon")) 
        {
            archTypeDescription = "Typhoon";
        }
        else if (archType.contains("yak")) 
        {
            archTypeDescription = "Yak";
        }
        
        return archTypeDescription;
    }
}
