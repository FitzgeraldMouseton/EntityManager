package reflection;

import reflection.metamodel.model.Person;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {

        // Class<?> не скомпиллируется, т.к. так работают дженерики в java, и ? в этом случае не является потомком класса String.
        // 1)
        final Class<? extends String> class1 = "Hello".getClass();
        final Class<? extends String> class2 = "World".getClass();
        // 2)
        final Class<String> class3 = String.class;
        // 3)
        final Class<?> class4 = Class.forName("java.lang.String");

        System.out.println(class1 == class2);
        System.out.println(class1 == class3);
        System.out.println(class1 == class4);

        // Get all implemented interfaces
        final Class<?>[] interfaces = class1.getInterfaces();
        System.out.println(Arrays.toString(interfaces));

        // Get superclass
        final Class<?> superclass = class1.getSuperclass();
        System.out.println(superclass);

        // ====================================== Fields =============================

        Class<Person> personClass = Person.class;

        // 1) Если знаем имя поля
        final Field age = personClass.getDeclaredField("age");
        // 2) Все объявленные в классе поля
        final Field[] declaredFields = personClass.getDeclaredFields();
        System.out.println(Arrays.toString(declaredFields));
        // 3) Публичные и унаследованные поля
        final Field[] fields = personClass.getFields();
        System.out.println(Arrays.toString(fields));

        // ====================================== Methods =============================

        // 1)
        Method method = personClass.getMethod("of", String.class, int.class);
        System.out.println(method);
        // 2)
        final Method[] declaredMethods = personClass.getDeclaredMethods();
        System.out.println("Declared methods: " + Arrays.toString(declaredMethods));
        // 3)
        final Method[] methods = personClass.getMethods();
        System.out.println(Arrays.toString(methods));

        // ====================================== Modifiers =============================

        int modifiers = age.getModifiers();
//        boolean isPublic = modifiers & 0x00000001;
        boolean isPublic = Modifier.isPublic(modifiers);
        modifiers = method.getModifiers();
        boolean isStatic = Modifier.isStatic(modifiers);
        System.out.println(isPublic);
        System.out.println(isStatic);

        Arrays.stream(methods).filter(method1 -> Modifier.isStatic(method1.getModifiers())).forEach(System.out::println);
    }
}
