package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;

public class MultiSelectGUI implements ActionListener
{
    private JPanel selectionLayout = null;
    private JPanel selectionGrid = null;
    private Map<String, JCheckBox> checkBoxes = new TreeMap<>();
    private Map<String, MultiSelectData> selectDataSet = new TreeMap<>();

    public JPanel build() throws PWCGException  
    {
        selectionLayout = new JPanel(new BorderLayout());
        selectionLayout.setOpaque(false);
        
        JPanel buttonGroup = buildButtonPanel();
        selectionLayout.add(buttonGroup, BorderLayout.NORTH);
                
        selectionGrid = new JPanel(new GridLayout(0, 1));
        selectionGrid.setOpaque(false);
        
        JPanel selectionGridContainer = new JPanel(new BorderLayout());
        selectionGridContainer.setOpaque(false);
        selectionGridContainer.add(selectionGrid, BorderLayout.NORTH);
        selectionLayout.add(selectionGridContainer, BorderLayout.CENTER);

        return selectionLayout;
    }

    public void addSelection(MultiSelectData selectionData) throws PWCGException
    {
        JCheckBox selection = makeCheckBox(selectionData);
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
    
    public List<MultiSelectData> getSelected()
    {
        List<MultiSelectData> selected = new ArrayList<>();
        for (JCheckBox checkBox: checkBoxes.values())
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
        for (JCheckBox checkBox: checkBoxes.values())
        {
            selectionGrid.add(checkBox);
        }
        
        selectionGrid.revalidate();
        selectionGrid.repaint();
    }
    
    private JPanel buildButtonPanel() throws PWCGException
    {
        JButton selectAllButton = PWCGButtonFactory.makePaperButtonWithBorder("Select All", "SelectAll", this);
        JLabel spacer = PWCGButtonFactory.makePaperLabelMedium("         ");
        JButton deselectAllButton = PWCGButtonFactory.makePaperButtonWithBorder("Deselect All", "DeselectAll", this);
        JPanel buttonGroup = new JPanel(new GridLayout(0, 3));
        buttonGroup.setOpaque(false);
        buttonGroup.add(selectAllButton);
        buttonGroup.add(spacer);
        buttonGroup.add(deselectAllButton);
        return buttonGroup;
    }

    private JCheckBox makeCheckBox(MultiSelectData selectionData) throws PWCGException 
    {
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBox(selectionData.getText(), selectionData.getName(), Color.black);
        checkBox.setName(selectionData.getName());
        ToolTipManager.setToolTip(checkBox, selectionData.getInfo());

        checkBox.addActionListener(this);
        return checkBox;
    }
    
    public void setTextColor(String key, Color color)
    {
        JCheckBox selection = checkBoxes.get(key);
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
                for (JCheckBox checkBox: checkBoxes.values())
                {
                    checkBox.setSelected(true);
                }
            }
            else if (action.equalsIgnoreCase("DeselectAll"))
            {
                for (JCheckBox checkBox: checkBoxes.values())
                {
                    checkBox.setSelected(false);
                }
            }
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
}
