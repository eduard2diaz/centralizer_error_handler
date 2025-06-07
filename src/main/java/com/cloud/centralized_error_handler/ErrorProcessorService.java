package com.cloud.centralized_error_handler;

import response.ApiError;

public interface ErrorProcessorService {
    void process(ApiError apiError);
}
