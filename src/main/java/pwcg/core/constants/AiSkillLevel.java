package pwcg.core.constants;

public enum AiSkillLevel 
{
	PLAYER(0),
	NOVICE(1),
	COMMON(2),
	VETERAN(3),
	ACE(4);
	
	private Integer aiSkillLevel;
	
	public static AiSkillLevel createAiSkilLLevel(int levelValue)
	{
		if (levelValue == 0)
		{
			return PLAYER;
		}
		else if (levelValue == 1)
		{
			return NOVICE;
		}
		else if (levelValue == 2)
		{
			return COMMON;
		}
		else if (levelValue == 3)
		{
			return VETERAN;
		}
		else if (levelValue == 4)
		{
			return ACE;
		}
		
		return NOVICE;
	}

	
    private AiSkillLevel(final Integer aiSkillLevel) 
    {
        this.aiSkillLevel = aiSkillLevel;
    }

    public Integer getAiSkillLevel() 
    {
        return aiSkillLevel;
    }
    
    public boolean equals(AiSkillLevel aiSkill)
    {
    	return (this.aiSkillLevel == aiSkill.getAiSkillLevel());
    }

    public boolean greaterThan(AiSkillLevel aiSkill)
    {
    	return (this.aiSkillLevel > aiSkill.getAiSkillLevel());
    }
    
    public boolean lessThan(AiSkillLevel aiSkill)
    {
    	return (this.aiSkillLevel < aiSkill.getAiSkillLevel());
    }

}
