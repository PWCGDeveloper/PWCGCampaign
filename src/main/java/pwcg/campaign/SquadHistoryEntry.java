package pwcg.campaign;

public class SquadHistoryEntry
{
    public static final int NO_SQUADRON_SKILL_CHANGE = -1;
    private String date = "";
    private String squadName = "";
	private String armedServiceName = "";
	private int skill = NO_SQUADRON_SKILL_CHANGE;
	private String unitIdCode;
	private String subUnitIdCode;
    
	public String getDate()
    {
        return date;
    }
    
    public void setDate(String date)
    {
        this.date = date;
    }
    
    public String getSquadName()
    {
        return squadName;
    }
    
    public void setSquadName(String squadName)
    {
        this.squadName = squadName;
    }
    
    public String getArmedServiceName()
    {
        return armedServiceName;
    }
    
    public void setArmedServiceName(String armedServiceName)
    {
        this.armedServiceName = armedServiceName;
    }

    public int getSkill()
    {
        return skill;
    }

    public void setSkill(int skill)
    {
        this.skill = skill;
    }

    public String getUnitIdCode() {
        return unitIdCode;
    }

    public void setUnitIdCode(String unitIdCode) {
        this.unitIdCode = unitIdCode;
    }

    public String getSubUnitIdCode() {
        return subUnitIdCode;
    }

    public void setSubUnitIdCode(String subUnitIdCode) {
        this.subUnitIdCode = subUnitIdCode;
    }
}
