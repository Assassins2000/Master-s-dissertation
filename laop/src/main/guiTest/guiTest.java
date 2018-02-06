import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class guiTest implements ActionListener {

    public guiTest() {

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

        Object[][] data = {
                {"ЗИ","ПЗИ",0.5, "ad"},
                {"ПИ","ПЗИ",0.5, "pes"},
        };

        JTable table = new JTable(new MyTableModel());

        JScrollPane jscrlp = new JScrollPane(table);

        jfrm.getContentPane().add(jscrlp);

        JButton btn = new JButton("Посчитать");

        jfrm.add(btn);
        jfrm.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String comStr = e.getActionCommand();

        switch (comStr) {
            case "Open": {
                break;
            }

            case "Close": {
                break;
            }
        }
    }

    public static void main(String args[]) {

        guiTest gui = new guiTest();

    }

    class MyTableModel extends AbstractTableModel {

        Object[][] rules;

        MyTableModel(Object[][] rules)
        {
            this.rules= rules;
        }

        MyTableModel() {
            this.rules= new Object[4][4];
        }

        @Override
        public int getRowCount() {
            return rules.length;
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex){
                case 0:
                    return rules[rowIndex][0];
                case 1:
                    return rules[rowIndex][1];
                case 2:
                    return rules[rowIndex][2];
                case 3:
                    return rules[rowIndex][3];
                    default:
                        return "";
            }
        }

        public String getColumnName(int c) {
            switch (c) {
                case 0:
                    return "Компонент";
                case 1:
                    return "Значение";
                default:
                    return "Other";
            }
        }
    }
}
