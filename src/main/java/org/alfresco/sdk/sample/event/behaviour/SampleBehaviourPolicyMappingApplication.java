/*
 * Copyright 2021-2021 Alfresco Software, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.alfresco.sdk.sample.event.behaviour;

import org.alfresco.event.sdk.handling.filter.*;
import org.alfresco.event.sdk.integration.EventChannels;
import org.alfresco.event.sdk.integration.filter.IntegrationEventFilter;
import org.alfresco.sdk.sample.event.behaviour.filter.AspectFilter;
import org.alfresco.sdk.sample.event.behaviour.filter.AspectRemovedFilterPatched;
import org.alfresco.sdk.sample.event.behaviour.filter.ContentFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageHandlingException;

@SpringBootApplication
public class SampleBehaviourPolicyMappingApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleBehaviourPolicyMappingApplication.class);

    public static void main(final String[] args) {
        SpringApplication.run(SampleBehaviourPolicyMappingApplication.class, args);
    }

    @Bean
    public IntegrationFlow logError() {
        return IntegrationFlows.from(EventChannels.ERROR).handle(t -> {
            LOGGER.info("Error: {}", t.getPayload().toString());
            MessageHandlingException exception = (MessageHandlingException) t.getPayload();
            exception.printStackTrace();
        }).get();
    }

    @Bean
    public IntegrationFlow logContentBehaviourPolicy() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_CREATED
                        .and(ContentFilter.get())
                        .or(ContentChangedFilter.get())))
                .handle(t -> LOGGER.info("onContentPropertyUpdated / onContentUpdate! - Event: {}", t.getPayload().toString()))
                .get();
    }

    @Bean
    public IntegrationFlow logCopyPolicy() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_CREATED
                        .and(AspectFilter.of("cm:copiedfrom"))))
                .handle(t -> LOGGER.info("onCopyNode! - Event: {}", t.getPayload().toString()))
                .get();
    }

    /**
     * Using constant "cm:summarizable" aspect by now
     */
    @Bean
    public IntegrationFlow logAddAspect() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_CREATED
                        .and(AspectFilter.of("cm:summarizable"))
                        .or(AspectAddedFilter.of("cm:summarizable"))))
                .handle(t -> LOGGER.info("onAddAspect! - Event: {}", t.getPayload().toString()))
                .get();
    }

    // FIXME Not working
    @Bean
    public IntegrationFlow logCreateAssociation() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.PEER_ASSOC_CREATED))
                .handle(t -> LOGGER.info("onCreateAssociation! - Event: {}", t.getPayload().toString()))
                .get();
    }

    // FIXME Not working
    @Bean
    public IntegrationFlow logCreateChildAssociation() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.CHILD_ASSOC_CREATED))
                .handle(t -> LOGGER.info("onCreateChildAssociation! - Event: {}", t.getPayload().toString()))
                .get();
    }

    @Bean
    public IntegrationFlow logCreateNode() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_CREATED))
                .handle(t -> LOGGER.info("onCreateNode! - Event: {}", t.getPayload().toString()))
                .get();
    }

    // FIXME Not working
    @Bean
    public IntegrationFlow logDeleteAssociation() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.PEER_ASSOC_DELETED))
                .handle(t -> LOGGER.info("onDeleteAssociation! - Event: {}", t.getPayload().toString()))
                .get();
    }

    // FIXME Not working
    @Bean
    public IntegrationFlow logDeleteChildAssociation() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.CHILD_ASSOC_DELETED))
                .handle(t -> LOGGER.info("onDeleteChildAssociation! - Event: {}", t.getPayload().toString()))
                .get();
    }

    @Bean
    public IntegrationFlow logDeleteNode() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_DELETED))
                .handle(t -> LOGGER.info("onDeleteNode! - Event: {}", t.getPayload().toString()))
                .get();
    }

    @Bean
    public IntegrationFlow logMoveNode() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(NodeMovedFilter.get()))
                .handle(t -> LOGGER.info("onModeNode! - Event: {}", t.getPayload().toString()))
                .get();
    }

    /**
     * Using constant "cm:summarizable" aspect by now
     * FIXME AspectRemovedFilter is not checking right the aspect existed before the update,
     * a local AspectRemovedFilterPatched class has been created
     * Issue has been created: https://alfresco.atlassian.net/browse/REPO-5605
     */
    @Bean
    public IntegrationFlow logRemoveAspect() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_UPDATED
                        .and(AspectRemovedFilterPatched.of("cm:summarizable"))))
                .handle(t -> LOGGER.info("onRemoveAspect! - Event: {}", t.getPayload().toString()))
                .get();
    }

    /**
     * Using constant "smf:smartFolderTemplate" type by now
     */
    @Bean
    public IntegrationFlow logSetNodeType() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_UPDATED
                        .and(NodeTypeChangedFilter.get())
                        .and(NodeTypeFilter.of("smf:smartFolderTemplate"))))
                .handle(t -> LOGGER.info("onSetNodeType! - Event: {}", t.getPayload().toString()))
                .get();
    }

    @Bean
    public IntegrationFlow logUpdateNode() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_UPDATED))
                .handle(t -> LOGGER.info("onNodeUpdate! - Event: {}", t.getPayload().toString()))
                .get();
    }

    @Bean
    public IntegrationFlow logUpdateNodeProperties() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_UPDATED))
                .handle(t -> LOGGER.info("onNodeUpdateProperties! - Event: {}", t.getPayload().toString()))
                .get();
    }

    @Bean
    public IntegrationFlow logAfterCreateVersion() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_UPDATED
                        .and(PropertyChangedFilter.of("cm:versionLabel"))))
                .handle(t -> LOGGER.info("afterCreateVersion! - Event: {}", t.getPayload().toString()))
                .get();
    }

    @Bean
    public IntegrationFlow logOnCreateVersion() {
        return IntegrationFlows.from(EventChannels.MAIN)
                .filter(IntegrationEventFilter.of(EventTypeFilter.NODE_UPDATED
                        .and(PropertyChangedFilter.of("cm:versionLabel"))))
                .handle(t -> LOGGER.info("onCreateVersion! - Event: {}", t.getPayload().toString()))
                .get();
    }

}
