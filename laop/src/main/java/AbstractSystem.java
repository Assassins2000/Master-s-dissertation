import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import core.*;
import org.jetbrains.annotations.Contract;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Класс служит для создания проивзольной системы
 * Реализует паттерн Singleton
 *
 * @author Balanovsky Danil
 */
public class AbstractSystem {

    private static AbstractSystem ourInstance = new AbstractSystem();

    private AbstractSystem() {
        this.fb = FactsBase.getInstance();
        this.rb = RulesBase.getInstance();
        // this.fbBackup= new Hashtable<>();
    }

    @Contract(pure = true)
    public static AbstractSystem getInstance() {
        return ourInstance;
    }

   // private Hashtable<Integer, Fact> fbBackup;
    /**
     * Поле- обеспечивает доступ к базе фактов
     */
    private FactsBase fb;

    /**
     * Поле- обеспечивает доступ к базе правил
     */
    private RulesBase rb;


    /**
     * Метод создает и добавляет факт в базу фактов
     *
     * @param component обозначение компонента
     * @param value     падение эффективности
     * @return ключ созданного факта
     */
    public int addFact(String component, double value) {
        return fb.addFact(new Fact(component, value));
    }


    /**
     * Метод созлает и добавляет правило в базу правил
     *
     * @param component обозначение комопнентп
     * @param subsystem обозначение подсистемы
     * @param value     значение убыли эффективности компонента системы
     * @param ratio     тип объединения при каждом компоненте подсистемы
     * @return возвращает ключ созданного правила
     */
    public int addRule(String component, String subsystem, double value, int ratio, boolean contribution, int funF, int point) {
        return rb.addRule(new Rule(component, subsystem, value, ratio, funF, point));
    }

    public Object[][] deleteFact(int key){
        fb.getFacts().remove(key);
        return facttoObj();
    }
    public Object[][] deleteRule(int key){
        rb.getRules().remove(key);
        return ruletoObj();
    }

    public Object[][] facttoObj(){
        Set<Integer> keys = fb.getKeys();
        int keysSize= keys.size();
        Object[][] objMass= new Object[keysSize][3];
        keysSize--;
        for (int key : keys){
            objMass[keysSize][0]= fb.getFact(key).getComponent();
            objMass[keysSize][1]= fb.getFact(key).getFallValue();
            objMass[keysSize][2]= key;
            keysSize--;
        }
        return objMass;
    }
    public Object[][] ruletoObj(){
        Set<Integer> keys = rb.getKeys();
        int keysSize= keys.size();
        Object[][] objMass= new Object[keysSize][6];
        keysSize--;
        for (int key : keys){
            objMass[keysSize][0]= rb.getRule(key).getComponent();
            objMass[keysSize][3]= rb.getRule(key).getFuncRatio();
            objMass[keysSize][4]= rb.getRule(key).isContribution();
            objMass[keysSize][5]= key;
            objMass[keysSize][1]= rb.getRule(key).getSubsystem();
            objMass[keysSize][2]= rb.getRule(key).getFallValue();
            keysSize--;
        }
        return objMass;
    }
    /**
     * Процедура для очистки базы фактов и правил
     */
    public void deleteAll() {
        fb.setFacts(new Hashtable<>());
        rb.setRules(new Hashtable<>());
    }

    public void exportRuleBase() throws IOException {
        Writer writer = new FileWriter("Output.json");
        String js = new Gson().toJson(rb.getRules());
        writer.write(js);
        writer.close();
    }

    public Object[][] importRuleBase(String path) throws IOException {

        Gson gson = new Gson();

        BufferedReader br = new BufferedReader(
                new FileReader(path));
        Type type = new TypeToken<Hashtable<Integer, Rule>>() {
        }.getType();

         rb.setRules(gson.fromJson(br, type));

        return ruletoObj();
    }

    public String createBackup(){

        return new GsonBuilder().setPrettyPrinting().create().toJson(fb.getFacts());
    }

    public double analaze(){
        Hashtable<Integer, Fact> fbBackup= new Hashtable<>();
        fbBackup.putAll(fb.getFacts());
        double result= getEfficiency();
        fb.setFacts(fbBackup);
        return result;
    }
    /**
     * Метод расчитывает эффективность работы системы с учетом всех потерь
     *
     * @return возвращает эффективность работы системы
     */
    private double getEfficiency()  {

        if (fb.getKeys().size()==0){
          throw new RuntimeException();
       }
     //   RulesBase rb = RulesBase.getInstance();
       // FactsBase fb = FactsBase.getInstance();

        //объединение потерь
        fb.poolLosses();
        //удаление нулевых фактов
        fb.deleteZeroFact();

        //коллекция новых фактов
        Hashtable<Integer, Fact> newFacts = new Hashtable<>();
        //String genComponent=;

        for (int keyF : fb.getKeys()) {
            Fact mainSystem = null;
            for (int keyR : rb.getKeys()) {
                /*
                     Если обозначение компонента в факте keyF равно такому же обозначению компонента в правиле keyR
                 */

                if (fb.getFact(keyF).getComponent().equals(rb.getRule(keyR).getComponent())) {
                    //коллекция компонентов
                    List<Component> lComponent = new ArrayList<>();
                    //заполнение коллекции компонентами, которые входят в подсистему fb.getFact(keyF).getComponent()
                    lComponent = rb.listComponent(new Component(fb.getFact(keyF).getComponent()), lComponent);

                    /*
                        Если в коллекции компонентов есть компоненты, которые заданы в базе фактов и
                        не являются подсистемами
                     */
                    if (fb.haveComponent(lComponent, keyR) == false) {
                        newFacts.put(newFacts.size() + 1, fb.applyFact(keyF, rb.getRule(keyR)));
                    } else {

                        boolean fl= false;
                        for(int key: newFacts.keySet()) {
                            if (newFacts.get(key).getComponent().equals(fb.getFact(keyF).getComponent()) && newFacts.get(key).getRuleValue() == fb.getFact(keyF).getRuleValue() && newFacts.get(key).getFallValue() == fb.getFact(keyF).getFallValue()) {
                                fl=true;
                            }
                        }
                        if (fl==false){
                            newFacts.put(newFacts.size() + 1, fb.getFact(keyF));
                        }
                    }

                } else {
                    /*
                        Если компонент, заданный в факте keyF является главной системой
                     */
                    if (fb.getFact(keyF).getComponent().equals(rb.getMainSystem().getComponent())) {
                        mainSystem = fb.getFact(keyF);
                    }
                }

            }

            if (mainSystem != null) {
               newFacts.put(newFacts.size() + 1, mainSystem);
            }

        }

        System.out.println("////////////////////////////");
        if (newFacts.size() != 1) {
            fb.setFacts(newFacts);
            return getEfficiency();

        } else {

            if (newFacts.get(1).getComponent().equals(rb.getMainSystem().getComponent())==false){
                fb.setFacts(newFacts);
                return getEfficiency();
            }
            else
            {
                return 1 - newFacts.get(1).getFallValue();

            }

        }
    }

}


