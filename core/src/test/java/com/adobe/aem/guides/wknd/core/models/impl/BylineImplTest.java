package com.adobe.aem.guides.wknd.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.aem.guides.wknd.core.models.Byline;
import com.adobe.cq.wcm.core.components.models.Image;
import com.google.common.collect.ImmutableList;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class BylineImplTest {
    
    private final AemContext context = new AemContext();

    @Mock
    private Image image;

    @Mock
    private ModelFactory modelFactory;

    @BeforeEach
    void setUp() throws Exception {
        context.addModelsForClasses(BylineImpl.class);
        context.load().json("/com/adobe/aem/guides/wknd/core/models/impl/BylineImplTest.json", "/content");

        lenient().when(modelFactory.getModelFromWrappedRequest(eq(context.request()), any(Resource.class), eq(Image.class))).thenReturn(image);

        context.registerService(ModelFactory.class, modelFactory, org.osgi.framework.Constants.SERVICE_RANKING, Integer.MAX_VALUE);
    }

    @Test
    void testGetName() {
        final String expected = "John Abraham";

        context.currentResource("/content/byline");

        Byline byline = context.request().adaptTo(Byline.class);

        String actual = byline.getName();

        assertEquals(expected, actual);
    }

    @Test
    void testGetOccupations() {
        List<String> expected = new ImmutableList.Builder<String>()
                            .add("Blogger")
                            .add("Photographer")
                            .add("YouTuber")
                            .build();

        context.currentResource("/content/byline");
        
        Byline byline = context.request().adaptTo(Byline.class);

        List<String> actual = byline.getOccupations();

        assertEquals(expected, actual);
    }

    @Test
    void testIsEmpty() {
        context.currentResource("/content/empty");

        Byline byline = context.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsEmpty_WithoutName() {
        context.currentResource("/content/without-name");

        Byline byline = context.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsEmpty_WithoutOccupations() {
        context.currentResource("/content/without-occupations");

        Byline byline = context.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsEmpty_WithoutImage() {
        context.currentResource("/content/byline");

        lenient().when(modelFactory.getModelFromWrappedRequest(eq(context.request()),
            any(Resource.class),
            eq(Image.class))).thenReturn(null);

        Byline byline = context.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsEmpty_WithoutImageSrc() {
        context.currentResource("/content/byline");

        when(image.getSrc()).thenReturn("");

        Byline byline = context.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsNotEmpty() {
        context.currentResource("/content/byline");
        when(image.getSrc()).thenReturn("/content/bio.png");

        Byline byline = context.request().adaptTo(Byline.class);

        assertFalse(byline.isEmpty());
    }
}
