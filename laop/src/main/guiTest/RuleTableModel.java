import core.Rule;

import javax.swing.table.AbstractTableModel;
import java.util.Hashtable;

public class RuleTableModel extends AbstractTableModel {

    Hashtable<Integer, Rule> rules;

    public RuleTableModel(Hashtable<Integer, Rule> rules) {
        super();
        this.rules = rules;
    }

    public RuleTableModel() {
        super();
        this.rules = new Hashtable<>();
    }

    @Override
    public int getRowCount() {
        return rules.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return rules.get(rowIndex + 1).getComponent();
            case 1:
                return rules.get(rowIndex + 1).getSubsystem();
            case 2:
                return rules.get(rowIndex + 1).getValue();
            case 3:
                return rules.get(rowIndex + 1).getRatio();
            default:
                return "";
        }
    }

    public String getColumnName(int c) {
        switch (c) {
            case 0:
                return "Компонент";
            case 1:
                return "Подсистема";
            case 2:
                return "Значение";
            case 3:
                return "Отношение";
            default:
                return "Other";
        }
    }
}

