package misolab;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.logging.Logger;

public class ObservableExample {
    static public Logger logger = Logger.getLogger("ObservableExample");

    public static void main(String[] args) throws InterruptedException {

        Observable<String> observable = Observable.create(emitter -> {
            String[] datas = {"Hello, world", "Bye World"};
            for (String data : datas) {
                if (emitter.isDisposed()) {
                    return;
                }
                emitter.onNext(data);
            }
            emitter.onComplete();
        });

        observable
                .observeOn(Schedulers.computation())
                .subscribe(new Observer<String>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        logger.info("onSubscribe-");
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
