package misolab.book.rxjava;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ch5 {

    private static final String URL = "https://api.github.com/repos/square/retrofit/contributors";

    public static void main(String[] args) {
        Ch5 ch5 = new Ch5();
//        ch5.testExam();
        ch5.testExam2();
    }

    private void testExam2() {
        Observable<String> source = ConnectableObservable.just(URL)
                .map(OkHttpHelper::getWithLog)
                .subscribeOn(Schedulers.io())
                .share()
                .observeOn(Schedulers.newThread());

        source.map(this::parseLogin).subscribe(Log::it);
        source.map(this::parseHtmlUrl).subscribe(Log::it);
        source.map(this::parseContributions).subscribe(Log::it);

        CommonUtils.sleep(3000);
    }


    private void testExam() {
        Observable<String> source = Observable.just(URL)
                .map(OkHttpHelper::getWithLog)
                .subscribeOn(Schedulers.io());

        Observable<String> login = source.map(this::parseLogin);
        Observable<String> htmlUrl = source.map(this::parseHtmlUrl);
        Observable<String> contributions = source.map(this::parseContributions);

        Observable.concat(login, htmlUrl, contributions)
                .observeOn(Schedulers.newThread())
                .doOnError(e -> e.printStackTrace())
                .subscribe(Log::it);
        CommonUtils.sleep(3000);
    }


    private String parseContributions(String json) {
        return parse(json, "\"contributions\":[0-9]*.[0-9]*");
    }

    private String parseHtmlUrl(String json) {
        return parse(json, "\"html_url\":\"[a-zA-Z]*\"");
    }

    private String parseLogin(String json) {
        return parse(json, "\"login\":\"[a-zA-Z]*\"");
    }

    private String parse(String json, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(json);
        if (match.find()) {
            return match.group();
        }
        return "N/A";
    }


}
