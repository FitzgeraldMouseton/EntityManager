package reflection.metamodel.beanmanager;

import reflection.metamodel.annotations.Inject;
import reflection.metamodel.annotations.Provides;
import reflection.metamodel.provider.H2ConnectionProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Этот класс создает инстанс EntityManager, проевряет, есть ли у него поля с аннотацией @Inject, и создает эти поля
 */

public class BeanManager {

    //"Наивная" имплементация синглтона
    private static BeanManager beanManager = new BeanManager();
    private Map<Class<?>, Supplier<?>> registry = new HashMap<>();

    public static BeanManager getInstance() {
        return beanManager;
    }

    private BeanManager() {
        /* Здесь проверяем, классы и методы, помеченные @Provides, чтобы знать, что должно быть заиндектено.
         Однако, в JDK нет способа получить список всех загруженные классы, чтобы проверить их на наличие аннотаций.
         Такую возможность добавляют сторонние библиотеки вроде Guava или Reflections API, но тут мы сделаем по-простому
         */
        final List<Class<?>> classes = List.of(H2ConnectionProvider.class);
        for (Class<?> clss : classes) {
            final Method[] methods = clss.getDeclaredMethods();
            for (Method method : methods) {
                final Provides annotation = method.getAnnotation(Provides.class);
                if (annotation != null) {
                    final Class<?> returnType = method.getReturnType();
                    // Создаем supplier,  в котором будет храниться сам вызов нужно метода у нужного объекта
                    // (или не у объекта, если метод статический)
                    Supplier<?> supplier = () -> {
                        try {
                            if (!Modifier.isStatic(method.getModifiers())) {
                                final Object o = clss.getConstructor().newInstance();
                                return method.invoke(o);
                            } else {
                                return method.invoke(null);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    };
                    // Сохраняем класс объекта, который нужно будет заинжектить через @Inject, и "рецепт" его создания
                    registry.put(returnType, supplier);
                }
            }
        }
    }

    public <T> T getInstance(Class<T> clss) {
        try {
            final T t = clss.getConstructor().newInstance();
            final Field[] fields = clss.getDeclaredFields();
            for (Field field : fields) {
                final Inject annotation = field.getAnnotation(Inject.class);
                if (annotation != null) {
                    final Class<?> injectedFieldType = field.getType();
                    final Supplier<?> supplier = registry.get(injectedFieldType);
                    final Object instanceToInject = supplier.get();
                    field.setAccessible(true);
                    field.set(t, instanceToInject);
                }
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
