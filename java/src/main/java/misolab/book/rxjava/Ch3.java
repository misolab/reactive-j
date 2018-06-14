package misolab.book.rxjava;

import io.reactivex.Observable;

import java.util.Scanner;
import java.util.stream.Stream;

public class Ch3 {

    public static void main(String[] args) {
        Ch3 ch3 = new Ch3();
//        ch3.testMap();
        ch3.testStream();
    }

    private void testStream() {
//        <R> Stream<R> flatMap (Function<? super T, ? extends Stream<? extends R>> mapper);
//        <R> Stream<R> map     (Function<? super T, ? extends R> mapper);

        Scanner in = new Scanner(System.in);
        System.out.println("Input:");
        int dan = Integer.parseInt(in.nextLine());

//        Stream.iterate(1, a -> a + 1)
//                .limit(10)
//                .map(a -> String.format(" %d x %d = %d", dan, a, dan * a))
//                .forEach(System.out::println);

//        java.util.function.Function<Integer, Stream<String>> gugudan = d ->
//                Stream.iterate(1, i -> i + 1)
//                        .limit(10)
//                        .map(row -> String.format(" %d x %d = %d", d, row, d * row));
//
//        Stream.of(dan)
//                .flatMap(gugudan)
//                .forEach(System.out::println);


        Stream.of(dan)
                .flatMap(
                        d -> Stream.iterate(1, i -> i + 1)
                                .limit(10)
                                .map(row -> String.format(" %d x %d = %d", d, row, d * row))
                )
                .forEach(System.out::println);
    }

    private void testMap() {

//        public final <R> Observable<R> flatMap    (Function<? super T, ? extends ObservableSource<? extends R>> mapper)
//        public final <R> Observable<R> map        (Function<? super T, ? extends R> mapper)

        Scanner in = new Scanner(System.in);
        System.out.println("Input:");
        int dan = Integer.parseInt(in.nextLine());

//        Observable<Integer> source = Observable.range(1, 9);
//        source.subscribe(row -> System.out.println(dan + " x " + row + " = " + dan*row));

        io.reactivex.functions.Function<Integer, Observable<String>> gugudan =
                num -> Observable.range(1, 9)
                        .map(row -> num + " x " + row + " = " + num * row);

        Observable<String> source = Observable.just(dan).flatMap(gugudan);

//        Observable<String> source = Observable
//                .just(dan)
//                .flatMap(
//                        num -> Observable
//                                .range(1, 9)
//                                .map(row -> num + " x " + row + " = " + num * row)
//                );

        source.subscribe(System.out::println);

    }
}
