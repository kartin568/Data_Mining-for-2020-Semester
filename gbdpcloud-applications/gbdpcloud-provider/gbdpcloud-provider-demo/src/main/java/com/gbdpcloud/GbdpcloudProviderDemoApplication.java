package com.gbdpcloud;

import com.gbdpcloud.Handler.SpringUtil;
import org.springframework.context.annotation.Import;
import gbdpcloudsecurityapp.gbdpcloudsecurityapp.SecurityCoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
@Import(SpringUtil.class)

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("gbdpcloudprovideruserapi.gbdpcloudprovideruserapi")
@SecurityCoreConfig
public class GbdpcloudProviderDemoApplication {
// test
    public static void main(String[] args) {
        SpringApplication.run(GbdpcloudProviderDemoApplication.class, args);
    }
}
