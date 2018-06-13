package misolab.book.rxjava;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.ReplaySubject;
import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Ch2 {

    Logger logger = Logger.getLogger(Ch2.class.getSimpleName());


    public void testObservable() {
        logger.info("emit start");

//        Observable source = basicObservale();
//        Observable source = advanceObservale();


        Observable source = publishObservable();
        Disposable disposable = source.subscribe(System.out::println);
        logger.info("disposable.isDisposed() : " + disposable.isDisposed());
    }

    public Observable<String> publishObservable() {

        Publisher<String> publisher = (s -> {
            s.onNext("Publisher<String> publisher");

            s.onNext("onNext 1");
            s.onNext("onNext 2");
            s.onNext("onNext 3");

            s.onComplete();
        });
        return Observable.fromPublisher(publisher);
    }

    public Observable<String> advanceObservale() {
        Callable<String> callable = () -> {
            Thread.sleep(1000);
            return "This is Callable";
        };
//        logger.info(callable.call());
//        return Observable.fromCallable(callable);


        Future<String> future = Executors.newSingleThreadExecutor().submit(callable);
//        logger.info(future.get());
        return Observable.fromFuture(future);
    }

    public Observable<Integer> basicObservale() {
        //  just
//        return Observable.just(1,2,3);

        //  create
//        Observable<Integer> source =
//        Observable.create((ObservableEmitter<Integer> emitter) -> {
//            emitter.onNext(100);
//            emitter.onNext(200);
//            emitter.onNext(300);
//            emitter.onComplete();
//        });

        //  fromXXX
//        Integer[] arr = {100, 200, 300};
//        return Observable.fromArray(arr);

        List<Integer> list = Arrays.asList(100, 200, 300);
        return Observable.fromIterable(list);
    }

    public static void main(String[] args) {

        Ch2 ch2 = new Ch2();
//        ch2.testObservable();
//        ch2.testSingle();
//        ch2.testSubject();
        ch2.test2Subject();
    }

    private void test2Subject() {

        String[] dt = {"1", "3", "5"};
        Observable<String> balls = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(i -> dt[i])
                .take(dt.length);

        ConnectableObservable<String> source = balls.publish();

        source.subscribe(data -> logger.info(" #1 -> " + data));
        source.subscribe(data -> logger.warning(" #2 -> " + data));
        source.connect();

        safeSleep(250);
        source.subscribe(data -> logger.warning(" #3 -> " + data));
        safeSleep(100);
    }

    public void safeSleep(long interval) {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void testSubject() {

//        AsyncSubject
//        AsyncSubject subject = AsyncSubject.create();

//        BehaviorSubject
//        BehaviorSubject subject = BehaviorSubject.createDefault(0);
//        subject.subscribe(data -> logger.info(" #1 -> " + data));

//        PublishSubject
//        PublishSubject<Integer> subject = PublishSubject.create();

//        ReplaySubject
        ReplaySubject<Object> subject = ReplaySubject.create();

        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);
        subject.subscribe(data -> logger.info(" #1 -> " + data));

        subject.onNext(10);
        subject.onNext(11);
        subject.subscribe(data -> logger.warning(" #2 -> " + data));

        subject.onNext(100);
        subject.onComplete();
        subject.onNext(110);
    }



    private void testSingle() {
        Observable<String> source = Observable.just("This is Single");
        Single.fromObservable(source)
                .subscribe(System.out::println);

        Observable.just("This is Single")
                .single("defaultItem")
                .subscribe(System.out::println);

        Integer[] arr = {100, 200, 300};
        Observable.fromArray(arr)
                .first(0)
                .subscribe(System.out::println);

        Observable.empty()
                .first(0)
                .subscribe(System.out::println);

        Observable.fromArray(arr)
                .take(2)
                .subscribe(System.out::println);
    }
}
