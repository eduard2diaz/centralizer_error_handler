package com.cloud.error_handler;

import response.ApiError;

public interface ErrorProcessorService {
    void process(ApiError apiError);
}
