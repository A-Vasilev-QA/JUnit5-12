package guru.qa;

import org.junit.jupiter.api.*;

@DisplayName("Класс с демонстрационными тестами")
public class SimpleTest {

    @BeforeAll
    public static void setUp() {
        System.out.println("Выполняю @BeforeAll");
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Выполняю @AfterAll");
    }

    @BeforeEach
    public static void beforeEach() {
        System.out.println("Выполняю @BeforeEach");
    }

    @AfterEach
    public static void afterEach() {
        System.out.println("Выполняю @AfterEach");
    }

    @Disabled("CODETOOLS-7902347")
    @DisplayName("Демонстрационный тест")
    @Test
    void firstTest() {
        // Вот тут проверим ...
        Assertions.assertTrue(3 > 2);
        Assertions.assertFalse(3 < 2);
        Assertions.assertEquals("Foo", "Foo");
        Assertions.assertAll(
                () -> Assertions.assertTrue(3 < 2),
                () -> Assertions.assertTrue(3 > 2)
        );
    }

    @DisplayName("Демонстрационный тест № 2")
    @Test
    void secondTest() {
        Assertions.assertTrue(2 >= 2);
    }
}