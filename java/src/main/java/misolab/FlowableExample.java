package misolab;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.logging.Logger;

public class FlowableExample {

    static public Logger logger = Logger.getLogger("FlowableExample");

    public static void main(String[] args) throws InterruptedException {
        logger.info("restart");

        Flowable<String> flowable = Flowable.create(emitter -> {
            String[] datas = {"Hello, world", "Bye World"};
            for (String data : datas) {
                if (emitter.isCancelled()) {
                    return;
                }
                emitter.onNext(data);
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);

        flowable
                .observeOn(Schedulers.computation())
                .subscribe(new Subscriber<String>() {
                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.subscription = s;
                        this.subscription.request(10);
                    }

                    @Override
                    public void onNext(String s) {
                        String threadName = Thread.currentThread().getName();
                        logger.info("onNext-" + threadName);
                        logger.info(s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        String threadName = Thread.currentThread().getName();
                        logger.info("onComplete-" + threadName);
                    }
                });

        Thread.sleep(500L);
    }
}
