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

package io.github.jukyellow.spring.cloud.zuul.route;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;

import io.github.jukyellow.spring.cloud.zuul.store.ZuulRouteStore;

import java.util.LinkedHashMap;

/**
 * A simple {@link org.springframework.cloud.netflix.zuul.filters.RouteLocator} that is being populated from configured
 * {@link ZuulRouteStore}.
 *
 * @author Jakub Narloch (Modified by Un-kuk on 19/5/30)
 */
public class StoreRefreshableRouteLocator extends DiscoveryClientRouteLocator {

  private final ZuulRouteStore store;

  /**
   * Creates new instance of {@link StoreRefreshableRouteLocator}
   * @param servletPath the application servlet path
   * @param discovery the discovery service
   * @param properties the zuul properties
   * @param store the route store
   */
	/*
	 * @Deprecated //(2019.05.28,juk) Delete deprecated method public
	 * StoreRefreshableRouteLocator(String servletPath, DiscoveryClient discovery,
	 * ZuulProperties properties, ZuulRouteStore store) { super(servletPath,
	 * discovery, properties); this.store = store; }
	 */
  
  //(2019.05.28,juk) Add new method
  public StoreRefreshableRouteLocator(String servletPath,
          DiscoveryClient discovery,
          ZuulProperties properties,
          ZuulRouteStore store,
          ServiceInstance localServiceInstance) {
	super(servletPath, discovery, properties, localServiceInstance);
	this.store = store;
	}

  /**
   * {@inheritDoc}
   */
  @Override
  protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
	LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
    routesMap.putAll(super.locateRoutes());
    for (ZuulProperties.ZuulRoute route : store.findAll()) {
      routesMap.put(route.getPath(), route);
    }
    return routesMap;
  }
}
