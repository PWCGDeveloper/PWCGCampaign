package pwcg.campaign.target;

import pwcg.core.exception.PWCGException;

public interface ITargetDefinitionBuilder
{
    public TargetDefinition buildTargetDefinition () throws PWCGException;
}
