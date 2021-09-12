package pwcg.gui.utils;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.control.Button;
import javax.swing.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;

public class MultiSelectGUI implements ActionListener
{
    private Pane selectionLayout = null;
    private Pane selectionGrid = null;
    private Map<String, CheckBox> checkBoxes = new TreeMap<>();
    private Map<String, MultiSelectData> selectDataSet = new TreeMap<>();

    public Pane build(int numColumns) throws PWCGException  
    {
        selectionLayout = new Pane(new BorderLayout());
        selectionLayout.setOpaque(false);
        
        Pane buttonGroup = buildButtonPanel();
        selectionLayout.add(buttonGroup, BorderLayout.NORTH);
                
        selectionGrid = new Pane(new GridLayout(0, numColumns));
        selectionGrid.setOpaque(false);
        
        Pane selectionGridContainer = new Pane(new BorderLayout());
        selectionGridContainer.setOpaque(false);
        selectionGridContainer.add(selectionGrid, BorderLayout.NORTH);
        selectionLayout.add(selectionGridContainer, BorderLayout.CENTER);

        return selectionLayout;
    }

    public void addSelection(MultiSelectData selectionData) throws PWCGException
    {
        CheckBox selection = makeCheckBox(selectionData);
        checkBoxes.put(selectionData.getName(), selection);
        selectDataSet.put(selectionData.getName(), selectionData);
        buildCheckBoxGrid();
    }

    public void removeSelection(String selectionName) throws PWCGException
    {
        if (checkBoxes.containsKey(selectionName))
        {
            checkBoxes.remove(selectionName);
            selectDataSet.remove(selectionName);
            buildCheckBoxGrid();
        }
    }

    public void clear() throws PWCGException
    { 
        checkBoxes = new TreeMap<>();
        selectDataSet = new TreeMap<>();
        buildCheckBoxGrid();
    }
    
    public List<MultiSelectData> getSelected()
    {
        List<MultiSelectData> selected = new ArrayList<>();
        for (CheckBox checkBox: checkBoxes.values())
        {
            if (checkBox.isSelected())
            {
                MultiSelectData selectData = selectDataSet.get(checkBox.getName());
                selected.add(selectData);
            }
        }
        return selected;
    }
    
    public List<MultiSelectData> getAll()
    {
        List<MultiSelectData> selected = new ArrayList<>();
        for (MultiSelectData selectData: selectDataSet.values())
        {
            selected.add(selectData);
        }
        return selected;
    }

    private void buildCheckBoxGrid()
    {
        selectionGrid.removeAll();
        for (CheckBox checkBox: checkBoxes.values())
        {
            selectionGrid.add(checkBox);
        }
        
        selectionGrid.revalidate();
        selectionGrid.repaint();
    }
    
    private Pane buildButtonPanel() throws PWCGException
    {
        Label spacer1 = ButtonFactory.makePaperLabelMedium("         ");
        Button selectAllButton = ButtonFactory.makePaperButtonWithBorder("Select All", "SelectAll", this);
        selectAllButton.setOpaque(false);
        Label spacer2 = ButtonFactory.makePaperLabelMedium("         ");
        Button deselectAllButton = ButtonFactory.makePaperButtonWithBorder("Deselect All", "DeselectAll", this);
        deselectAllButton.setOpaque(false);
        Label spacer3 = ButtonFactory.makePaperLabelMedium("         ");
        
        Pane buttonGroup = new Pane(new GridLayout(0, 5));
        buttonGroup.setOpaque(false);
        buttonGroup.add(spacer1);
        buttonGroup.add(selectAllButton);
        buttonGroup.add(spacer2);
        buttonGroup.add(deselectAllButton);
        buttonGroup.add(spacer3);
        return buttonGroup;
    }

    private CheckBox makeCheckBox(MultiSelectData selectionData) throws PWCGException 
    {
        CheckBox checkBox = ButtonFactory.makeCheckBox(selectionData.getText(), selectionData.getName(), Color.black, this);
        checkBox.setName(selectionData.getName());
        ToolTipManager.setToolTip(checkBox, selectionData.getInfo());
        return checkBox;
    }
    
    public void setTextColor(String key, Color color)
    {
        CheckBox selection = checkBoxes.get(key);
        selection.setForeground(color);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("SelectAll"))
            {
                for (CheckBox checkBox: checkBoxes.values())
                {
                    checkBox.setSelected(true);
                }
            }
            else if (action.equalsIgnoreCase("DeselectAll"))
            {
                for (CheckBox checkBox: checkBoxes.values())
                {
                    checkBox.setSelected(false);
                }
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}
