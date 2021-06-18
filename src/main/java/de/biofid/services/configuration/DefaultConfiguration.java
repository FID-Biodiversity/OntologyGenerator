package de.biofid.services.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class DefaultConfiguration {

    protected static final Logger logger = LogManager.getLogger();

    public static void applyGeneralSettingParameterToOntologyConfiguration(String settingKey, Configuration configuration) {
        if (!configuration.toJSONObject().has(settingKey)) {
            logger.info("The general configuration is missing the \"" + settingKey + "\"! It is expected to be" +
                    "configured for each ontology individually!");
            return;
        }

//
//        for (Object dataObject : getOntologyDataInConfiguration()) {
//            JSONObject jsonObject = (JSONObject) dataObject;
//            if (!jsonObject.has(settingKey)) {
//                jsonObject.put(settingKey, configuration.get(settingKey));
//            }
//        }
    }
}
