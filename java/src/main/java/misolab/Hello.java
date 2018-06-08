package misolab;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.io.IOException;
import java.util.List;

public class Hello {

    public static final void log(String msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {

        String hello = "Hello world";
        log(hello);
        log("----------");

        Flowable.just(hello).subscribe(Hello::log);

        GitHubService service = GitHubService.retrofit.create(GitHubService.class);

        Observable<List<Contributor>> observable = service.repoContributors2("square", "retrofit");
        observable.subscribe(
                contributors -> {
                    log("-----retrofit2 + rxjava2 -----");
                    contributors.forEach(System.out::println);
                }
        );


        try {
            //  sync
            if (true) {
                Call<List<Contributor>> call = service.repoContributors("square", "retrofit");
                List<Contributor> result = call.execute().body();
                log("-----retrofit2 sync -----");
                result.forEach(System.out::println);
            }

            if (true) {
                //  async
                Call<List<Contributor>> call = service.repoContributors("square", "retrofit");
                call.enqueue(new Callback<List<Contributor>>() {
                    @Override
                    public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                        List<Contributor> result = response.body();
                        log("-----retrofit2 async -----");
                        result.forEach(System.out::println);
                    }

                    @Override
                    public void onFailure(Call<List<Contributor>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class Contributor {
    String login;
    String html_url;

    int contributions;

    @Override
    public String toString() {
        return "Contributor{" +
                "login='" + login + '\'' +
                ", html_url='" + html_url + '\'' +
                ", contributions=" + contributions +
                '}';
    }
}

interface GitHubService {
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> repoContributors(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> repoContributors2(@Path("owner") String owner, @Path("repo") String repo);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build();
}