import core.Fact;
import core.Rule;
import sun.plugin2.message.Message;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;


public class guiTest implements ActionListener {

    JTable table;
    JTable table1;
    Object[][] massRule;
    Object[][] massFact;
    int keyFact;
    DefaultTableModel rtb;
    DefaultTableModel ftb;
    JLabel jlbFlag;
    JTextField component;
    JTextField value;
    JTextField component1;
    JTextField value1;
    JTextField subsystem;
    JTextField ratio;
    AbstractSystem as;

    public guiTest() {

        as = AbstractSystem.getInstance();

        JFrame jfrm = new JFrame("guiTest");
        jfrm.setLayout(new FlowLayout());
        jfrm.setSize(450, 400);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //-----------------------------------------------------
        JMenuBar jmb = new JMenuBar(); //создать строку меню
        //создать меню
        JMenu jmFile = new JMenu("Система");
        JMenuItem jmiOpen = new JMenuItem("Загрузить");
        JMenuItem jmiClose = new JMenuItem("Настроить");

        jmFile.add(jmiOpen);
        jmFile.add(jmiClose);

        jmb.add(jmFile);

        jmiOpen.addActionListener(this);
        jmiClose.addActionListener(this);

        jfrm.setJMenuBar(jmb);


        ftb = new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Компонент", "Значение", "ИД"
                }
        );
        table1 = new JTable(ftb);
        JScrollPane jscrlp1 = new JScrollPane(table1);

       table1.setPreferredScrollableViewportSize(new Dimension(400,200));

        jfrm.add(jscrlp1);

        JLabel jlb1= new JLabel("Компонент");
        JLabel jlb2= new JLabel("Значение");

        component = new JTextField(10);
        value = new JTextField(10);
        component.setAlignmentX(1);
        component.setEditable(false);
        value.setEditable(false);

        JButton btn = new JButton("Добавить факт");
        btn.addActionListener(e -> {
            try {
                keyFact = as.addFact(component.getText().toString(), Double.parseDouble(value.getText()));
                massFact = as.facttoObj();
                ftb.addRow(new String[]{component.getText().toString(), value.getText(), Integer.toString(keyFact)});
            }
            catch (Exception exp)
            {
                JOptionPane.showMessageDialog(jfrm,"Факт не задан");
            }

        });

        JButton btn1 = new JButton("Посчитать");
        JTextArea log= new JTextArea(100, 30);
        JLabel jlb= new JLabel("Ответ:");
        btn1.addActionListener(e -> {
            try{
                String logText= as.createBackup();
                jlb.setText(jlb.getText()+ Double.toString(as.getEfficiency()));
                as.deleteAll();
                table1.setModel(ftb);
                log.setText(log.getText()+ logText+' '+ jlb.getText());
            }
            catch (Exception exc)
            {
                JOptionPane.showMessageDialog(jfrm, "Факты не заданы");
            }
        });

        JButton btn2 = new JButton("Удалить");
        btn2.addActionListener(e -> {
            try {
                as.deleteFact(Integer.parseInt(table1.getModel().getValueAt(table1.getSelectedRow(),2).toString()));
                jlbFlag.setText(as.facttoObj().toString());
                ftb.removeRow(table1.getSelectedRow());
            }
            catch (Exception exp)
            {
                JOptionPane.showMessageDialog(jfrm,"Факт не выбран");
            }
        });

        jlbFlag = new JLabel("База знаний не загружена");

        jfrm.add(jlb1);
        jfrm.add(component);
        jfrm.add(jlb2);
        jfrm.add(value);
        jfrm.add(btn);
        jfrm.add(btn1);
        jfrm.add(btn2);
        jfrm.add(jlb);
        jfrm.add(jlbFlag);
        jfrm.add(log);
        jfrm.setVisible(true);
        jfrm.setResizable(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //String comStr = e.getActionCommand();
        switch (e.getActionCommand()) {
            case "Загрузить": {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    try {
                        massRule = as.importRuleBase(file.getAbsolutePath().toString());

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                    jlbFlag.setText("База знаний загружена");
                    component.setEditable(true);
                    value.setEditable(true);

//                    rtb.fireTableDataChanged();

                }
                break;
            }

            case "Настроить": {
                try{
                    new guiDK(massRule);
                }
                catch (Exception exc)
                {
                    JOptionPane.showMessageDialog(null,"База знаний не загружена");
                }
                break;
            }
        }
    }

    public static void main(String args[]) {

        guiTest gui = new guiTest();

    }

}
