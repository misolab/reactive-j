package misolab;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.io.IOException;
import java.util.List;

public class Hello {

    public static void main(String[] args) {

        String hello = "Hello world";
        System.out.print(hello);

        Flowable.just(hello).subscribe(System.out::println);


        try {
            GitHubService service = GitHubService.retrofit.create(GitHubService.class);
            Call<List<Contributor>> call = service.repoContributors("square", "retrofit");
            //  sync
            if (false) {
                List<Contributor> result = call.execute().body();
                result.forEach(System.out::println);
            }

            //  async
            call.enqueue(new Callback<List<Contributor>>() {
                @Override
                public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                    List<Contributor> result = response.body();
                    result.forEach(System.out::println);
                }

                @Override
                public void onFailure(Call<List<Contributor>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

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

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}