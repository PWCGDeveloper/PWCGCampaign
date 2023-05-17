package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class BaseFlightMcu extends BaseMcu 
{	
	protected HashSet<String> targets = new HashSet<String>();
	protected HashSet<String> objects = new HashSet<String>();
	
	public BaseFlightMcu()
	{
	    super();
	}
	
	public BaseFlightMcu clone (BaseFlightMcu clone)
	{
	    super.clone(clone);
	    
        clone.targets = new HashSet<String>();
        clone.targets.addAll(this.targets);

        clone.objects = new HashSet<String>();
        clone.objects.addAll(this.objects);
	        
        return clone;
	}
	
	public void write(BufferedWriter writer) throws PWCGException
	{
        try
        {
    		StringBuffer targetBuffer = new StringBuffer("");
    		List<String> targetList = getTargets();
    		
    		// Randomize the target list in an effort to get the AI not to follow one plane.
    		// Probably won't work, but it's an attempt.
    		Collections.shuffle(targetList);
    		for (int i = 0; i < targetList.size(); ++i)
    		{
                if (i > 0)
                {
                    targetBuffer.append(",");               
                }
    			targetBuffer.append(targetList.get(i));
    		}
    		
    		StringBuffer objectBuffer = new StringBuffer("");
            List<String> objectList = getObjects();
    		for (int i = 0; i < objectList.size(); ++i)
    		{
                if (i > 0)
                {
                    objectBuffer.append(",");               
                }
    			objectBuffer.append(objectList.get(i));
    		}
    		
    		writer.write("  Index = " + index + ";");
    		writer.newLine();
    		writer.write("  Name = \"" + name + "\";");
    		writer.newLine();
    		writer.write("  Desc = \"" +  desc + "\";");
    		writer.newLine();
    		writer.write("  Targets = [" +  targetBuffer.toString() + "];");
    		writer.newLine();
    		writer.write("  Objects = [" +  objectBuffer.toString() + "];");
    		writer.newLine();
    		
    		position.write(writer);
    		orientation.write(writer);		
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}
	
	public String toString()
	{
		StringBuffer output = new StringBuffer("");
		output.append("  Index = " + index + ";\n");
		output.append("  Name = \"" + name + "\";");
		output.append("  Desc = \"" +  desc + "\";\n");
		output.append("  Targets = [" +  "" + "];\n");
		output.append("  Objects = " + "" + ";\n");
		output.append("  XPos = " + position.getXPos() + ";\n");
		output.append("  YPos = " + position.getYPos() + ";\n");
		output.append("  ZPos = " + position.getZPos() + ";\n");
		output.append("  XOri = " + orientation.getxOri() + ";\n");
		output.append("  YOri = " + orientation.getyOri() + ";\n");
		output.append("  ZOri = " + orientation.getzOri() + ";\n");
		
		return output.toString();

	}
	
	public List<String> getTargets() 
	{
	    List <String> targetList = new ArrayList<String>();
	    targetList.addAll(targets);
	    
		return targetList;
	}
    
    public boolean isTarget(String searchFor) 
    {
        for (String target : targets)
        {
            if (target.equals(searchFor))
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean isObject(String searchFor) 
    {
        for (String obj : objects)
        {
            if (obj.equals(searchFor))
            {
                return true;
            }
        }
        return false;
    }
	
	protected void setTarget(int target) 
	{
		String targetStr = Integer.valueOf(target).toString();
		targets.add(targetStr);
	}

	public List<String> getObjects() 
	{
        List <String> objectList = new ArrayList<String>();
        objectList.addAll(objects);
        
		return objectList;
	}

	public void setObject(int object) 
	{
		String objectStr = Integer.valueOf(object).toString();
		objects.add(objectStr);
	}
	
	public void clearTargets()
	{
		targets.clear();
	}
	
	public void clearObjects()
	{
		objects.clear();
	}

    /**
     * Goal is used only by RoF.  Pull the decision to write into the base class.
     * 
     * @param writer
     * @param goalType
     * @throws IOException
     */
    protected void writeMCUGoal(BufferedWriter writer, int goalType) throws IOException
    {
        // Ugly but I don't want to override for one line of code
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        if (productSpecificConfiguration.useWaypointGoal())
        {
            writer.write("  GoalType = " + goalType + ";");
            writer.newLine();
        }
    }
    

}
