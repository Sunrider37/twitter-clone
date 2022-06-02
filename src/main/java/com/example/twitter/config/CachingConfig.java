package com.example.twitter.config;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager ehCacheManager() {
        CacheConfiguration tweetsCache = new CacheConfiguration();
        tweetsCache.setName("tweets-cache");
        tweetsCache.setMemoryStoreEvictionPolicy("LRU");
        tweetsCache.setMaxEntriesLocalHeap(1000);
        tweetsCache.setTimeToLiveSeconds(3600);
        net.sf.ehcache.config.Configuration configuration = new net.sf.ehcache.config.Configuration();
        configuration.addCache(tweetsCache);
        return CacheManager.newInstance();
    }

    @Bean
    @Override
    public org.springframework.cache.CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }
}
