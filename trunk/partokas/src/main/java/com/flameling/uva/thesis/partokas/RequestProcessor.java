package com.flameling.uva.thesis.partokas;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("restriction")
public class RequestProcessor {
	
	private Set<String> entryPoints;
	public static final int nonceCacheSize = 100;
	private Random randomSource;
	
	public RequestProcessor(){
		this.entryPoints = new HashSet<String>();
		this.entryPoints.add("/index.jsp");
		this.entryPoints.add("/index.action");
		this.entryPoints.add("/security/login.action");
		this.entryPoints.add("/admin/index.jsp");
		this.randomSource = new SecureRandom();
	}
	
	HttpServletResponse process(HttpServletRequest request, HttpServletResponse response)
			throws NoValidTokenException{
		

        HttpServletResponse wResponse = response;
        HttpSession session = request.getSession(false);
        LruCache<String> nonceCache = getCacheFromSession(session);
        if (!skipNonceCheck(request) && hasNoValidToken(nonceCache, request)) {
        		//res.sendError(HttpServletResponse.SC_FORBIDDEN);
        	throw new NoValidTokenException();
        } else {
        	putCacheOnSession(nonceCache, session, request);
        	session = request.getSession(false);
        	nonceCache = (LruCache<String>) session.getAttribute(Constants.CSRF_NONCE_SESSION_ATTR_NAME);
            String newNonce = generateNonce();
            nonceCache.add(newNonce);
            //wResponse = new CsrfResponseWrapper(response, newNonce);
        }
        return wResponse;
    }
	
	private boolean hasNoValidToken(LruCache<String> nonceCache, HttpServletRequest req){
		boolean result = false;
		String previousNonce = req.getParameter(Constants.CSRF_NONCE_REQUEST_PARAM);
        if(previousNonce != null && previousNonce.equals("012345678901234567890123456789AB"))
        	return false;
		if (previousNonce == null ||
                !nonceCache.contains(previousNonce)) {
            result = true;
        }
        return result;
	}
	
	@SuppressWarnings("unchecked")
	private LruCache<String> getCacheFromSession(HttpSession session){
//        LruCache<String> nonceCache = (session == null) ? null
//                : (LruCache<String>) session.getAttribute(
//                        Constants.CSRF_NONCE_SESSION_ATTR_NAME);
		LruCache<String> nonceCache;
		if(session == null || session.getAttribute(Constants.CSRF_NONCE_SESSION_ATTR_NAME) == null){
			nonceCache = new LruCache<String>(nonceCacheSize);
		} else {
			nonceCache = (LruCache<String>) session.getAttribute(Constants.CSRF_NONCE_SESSION_ATTR_NAME);
		}
		return nonceCache;
	}
	
	/**
	 * If <code>nonceCache</code> is <code>null</code> a new {@link LruCache nonceCache} is created.
	 * If <code>session</code> is <code>null</code> a session is obtained from the {@link HttpServletRequest request}.
	 * @param nonceCache
	 * @param session
	 * @param req
	 */
	private void putCacheOnSession(LruCache<String> nonceCache, HttpSession session, HttpServletRequest req){
         if (session == null) {
             session = req.getSession(true);
         }
         session.setAttribute(
                 Constants.CSRF_NONCE_SESSION_ATTR_NAME, nonceCache);
	}
	
	private boolean skipNonceCheck(HttpServletRequest req){
		boolean result = false;
		if (Constants.METHOD_GET.equals(req.getMethod())) {
            String path = req.getServletPath();
            if (req.getPathInfo() != null) {
                path = path + req.getPathInfo();
            }
            
            if (entryPoints.contains(path)) {
                result = true;
            }
        }
		return result;
	}
	
	/**
     * Generate a once time token (nonce) for authenticating subsequent
     * requests. This will also add the token to the session. The nonce
     * generation is a simplified version of ManagerBase.generateSessionId().
     * 
     */
    protected String generateNonce() {
        byte random[] = new byte[16];

        // Render the result as a String of hexadecimal digits
        StringBuilder buffer = new StringBuilder();

        randomSource.nextBytes(random);
       
        for (int j = 0; j < random.length; j++) {
            byte b1 = (byte) ((random[j] & 0xf0) >> 4);
            byte b2 = (byte) (random[j] & 0x0f);
            if (b1 < 10)
                buffer.append((char) ('0' + b1));
            else
                buffer.append((char) ('A' + (b1 - 10)));
            if (b2 < 10)
                buffer.append((char) ('0' + b2));
            else
                buffer.append((char) ('A' + (b2 - 10)));
        }

        return "012345678901234567890123456789AB";//buffer.toString();
    }
	
    protected static class LruCache<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        // Although the internal implementation uses a Map, this cache
        // implementation is only concerned with the keys.
        private final Map<T,T> cache;
        
        public LruCache(final int cacheSize) {
            cache = new LinkedHashMap<T,T>() {
                private static final long serialVersionUID = 1L;
                @Override
                protected boolean removeEldestEntry(Map.Entry<T,T> eldest) {
                    if (size() > cacheSize) {
                        return true;
                    }
                    return false;
                }
            };
        }
        
        public void add(T key) {
            synchronized (cache) {
                cache.put(key, null);
            }
        }

        public boolean contains(T key) {
            synchronized (cache) {
                return cache.containsKey(key);
            }
        }
    }
}
