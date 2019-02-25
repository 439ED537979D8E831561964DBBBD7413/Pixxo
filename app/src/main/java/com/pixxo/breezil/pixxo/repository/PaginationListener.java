package com.pixxo.breezil.pixxo.repository;

import android.arch.paging.PageKeyedDataSource;

import java.util.List;

public interface PaginationListener<Response, Result> {

    void onInitialError(Throwable throwable);

    void onInitialSuccess(Response response, PageKeyedDataSource.LoadInitialCallback<Integer, Result> callback, List<Result> results);

    void onPaginationError(Throwable throwable);

    void onPaginationSuccess(Response response, PageKeyedDataSource.LoadCallback<Integer, Result> callback, PageKeyedDataSource.LoadParams<Integer> params, List<Result> results);

    void clear();
}
