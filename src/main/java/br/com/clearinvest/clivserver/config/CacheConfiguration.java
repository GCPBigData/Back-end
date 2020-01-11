package br.com.clearinvest.clivserver.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(br.com.clearinvest.clivserver.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.repository.UserRepository.USERS_BY_CPF_CACHE, jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.MarketSector.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.Stock.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.StockWatch.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.AppPreference.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.Brokerage.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.BrokerageAccount.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.StockOrder.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.StockTrade.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.StockTrade.class.getName() + ".orders", jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.StockBalance.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.StockFlow.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.BrokerageFlow.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.ExecReport.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.ChatMessage.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.BrokerageClient.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.Brokerage.class.getName() + ".brokerageClients", jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.BrokerageClient.class.getName() + ".brokerages", jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.BrokerageProduct.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.BrokerageProduct.class.getName() + ".brokerages", jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.Brokerage.class.getName() + ".brokerageProducts", jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.BrokerageAssistance.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.BrokerageAssistance.class.getName() + ".brokerages", jcacheConfiguration);
            cm.createCache(br.com.clearinvest.clivserver.domain.Brokerage.class.getName() + ".brokerageAssistances", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
