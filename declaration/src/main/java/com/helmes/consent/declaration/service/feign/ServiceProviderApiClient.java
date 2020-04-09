package com.helmes.consent.declaration.service.feign;

import com.helmes.consent.declaration.feign.api.ServiceProviderApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("PROVIDER")
public interface ServiceProviderApiClient extends ServiceProviderApi {
}
