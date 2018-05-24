package core;

/**
 * Класс служит для хранения правила в базе правил
 * Является потомком класса Fact
 * @author Balanovsky Danil
 */
public class Rule extends Fact {

    /**
     * Поле- хранит обозначение подсистемы, в которую входит компонент
     * Любой комопнент может быть подсистемой
     */
    private String  subsystem;

    /**
     * Поле- хранит тип функции объединения при каждом компоненте подсистемы
     * (1-аддитивный, 2-пессиматиыный, 3- мультипликативный)
     */
    private int funcRatio;

    private int funcEffect;

    private double criticalPoint;

    private boolean contribution;

    public double getPoint() {
        return criticalPoint;
    }

    /**
     * Конструктор- создание нового объекта
     * @param component обозначение компонента системы
     * @param subsystem обозначение подсистемы, в которую входит компонента
     * @param value значение убыли эффективности компонента системы
     * @param funcRatio тип объединения при каждом компоненте подсистемы (1-аддитивный, 2-пессиматиыный, 3- мультиплекативный)
     */
    public Rule(String component, String subsystem, double value, int funcRatio, int funcEffect, double point) {
        super(component, value);//вызов конструктора предка
        this.subsystem = subsystem;
        this.funcRatio = funcRatio;
        this.funcEffect= funcEffect;
        this.criticalPoint=point;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public int getFuncRatio() {
        return funcRatio;
    }

    public void setFuncRatio(int funcRatio) {
        this.funcRatio = funcRatio;
    }

    public int getFuncEffect() {
        return funcEffect;
    }

    public void setFuncEffect(int funcEffect) {
        this.funcEffect = funcEffect;
    }

    public boolean isContribution() {
        return contribution;
    }

    public void setContribution(boolean contribution) {
        this.contribution = contribution;
    }
    public void setPoint(double point) {
        this.criticalPoint= criticalPoint;
    }
}
