package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class McuTREntity extends BaseFlightMcu
{
    public static final int ENTITY_DISABLED = 0;
    public static final int ENTITY_ENABLED = 1;
    
    private int enabled = ENTITY_DISABLED;
    private int misObjID = 0;
    private ArrayList<McuMessage> onMessages = new ArrayList<>();
    private List<McuEvent> eventList = new ArrayList<>();

    public McuTREntity(int objectIndex)
    {
        super();
        misObjID = objectIndex;
    }

    public int getMisObjID()
    {
        return misObjID;
    }

    public ArrayList<McuMessage> getOnMessages()
    {
        return onMessages;
    }

    public void setOnMessages(int type, int cmdId, int targId)
    {
        McuMessage message = new McuMessage();
        message.setType(type);
        message.setCmdId(cmdId);
        message.setTarId(targId);

        onMessages.add(message);
    }

    public McuTREntity copy(int objectIndex)
    {
        McuTREntity newEntity = new McuTREntity(objectIndex);
        newEntity.enabled = this.enabled;
        newEntity.onMessages = new ArrayList<McuMessage>();
        newEntity.eventList = new ArrayList<McuEvent>();
        super.clone(newEntity);
        return newEntity;
    }

    public boolean isEnabled()
    {
        if (enabled == ENTITY_ENABLED)
        {
            return true;
        }
        return false;
    }

    public void enableEntity()
    {
        this.enabled = ENTITY_ENABLED;
    }

    public void disableEntity()
    {
        this.enabled = ENTITY_DISABLED;
    }

    public void addEvent(McuEvent event)
    {
        this.eventList.add(event);
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("MCU_TR_Entity");
            writer.newLine();
            writer.write("{");
            writer.newLine();
    
            super.write(writer);
    
            writer.write("  Enabled = " + enabled + ";");
            writer.newLine();
            writer.write("  MisObjID = " + misObjID + ";");
            writer.newLine();
            
            if (onMessages.size() > 0)
            {
                writeOnMessages(writer);
            }

            if (eventList.size() > 0)
            {
                writer.write("  OnEvents");
                writer.newLine();
                writer.write("  {");
                writer.newLine();
                for (McuEvent event : eventList)
                {
                    writer.write("      OnEvent");
                    writer.newLine();
                    writer.write("      {");
                    writer.newLine();
                    writer.write("          Type = " + event.getType() + ";");
                    writer.newLine();
                    writer.write("          TarId = " + event.getTarId() + ";");
                    writer.newLine();
                    writer.write("      }");
                    writer.newLine();
    
                }
                writer.write("  }");
                writer.newLine();
            }
    
            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    private void writeOnMessages(BufferedWriter writer) throws IOException, PWCGException
    {
        writer.write("  OnMessages");
        writer.newLine();
        writer.write("  {");
        writer.newLine();
        for (int i = 0; i < onMessages.size(); ++i)
        {
            McuMessage message = onMessages.get(i);
            message.write(writer);
        }
        writer.write("  }");
        writer.newLine();
    }

    public String toString()
    {
        StringBuffer output = new StringBuffer("");
        output.append("MCU_TR_Entity\n");
        output.append("{\n");

        output.append(super.toString());
        output.append("  Enabled = " + enabled + ";\n");
        output.append("  MisObjID = " + misObjID + ";\n");
        output.append("  OnMessages\n");
        output.append("  {\n");
        output.append("  }\n");

        output.append("}\n");
        output.append("\n");
        output.append("\n");

        return output.toString();

    }

    public void setEntityTarget(int linkTrId) 
    {
        super.setTarget(linkTrId);
    }
}
