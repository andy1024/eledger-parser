package org.warheim.net;

import org.junit.Assert;
import org.junit.Test;
import org.warheim.di.ObjectCache;
/**
 *
 * @author andy
 */
public class CacheabilityAnnotationTest {

    @Test
    public void testAnnotation() {
        Assert.assertTrue(ObjectCache.isCacheable(org.warheim.net.WebProcessorJsoup.class));
        Assert.assertTrue(ObjectCache.isCacheable(org.warheim.net.WebProcessorApache.class));
        Assert.assertFalse(ObjectCache.isCacheable(org.warheim.net.RequestPreparationException.class));
    }
}
