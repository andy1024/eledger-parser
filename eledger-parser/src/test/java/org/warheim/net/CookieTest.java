package org.warheim.net;

import org.junit.Assert;
import org.junit.Test;
import static org.warheim.net.WebProcessorApache.processCookie;

/**
 *
 * @author andy
 */
public class CookieTest {
    
    @Test
    public void testSimpleCookieExtraction() throws Exception {
        WebResponse wr = new WebResponseImpl(200, "");
        processCookie(wr, "jingle=blablabla");
        Assert.assertEquals("blablabla", wr.getCookie("jingle"));
    }

    @Test
    public void testVerboseCookieExtraction() throws Exception {
        WebResponse wr = new WebResponseImpl(200, "");
        processCookie(wr, "jingle=blablabla; path=/");
        Assert.assertEquals("blablabla", wr.getCookie("jingle"));
    }

    @Test
    public void testNoValueCookieExtraction() throws Exception {
        WebResponse wr = new WebResponseImpl(200, "");
        processCookie(wr, "jingle");
        Assert.assertTrue(wr.getCookies().containsKey("jingle"));
        Assert.assertNull(wr.getCookie("jingle"));
    }

}
