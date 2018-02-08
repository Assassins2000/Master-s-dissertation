import core.Fact;
import core.Rule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;


public class guiTest implements ActionListener {

    JTable table;
    JTable table1;
    Hashtable<Integer, Rule> massRule;
    Hashtable<Integer, Fact> massFact;
    RuleTableModel rtb;
    FactTableModel ftb;
    JLabel jlbFlag;
    JTextField component;
    JTextField value;
    AbstractSystem as;

    public guiTest() {

        as = AbstractSystem.getInstance();

        JFrame jfrm = new JFrame("guiTest");
        jfrm.setLayout(new FlowLayout());
        jfrm.setSize(200, 200);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //-----------------------------------------------------
        JMenuBar jmb = new JMenuBar(); //создать строку меню
        //создать меню
        JMenu jmFile = new JMenu("File");
        JMenuItem jmiOpen = new JMenuItem("Open");
        JMenuItem jmiClose = new JMenuItem("Close");

        jmFile.add(jmiOpen);
        jmFile.add(jmiClose);

        jmb.add(jmFile);

        jmiOpen.addActionListener(this);
        jmiClose.addActionListener(this);

        jfrm.setJMenuBar(jmb);
        //-----------------------------------------------------
        massRule = new Hashtable<>();
        rtb = new RuleTableModel(massRule);
        table = new JTable(rtb);
        JScrollPane jscrlp = new JScrollPane(table);
        jfrm.getContentPane().add(jscrlp);
        //-----------------------------------------------------
        massFact = new Hashtable<>();
        ftb = new FactTableModel(massFact);
        table1 = new JTable(ftb);
        JScrollPane jscrlp1 = new JScrollPane(table1);
        jfrm.getContentPane().add(jscrlp1);

        component= new JTextField(10);
        value = new JTextField(10);
        component.setAlignmentX(1);

        JButton btn = new JButton("Добавить факт");
        btn.addActionListener(e -> {
            int key=as.addFact(component.getText().toString(), Double.parseDouble(value.getText()));
            massFact.put(key, new Fact(component.getText().toString(), Double.parseDouble(value.getText())));
            ftb.fireTableDataChanged();

        });

        JButton btn1= new JButton("Посчитать");
        btn1.addActionListener(e -> jlbFlag.setText(Double.toString(as.getEfficiency())));

        jlbFlag = new JLabel("База знаний не загружена");


        jfrm.add(component);
        jfrm.add(value);
        jfrm.add(jlbFlag);
        jfrm.add(btn);
        jfrm.add(btn1);
        jfrm.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String comStr = e.getActionCommand();
        switch (comStr) {
            case "Open": {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    try {
                        Hashtable<Integer, Rule> mass1 = as.importRuleBase(file.getAbsolutePath().toString());
                        for (int key : mass1.keySet()) {
                            massRule.put(key, mass1.get(key));
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                    jlbFlag.setText("База знаний загружена");
                    rtb.fireTableDataChanged();

                }
                break;
            }

            case "Close": {

//                JFrame jfrm = new JFrame("DB");
//                jfrm.setLayout(new FlowLayout());
//
//                jfrm.setSize(200, 200);
//                jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//                rtb= new MyTableModel(massRule);
//                table = new JTable(rtb);
//                //  JScrollPane jscrlp = new JScrollPane(table);
//
//                jfrm.add(table);
//
//                jfrm.setVisible(true);


                break;
            }
        }
    }

    public static void main(String args[]) {

        guiTest gui = new guiTest();

    }

}
