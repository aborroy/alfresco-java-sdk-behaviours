package org.alfresco.sdk.sample.event.behaviour.filter;

import org.alfresco.event.sdk.handling.filter.AbstractEventFilter;
import org.alfresco.event.sdk.model.v1.model.DataAttributes;
import org.alfresco.event.sdk.model.v1.model.NodeResource;
import org.alfresco.event.sdk.model.v1.model.RepoEvent;
import org.alfresco.event.sdk.model.v1.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AspectRemovedFilterPatched extends AbstractEventFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectRemovedFilterPatched.class);

    private final String acceptedAspect;

    private AspectRemovedFilterPatched(final String acceptedAspect) {
        this.acceptedAspect = Objects.requireNonNull(acceptedAspect);
    }

    public static AspectRemovedFilterPatched of(final String acceptedAspect) {
        return new AspectRemovedFilterPatched(acceptedAspect);
    }

    @Override
    public boolean test(RepoEvent<DataAttributes<Resource>> event) {
        LOGGER.debug("Checking filter for aspect {} and event {}", acceptedAspect, event);
        final NodeResource nodeResourceBefore = (NodeResource) event.getData().getResourceBefore();
        final NodeResource nodeResource = (NodeResource) event.getData().getResource();
        final boolean aspectExistedBefore = (nodeResourceBefore != null && nodeResourceBefore.getAspectNames().contains(acceptedAspect));
        final boolean aspectExists = (nodeResource != null && nodeResource.getAspectNames().contains(acceptedAspect));
        return isNodeEvent(event) && !aspectExists && aspectExistedBefore;
    }
}
