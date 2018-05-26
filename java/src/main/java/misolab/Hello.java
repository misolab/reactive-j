package misolab;

import io.reactivex.Flowable;

public class Hello {

    public static void main(String[] args) {

        String hello = "Hello world";
        System.out.print(hello);

        Flowable.just(hello).subscribe(System.out::println);
    }

}
