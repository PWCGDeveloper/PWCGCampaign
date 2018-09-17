package pwcg.dev.utils;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pwcg.campaign.io.json.SkinIOJson;
import pwcg.campaign.skin.SkinTemplate;
import pwcg.campaign.skin.SkinTemplate.SkinTemplateInstance;
import pwcg.campaign.skin.SkinTemplateSet;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

public class SkinTemplateTester implements ActionListener {

    public static void main(String[] args) {
        SkinTemplateTester instance = new SkinTemplateTester();
        instance.initialize();
    }

    private Map<String, SkinTemplateSet> templateSets;
    private JComboBox<String> planeSelector;
    private JComboBox<String> templateSelector;
    private JTable parameterTable;
    private SkinTemplateInstance renderedTemplate;
    private SkinTemplate selectedTemplate;
    private JPanel previewPanel;

    public void initialize() {
        JFrame frame = new JFrame();

        GridBagConstraints c;

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(buttonPanel);

        JButton loadButton = new JButton();
        loadButton.setText("Load skin templates");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        buttonPanel.add(loadButton, c);

        planeSelector = new JComboBox<>();
        planeSelector.setActionCommand("plane_select");
        planeSelector.addActionListener(this);
        c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        buttonPanel.add(planeSelector, c);

        templateSelector = new JComboBox<>();
        templateSelector.setActionCommand("skin_select");
        templateSelector.addActionListener(this);
        c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        buttonPanel.add(templateSelector, c);

        DefaultTableModel model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        model.addColumn("Parameter");
        model.addColumn("Value");
        parameterTable = new JTable(model);

        JScrollPane parameterScrollPane = new JScrollPane(parameterTable);
        c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        buttonPanel.add(parameterScrollPane, c);

        JButton renderButton = new JButton("Render");
        renderButton.setActionCommand("render");
        renderButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        buttonPanel.add(renderButton, c);

        JButton writeButton = new JButton("Write");
        writeButton.setActionCommand("write");
        writeButton.addActionListener(this);
        c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 3;
        buttonPanel.add(writeButton, c);

        previewPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (renderedTemplate != null) {
                    int size = Math.min(getWidth(), getHeight());

                    g.drawImage(renderedTemplate.getColorImage(), (getWidth() - size) / 2, (getHeight() - size) / 2, size, size, null);
                }
            }
        };
        c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        buttonPanel.add(previewPanel, c);

        frame.pack();
        frame.setVisible(true);
        loadButton.doClick();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        switch(arg0.getActionCommand())
        {
            case "load":
                try {
                    templateSets = SkinIOJson.readSkinTemplateSet();

                    String oldPlane = (String) planeSelector.getSelectedItem();
                    planeSelector.removeAllItems();
                    for (String plane : new TreeSet<>(templateSets.keySet())) {
                        planeSelector.addItem(plane);
                    }
                    planeSelector.setSelectedItem(oldPlane);
                } catch (PWCGException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            case "plane_select":
                templateSelector.removeAllItems();
                SkinTemplateSet templateSet = templateSets.get(planeSelector.getSelectedItem());
                if (templateSet != null) {
                    for (String template : templateSet.getTemplates().keySet()) {
                        templateSelector.addItem(template);
                    }
                }
                break;

            case "skin_select":
                selectedTemplate = null;
                renderedTemplate = null;
                Map<String, Object> old_values = getParamValues();
                DefaultTableModel model = (DefaultTableModel)parameterTable.getModel();
                model.setRowCount(0);

                templateSet = templateSets.get(planeSelector.getSelectedItem());
                if (templateSet != null) {
                    selectedTemplate = templateSet.getTemplates().get(templateSelector.getSelectedItem());
                    if (selectedTemplate != null) {
                        for (String param : selectedTemplate.getParameters()) {
                            String value = "";
                            if (old_values.containsKey(param)) {
                                value = (String)old_values.get(param);
                            }
                            model.addRow(new String[] {param, value});
                        }
                    }
                }
                break;

            case "render":
                if (selectedTemplate != null)
                {
                    Map<String, Object> params = getParamValues();

                    renderedTemplate = selectedTemplate.instantiate(params);
                    try {
                        renderedTemplate.render();
                        previewPanel.repaint();
                    } catch (PWCGException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;

            case "write":
                if (renderedTemplate != null)
                {
                    try {
                        renderedTemplate.write();
                    } catch (PWCGIOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    private Map<String, Object> getParamValues()
    {
        Map<String, Object> res = new LinkedHashMap<>();
        for (int i = 0; i < parameterTable.getRowCount(); ++i) {
            String key = (String)parameterTable.getValueAt(i, 0);
            Object value = parameterTable.getValueAt(i, 1);
            if (value != null) {
                ParsePosition pos = new ParsePosition(0);
                Object parsedNum = NumberFormat.getInstance().parseObject((String)value, pos);
                if (parsedNum != null && pos.getIndex() == ((String)value).length()) {
                    value = parsedNum;
                }
            }
            res.put(key, value);
        }
        return res;
    }

}
