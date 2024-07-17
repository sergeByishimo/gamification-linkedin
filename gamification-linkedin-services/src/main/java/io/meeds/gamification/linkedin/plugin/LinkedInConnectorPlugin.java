package io.meeds.gamification.linkedin.plugin;

import io.meeds.gamification.plugin.ConnectorPlugin;
import io.meeds.gamification.service.ConnectorService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LinkedInConnectorPlugin extends ConnectorPlugin {

    private static final Log LOG                = ExoLogger.getLogger(LinkedInConnectorPlugin.class);

    private static final String     CONNECTOR_NAME     = "linkedin";

    private static final String     NAME               = "linkedin";

    @Autowired
    private ConnectorService connectorService;

    @PostConstruct
    public void initData() {
        LOG.info("Adding LinkedIn connector plugin");
        connectorService.addPlugin(this);
    }


    @Override
    public String getConnectorName() {
        LOG.info("Getting LinkedIn connector name");
        return CONNECTOR_NAME;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
