package misolab.book.rxjava;

import io.reactivex.Observable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Ch4 {

    public static void main(String[] args) {
        Ch4 ch4 = new Ch4();

//        ch4.rxInterval();
        ch4.rxTimer();
    }

    private void rxTimer() {
        Observable.timer(500L, TimeUnit.MILLISECONDS)
                .map(nu -> new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                        .format(new Date()))
                .subscribe(Log::it);
        CommonUtils.sleep(1000);

    }

    private void rxInterval() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(data -> data + 1 * 100)
                .take(5)
                .subscribe(Log::it);

        CommonUtils.sleep(1000);
    }
}
