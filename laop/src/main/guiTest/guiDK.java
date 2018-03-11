import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class guiDK {

    DefaultTableModel rtb;
    Object[][]massRule;
    JTable table;
    JTextField component;
    JTextField subsystem;
    JTextField value;
    JTextField ratio;
    AbstractSystem as;
    int keyRule;

    public guiDK(Object[][] massRule){

        if(massRule==null){
            throw new RuntimeException();
        }
        as= AbstractSystem.getInstance();

        JFrame jfrm = new JFrame("guiTest");
        jfrm.setLayout(new FlowLayout());
        jfrm.setSize(450, 400);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ////////
        rtb = new DefaultTableModel(
                massRule,
                new String[]{
                        "Компонент", "Подсистема", "Значение", "Отношение", "ИД"
                }
        );
        table = new JTable(rtb);
        JScrollPane jscrlp1 = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(400,200));

        jfrm.add(jscrlp1);

        JLabel jlb1= new JLabel("Компонент");
        JLabel jlb2= new JLabel("Подсистема");
        JLabel jlb3= new JLabel("Значение");
        JLabel jlb4= new JLabel("Отношение");

        component = new JTextField(10);
        component.setAlignmentX(1);
        value = new JTextField(10);
        subsystem= new JTextField(10);
        value= new JTextField(10);
        ratio = new JTextField(10);

        JButton btn1= new JButton("Добавить правило");

        btn1.addActionListener(e -> {

            try {
                keyRule= as.addRule(component.getText(), subsystem.getText(), Double.parseDouble(value.getText()), ratio.getText());
                rtb.addRow(new String[]{component.getText(), subsystem.getText(), value.getText(), ratio.getText(), Integer.toString(keyRule)});
            }
            catch (Exception exp)
            {
                JOptionPane.showMessageDialog(jfrm,"Правило не задано");
            }
        });

        JButton btn2 = new JButton("Удалить правило");
        btn2.addActionListener(e->{

            try {
                as.deleteRule(Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(),4).toString()));
                System.out.println(as.ruletoObj().length);
                rtb.removeRow(table.getSelectedRow());
            }
            catch (Exception exp)
            {
                JOptionPane.showMessageDialog(jfrm,"Правило не выбрано");
            }
        });





        jfrm.add(jlb1);
        jfrm.add(component);
        jfrm.add(jlb2);
        jfrm.add(subsystem);
        jfrm.add(jlb3);
        jfrm.add(value);
        jfrm.add(jlb4);
        jfrm.add(ratio);
        jfrm.add(btn1);
        jfrm.add(btn2);

        jfrm.setVisible(true);


    }
}
