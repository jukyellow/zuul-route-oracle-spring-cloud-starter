/**
 * Copyright (c) 2015 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jukyellow.spring.cloud.zuul.support;

import io.github.jukyellow.spring.cloud.zuul.route.StoreRefreshableRouteLocator;
import io.github.jukyellow.spring.cloud.zuul.store.ZuulRouteStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.ZuulProxyAutoConfiguration;
//import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.context.annotation.Configuration;

/**
 * Registers a {@link org.springframework.cloud.netflix.zuul.filters.RouteLocator} that is being populated through
 * external store.
 *
 * @author Jakub Narloch (Modified by Un-kuk on 19/5/30)
 */
@Configuration
public class ZuulProxyStoreConfiguration extends ZuulProxyAutoConfiguration { //(2019.05.28,juk) ZuulProxyConfiguration -> ZuulProxyAutoConfiguration

  @Autowired
  private ZuulRouteStore zuulRouteStore;

  @Autowired
  private DiscoveryClient discovery;

  @Autowired
  private ZuulProperties zuulProperties;

  @Autowired
  private ServerProperties server;
  
  @Autowired
  private ServiceInstance localServiceInstance;

  @Override
  public DiscoveryClientRouteLocator discoveryRouteLocator() { //(2019.05.28,juk) routeLocator -> discoveryRouteLocator
	  return new StoreRefreshableRouteLocator(server.getServletPath(), discovery, zuulProperties, zuulRouteStore, localServiceInstance);
  }
}
