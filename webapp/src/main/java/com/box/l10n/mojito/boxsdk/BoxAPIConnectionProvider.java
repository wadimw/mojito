package com.box.l10n.mojito.boxsdk;

import com.box.sdk.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author wyau
 */
@Component
public class BoxAPIConnectionProvider {

    /**
     * logger
     */
    private static Logger logger = getLogger(BoxAPIConnectionProvider.class);

    @Autowired
    BoxSDKServiceConfigProvider boxSDKServiceConfigProvider;

    @Autowired
    BoxSDKJWTProvider boxSDKJWTProvider;

    private BoxAPIConnection boxAPIConnection;

    private BoxSDKServiceConfig boxSDKServiceConfig;

    /**
     * @return An API connection for use
     * @throws BoxSDKServiceException
     */
    public BoxAPIConnection getConnection() throws BoxSDKServiceException {
        logger.debug("Getting Box API Connection");
        BoxSDKServiceConfig currentConfig = boxSDKServiceConfigProvider.getConfig();

        if (!Objects.equals(boxSDKServiceConfig, currentConfig) || boxAPIConnection == null) {
            logger.debug("Saving config for used later");
            boxSDKServiceConfig = currentConfig;

            logger.debug("Getting new API connection because config has changed");
            boxAPIConnection = createBoxAPIConnection();
        }

        return boxAPIConnection;
    }

    /**
     * @return A fresh API connection using the current config
     * @throws BoxSDKServiceException
     */
    protected BoxAPIConnection createBoxAPIConnection() throws BoxSDKServiceException {
        logger.debug("Getting a new App User Connection using the current config");

        BoxSDKServiceConfig boxSDKServiceConfig = boxSDKServiceConfigProvider.getConfig();
        JWTEncryptionPreferences encryptionPref = boxSDKJWTProvider.getJWTEncryptionPreferences(boxSDKServiceConfig);

        BoxDeveloperEditionAPIConnection connection = new BoxDeveloperEditionAPIConnection(
            boxSDKServiceConfig.getAppUserId(),
            DeveloperEditionEntityType.USER,
            boxSDKServiceConfig.getClientId(),
            boxSDKServiceConfig.getClientSecret(),
            encryptionPref,
            getAccessTokenCache()
        );

        if (boxSDKServiceConfig.getProxyHost() != null && boxSDKServiceConfig.getProxyPort() != null) {
            logger.debug("Setting proxy for Box API connection");
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(boxSDKServiceConfig.getProxyHost(), boxSDKServiceConfig.getProxyPort()));
            connection.setProxy(proxy);

            if (boxSDKServiceConfig.getProxyUser() != null) {
                connection.setProxyBasicAuthentication(boxSDKServiceConfig.getProxyUser(), boxSDKServiceConfig.getProxyPassword());
            }
        }

        connection.authenticate();

        return connection;
    }

    /**
     * Gets a cache that can store the access token
     *
     * @return {@link IAccessTokenCache}
     */
    private IAccessTokenCache getAccessTokenCache() {
        // TODO It is a best practice to use an access token cache to prevent unneeded requests to Box for access tokens.
        // For production applications it is recommended to use a distributed cache like Memcached or Redis, and to
        // implement IAccessTokenCache to store and retrieve access tokens appropriately for your environment.
        return new InMemoryLRUAccessTokenCache(5);
    }
}
