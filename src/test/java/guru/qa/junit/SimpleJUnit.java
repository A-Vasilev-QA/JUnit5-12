package guru.qa.junit;

import guru.qa.SimpleTest;
import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SimpleJUnit {

    public static void main(String[] args) throws Exception {
        boolean areTests = false;
        Method[] declaredMethods = SimpleTest.class.getDeclaredMethods();
        Method beforeAllMethod = null;
        Method beforeEachMethod = null;
        Method afterEachMethod = null;
        Method afterAllMethod = null;

        //Туповатое решение, но пускай алгоритм будет такой:
        //Проверим, есть в классе тесты
        //Запомним, какие методы должны выполняться в beforeAll, beforeEach, afterAll, afterEach
        for (Method method : declaredMethods) {
            method.setAccessible(true);
            Test testAnnotation = method.getAnnotation(Test.class);
            BeforeAll beforeAll = method.getAnnotation(BeforeAll.class);
            AfterAll afterAll = method.getAnnotation(AfterAll.class);
            BeforeEach beforeEach = method.getAnnotation(BeforeEach.class);
            AfterEach afterEach = method.getAnnotation(AfterEach.class);
            Disabled disabled = method.getAnnotation(Disabled.class);
            if (testAnnotation != null && disabled == null) {
                areTests = true;
            }
            if (beforeAll != null) {
                beforeAllMethod = method;
            }
            if (beforeEach != null) {
                beforeEachMethod = method;
            }
            if (afterAll != null) {
                afterAllMethod = method;
            }
            if (afterEach != null) {
                afterEachMethod = method;
            }
        }
        //Если тесты есть
        if (areTests) {
            //Запустим beforeAll перед всеми методами
            if (beforeAllMethod != null) {
                beforeAllMethod.invoke(SimpleTest.class.getDeclaredConstructor().newInstance());
            }
            //Смотрим, есть ли над методом @Test
            for (Method method : declaredMethods) {
                method.setAccessible(true);
                Test testAnnotation = method.getAnnotation(Test.class);
                Disabled disabled = method.getAnnotation(Disabled.class);
                DisplayName displayName = method.getAnnotation(DisplayName.class);
                if (testAnnotation != null && disabled == null) {
                    //Если есть, выполняем перед методом @beforeEach
                    if (beforeEachMethod != null) {
                        beforeEachMethod.invoke(SimpleTest.class.getDeclaredConstructor().newInstance());
                    }
                    //Выводим имя теста и выполняем метод
                    if (displayName != null) {
                        System.out.println("Выполняю тест: " + displayName.value());
                    } else {
                        System.out.println("Выполняю тест без названия");
                    }
                    try {
                        method.invoke(SimpleTest.class.getDeclaredConstructor().newInstance());
                    } catch (InvocationTargetException e) {
                        System.out.println("Тест упал: " + e.getCause().getMessage() + " ");
                        throw  e;
                    }
                    //Выполняем после него @afterEach
                    if (afterEachMethod != null) {
                        afterEachMethod.invoke(SimpleTest.class.getDeclaredConstructor().newInstance());
                    }
                }
            }
            //После них всех выполняем @afterAll
            if (afterAllMethod != null) {
                afterAllMethod.invoke(SimpleTest.class.getDeclaredConstructor().newInstance());
            }
        } else {
            System.out.println("There are no tests to execute");
        }
    }
}