package edu.msu.puidoka3.cloudhatter.Cloud;

import edu.msu.puidoka3.cloudhatter.Cloud.Models.Catalog;
import edu.msu.puidoka3.cloudhatter.Cloud.Models.LoadResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static edu.msu.puidoka3.cloudhatter.Cloud.Cloud.CATALOG_PATH;
import static edu.msu.puidoka3.cloudhatter.Cloud.Cloud.LOAD_PATH;

public interface HatterService {
    @GET(CATALOG_PATH)
    Call<Catalog> getCatalog(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    @GET(LOAD_PATH)
    Call<LoadResult> loadHat(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password,
            @Query("id") String idHatToLoad
    );
}
