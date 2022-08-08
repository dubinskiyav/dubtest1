package basics;

/**
 * Работа Дженериков
 */
public class GenericsExample {

    public void test() {
        System.out.println("basics.GenericsExample.test()");
        Avto ma = new Avto("Ziguli",65,12345678);
        System.out.println(ma);
        AvtoG<Integer> maI = new AvtoG("Lada",85,7654312);
        System.out.println(maI);
        AvtoG<Double> maD = new AvtoG("Lada",85,76543.21);
        System.out.println(maD);
        AvtoG<String> maS = new AvtoG("Lada",85,"76543");
        System.out.println(maS);
        maS.printModelName();
        String[] people = {"Tom", "Alice", "Sam", "Kate", "Bob", "Helen"};
        maS.<String>print(people);
    }

}

/**
 * Автомобиль
 */
class Avto {

    private String modelName;
    private int enginePower;
    private int vinCode;

    Avto(String modelName, int enginePower, int vinCode) {
        this.modelName = modelName;
        this.enginePower = enginePower;
        this.vinCode = vinCode;
    }

    public String getModelName() {
        return modelName;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public int getVinCode() {
        return vinCode;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public void setVinCode(int vinCode) {
        this.vinCode = vinCode;
    }

    @Override
    public String toString() {
        return getClass().getName() + ":"
                + " modelName=" + getModelName()
                + " enginePower=" + getEnginePower()
                + " vinCode=" + getVinCode();
    }
}

interface Avtotable<TvinCode>{
    void printModelName();
    TvinCode getVinCode();
}

class AvtoG<TvinCode> implements Avtotable<TvinCode> {

    private String modelName;
    private int enginePower;
    private TvinCode vinCode;

    AvtoG(String modelName, int enginePower, TvinCode vinCode) {
        this.modelName = modelName;
        this.enginePower = enginePower;
        this.vinCode = vinCode;
    }

    public TvinCode getVinCode() {return vinCode;}

    public void printModelName() {
        System.out.println(getModelName());
    }

    public String getModelName() {
        return modelName;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public void setVinCode(TvinCode vinCode) {
        this.vinCode = vinCode;
    }

    public <T> void print(T[] items) {
        for (T item: items) {
            System.out.println(item);
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + ":"
                + " modelName=" + getModelName()
                + " enginePower=" + getEnginePower()
                + " vinCode=" + getVinCode();
    }
}