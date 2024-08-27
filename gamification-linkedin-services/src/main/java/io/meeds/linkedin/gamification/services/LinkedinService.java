package io.meeds.linkedin.gamification.services;

import com.github.scribejava.core.model.OAuth2AccessToken;
import io.meeds.gamification.model.RemoteConnectorSettings;
import io.meeds.gamification.service.ConnectorSettingService;
import io.meeds.gamification.utils.Utils;
import io.meeds.gamification.websocket.entity.ConnectorIdentifierModification;
import io.meeds.linkedin.gamification.plugin.LinkedInConnectorPlugin;
import io.meeds.linkedin.gamification.storage.LinkedinAccountStorage;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkedinService {

    private static final Log LOG            = ExoLogger.getLogger(LinkedinService.class);

    private static final String     ADMIN_CONNECTOR_SCOPE    = "r_organization_followers,r_organization_social";

    public static final String      TOKEN_CREATED_EVENT_NAME    = "linkedin.connector.admin.token.created";

    @Autowired
    private ConnectorSettingService connectorSettingService;
    @Autowired
    private LinkedinAccountStorage linkedinAccountStorage;
    @Autowired
    private LinkedInConnectorPlugin linkedInConnectorPlugin;
    @Autowired
    private ListenerService listenerService;

    public RemoteConnectorSettings getConnectorSettings(Identity identity) throws IllegalAccessException {
        if (!Utils.isRewardingManager(identity.getUserId())) {
            throw new IllegalAccessException("The user is not authorized to get LinkedIn connector settings");
        }

        LOG.debug("LinkedinService.getConnectorSettings: started");

        return connectorSettingService.getConnectorSettings(
                LinkedInConnectorPlugin.CONNECTOR_NAME);
    }

    public void validateOathCode(Identity identity, String code) throws IllegalAccessException {
        if (!Utils.isRewardingManager(identity.getUserId())) {
            throw new IllegalAccessException("The user is not authorized to save or update LinkedIn Bearer Token");
        }

        LOG.debug("SettingsService.validateOathCode: started");
        LOG.debug("SettingsService.validateOathCode: identity : " + identity);
        LOG.debug("SettingsService.validateOathCode: code : " + code);

        OAuth2AccessToken oAuth2AccessToken = linkedInConnectorPlugin.getAccessToken(code, ADMIN_CONNECTOR_SCOPE);

        if (oAuth2AccessToken == null) {
            throw new IllegalStateException("Error getting access token");
        }

        LOG.debug("SettingsService.validateOathCode: oAuth2AccessToken: " + oAuth2AccessToken.getAccessToken());
        LOG.debug("SettingsService.validateOathCode: getExpiresIn: " + oAuth2AccessToken.getExpiresIn());

        linkedinAccountStorage.saveLinkedinBearerToken(oAuth2AccessToken.getAccessToken());

        try {
            listenerService.broadcast(TOKEN_CREATED_EVENT_NAME,
                    new ConnectorIdentifierModification("connectorIdentifierUpdated",
                            connectorName,
                            username,
                            connectorUserId),
                    username);
        } catch (Exception e) {
            LOG.warn("Error while broadcasting operation '{}' for connector {}", "connectorIdentifierUpdated", connectorName, e);
        }
    }




}
