package core;
/**
 * Класс служит для хранения факта в базе фактов
 * Является потомком класса Component
 * @author Balanovsky Danil
 */
public class Fact extends Component {
    /**
     * Поле- хранит значение убыли эффективности компонента системы
     */
    private double fallValue;
    private double ruleValue;
    private boolean unificationFlag;


    /**
     * Конструктор- создание нового объекта
     * @param component обоначение компонента системы
     * @param fallValue значение убыли эффективности компонента системы
     */
    public Fact(String component, double fallValue) {
        super(component);//вызов конуструктора предка
        this.unificationFlag=false;
        this.fallValue = fallValue;

    }

    public double getFallValue() {
        return fallValue;
    }

    public void setFallValue(double fallValue) {
        this.fallValue = fallValue;
    }

    public double getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(double ruleValue) {
        this.ruleValue = ruleValue;
    }

    public boolean isUnificationFlag() {
        return unificationFlag;
    }

    public void setUnificationFlag(boolean unificationFlag) {
        this.unificationFlag = unificationFlag;
    }
}
