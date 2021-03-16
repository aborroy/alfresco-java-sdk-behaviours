package org.alfresco.sdk.sample.event.behaviour.filter;

import org.alfresco.event.sdk.handling.filter.AbstractEventFilter;
import org.alfresco.event.sdk.model.v1.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentFilter extends AbstractEventFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentFilter.class);

    private ContentFilter() {
    }

    public static ContentFilter get() {
        return new ContentFilter();
    }

    @Override
    public boolean test(final RepoEvent<DataAttributes<Resource>> event) {
        LOGGER.debug("Checking if event {} includes content", event);
        return isNodeEvent(event) && checkContent(event);
    }

    private boolean checkContent(final RepoEvent<DataAttributes<Resource>> event) {
        final NodeResource nodeResource = (NodeResource) event.getData().getResource();
        final ContentInfo contentInfo = nodeResource.getContent();
        return contentInfo != null && contentInfo.getEncoding() != null && contentInfo.getMimeType() != null && contentInfo.getSizeInBytes() != null;
    }
}
