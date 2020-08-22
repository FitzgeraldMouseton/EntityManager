import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

public class FirstLambdas {

    public static void main(String[] args) {

        Supplier<String> supplier = () -> "Hello!";
        System.out.println(supplier.get());

        Consumer<String> consumer = (String s) -> System.out.println(s);
        consumer.accept("Hello!");

        Predicate<String> predicate = (String s) -> s.isEmpty();
        System.out.println(predicate.test("Avo"));

        Function<Integer, String> function = (i) -> String.valueOf(i);
        System.out.println(function.apply(8));

        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        list.add(7);

        Predicate<Integer> filter = (integer -> integer > 3);
        list.removeIf(filter);

        Consumer<Integer> action = (integer -> System.out.println(integer));
        list.forEach(action);

        Comparator<Integer> comparator = (i1, i2) -> Integer.compare(i1, i2);
        int compared = comparator.compare(10, 20);

        IntSupplier intSupplier = () -> 10;
        int i = intSupplier.getAsInt();
        System.out.println(i);

        // ================================================

        Consumer<String> c1 = (s) -> System.out.println("c1: " + s);
        Consumer<String> c2 = (s) -> System.out.println("c2: " + s);

        Consumer<String> c3 = (s) -> {
            c1.accept(s);
            c2.accept(s);
        };
        c3.accept("Hello");

        // OR

        c3 = c1.andThen(c2);
        c3.accept("Hello");

        // =================================================

        List<String> strings = Arrays.asList("one", "two", "three", "four", "five");


        Comparator<String> cmp = (s1, s2) -> s1.compareTo(s2);
        strings.sort(cmp);
        System.out.println(strings);


        cmp = (s1, s2) -> Integer.compare(s1.length(), s2.length());
        strings.sort(cmp);
        System.out.println(strings);

        //OR

        Function<String, Integer> func = s -> s.length(); // cons - autoboxing of ints
        cmp = Comparator.comparing(func);
        strings.sort(cmp);
        System.out.println(strings);

        //OR

        ToIntFunction<String> func1 = String::length; //
        cmp = Comparator.comparingInt(func1);
        strings.sort(cmp);
        System.out.println(strings);


        Comparator<String> cInt = Comparator.comparing(s -> s.length());
        Comparator<String> cBytes = Comparator.comparing(s -> s.getBytes().length);
        cmp = Comparator.comparing((String s) -> s.length()).thenComparing(s -> s.getBytes().length).reversed();
        strings.sort(cmp);
        System.out.println(strings);

        LocalDate date1 = LocalDate.of(1990, Month.SEPTEMBER, 5);
        LocalDate date2 = LocalDate.of(1990, Month.SEPTEMBER, 6);
        final Temporal temporal = date1.adjustInto(date2);

        ZonedDateTime currentMeeting = ZonedDateTime.of(LocalDate.of(2020, Month.JULY, 29),
                                                        LocalTime.of(10, 20),
                                                        ZoneId.of("Europe/London"));
        ZonedDateTime nextMeeting = currentMeeting.plusMonths(1);
        ZonedDateTime nextMeetingUS = nextMeeting.withZoneSameInstant(ZoneId.of("US/Central"));
//        LocalDate localDate = LocalDate.from(Instant.now());

        Instant start = Instant.now();

        StringBuffer builder = new StringBuffer();
        AtomicInteger b = new AtomicInteger(1);

//        String string = () -> String.valueOf(b.getAndIncrement()).repeat(100);

        Instant end = Instant.now();
        System.out.println("Duration: " + Duration.between(start, end).get(ChronoUnit.NANOS) / 1_000_000);

        System.out.println("4".repeat(7));

        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        joiner.add("one").add("two").add("three");
        System.out.println(joiner.toString());

        String string = String.join(", ", "one", "two", "three");
        System.out.println(string);
    }
}
