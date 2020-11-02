package it.algos.webbase.web.lib;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.JavaScript;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * Created by alex on 30-09-2015.
 * .
 */
public class LibCookie {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    /**
     * Creates or updates a cookie in the browser.
     * Uses the default path and makes the cookie session-scoped (not persistent)
     *
     * @param key   the key
     * @param value the value
     */
    public static void setCookie(String key, String value) {
        setCookie(key, value, getPath(), -1);
    }

    /**
     * Creates or updates a cookie in the browser.
     * Uses the default path
     *
     * @param key       the key
     * @param value     the value
     * @param expirySec the expiration time in seconds,
     *                  A positive value indicates that the cookie will expire after that many seconds have passed.
     *                  A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits.
     *                  A zero value causes the cookie to be deleted.
     */
    @Deprecated
    public static void setCookie(String key, String value, int expirySec) {
        setCookie(key, value, getPath(), expirySec);
    }


    /**
     * Creates or updates a cookie in the browser.
     *
     * @param key       the key
     * @param value     the value
     * @param path      the path
     * @param expirySec the expiration time in seconds,
     *                  A positive value indicates that the cookie will expire after that many seconds have passed.
     *                  A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits.
     *                  A zero value causes the cookie to be deleted.
     */
    public static void setCookie(String key, String value, String path, int expirySec) {

        JavaScript js = JavaScript.getCurrent();

        if (js != null) {

            // protects the value
            value = protect(value);

            if (StringUtils.isEmpty(path)) {
                path="/";
            }// end of if cycle

            if (expirySec == 0) {
                String cmd = String.format("document.cookie = '%s=; path=%s; expires=Thu, 01 Jan 1970 00:00:01 GMT';", key,path);
                js.execute(cmd);
                return;
            }

            if (expirySec > 0) {
                Instant i = Instant.now().plusSeconds(expirySec);
                Date d = Date.from(i);
                String utc = getUTCString(d);
                String cmd = String.format("document.cookie = '%s=%s; path=%s; expires=%s';", key, value, path, utc);
                js.execute(cmd);
                return;
            }

            if (expirySec < 0) {
                String cmd = String.format("document.cookie = '%s=%s; path=%s';", key, value, path);
                js.execute(cmd);
                return;
            }
        }

    }

    /**
     * Deletes a cookie in the browser.
     * Uses the default path
     *
     * @param key the key
     */
    @Deprecated
    public static void deleteCookie(String key) {
        setCookie(key, "", getPath(), 0);
    }

    /**
     * Deletes a cookie in the browser.
     * Uses the default path
     *
     * @param key the key
     */
    public static void deleteCookie(String key,String path) {
        setCookie(key, "", path, 0);
    }


    /**
     * Find a cookie by name
     */
    public static Cookie getCookie(String name) {
        // Fetch all cookies from the request
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Iterate to find cookie by its name
        if (name != null && cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null) {
                    if (name.equals(cookie.getName())) {

                        // unprotect the value
                        String value = cookie.getValue();
                        cookie.setValue(unprotect(value));

                        return cookie;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Return a cookie's value by name
     */
    public static String getCookieValue(String name) {
        String value = "";
        Cookie cookie = getCookie(name);

        if (cookie != null) {
            value = cookie.getValue();
        }// fine del blocco if

        return value;
    }// end of method


    /**
     * Convert a date in JS cookie format
     */
    private static String getUTCString(Date date) {
        DATE_FORMAT.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        String str = DATE_FORMAT.format(date);
        str += " UTC";
        return str;
    }

    /**
     * @return the path for the cookie
     */
    private static String getPath() {
        String path = "/";
        return path;
    }

    /**
     * Protects some characters not allowed in cookie values.
     * As of RFC 6265, excluded characters are:
     * whitespace, double quote, comma, semicolon, and backslash
     * The equal character should be ok but some browsers mess with it,
     * so we protect this character too.
     *
     * @param in the original value
     * @return the protected value (safe to write in cookie)
     */
    private static String protect(String in) {
        String out=in;
        if(in!=null){
            out=out.replace(" ","/xspc/");
            out=out.replace("\"","/xquote/");
            out=out.replace(",","/xcomma/");
            out=out.replace(";","/xsemi/");
            out=out.replace("\\","/xback/");
            out=out.replace("=","/xeq/");
        }
        return out;
    }

    /**
     * Unprotects some characters not allowed in cookie values.
     *
     * @param in the protected value
     * @return the original value
     */
    private static String unprotect(String in) {
        String out=in;
        if(in!=null){
            out=out.replace("/xspc/"," ");
            out=out.replace("/xquote/","\"");
            out=out.replace("/xcomma/",",");
            out=out.replace("/xsemi/",";");
            out=out.replace("/xback/","\\");
            out=out.replace("/xeq/","=");
        }
        return out;
    }


}
