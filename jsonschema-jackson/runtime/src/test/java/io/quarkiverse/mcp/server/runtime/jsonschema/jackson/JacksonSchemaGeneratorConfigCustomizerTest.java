package io.quarkiverse.mcp.server.runtime.jsonschema.jackson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;

import io.quarkiverse.mcp.server.runtime.jsonschema.jackson.config.JacksonBuildTimeConfig;

@ExtendWith(MockitoExtension.class)
class JacksonSchemaGeneratorConfigCustomizerTest {

    @Mock
    SchemaGeneratorConfigBuilder builderMock;

    @Mock
    JacksonBuildTimeConfig jacksonBuildTimeConfigMock;

    @InjectMocks
    JacksonSchemaGeneratorConfigCustomizer customizer;

    @Captor
    ArgumentCaptor<JacksonModule> jacksonModuleCaptor;

    @Test
    void testCustomize_allOptionsTrue() throws Exception {
        // Arrange
        when(jacksonBuildTimeConfigMock.respectJsonpropertyRequired()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.flattenedEnumsFromJsonvalue()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.flattenedEnumsFromJsonproperty()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.includeOnlyJsonpropertyAnnotatedMethods()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.jsonidentityReferenceAlwaysAsId()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.alwaysRefSubtypes()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.inlineTransformedSubtypes()).thenReturn(true);
        // New options
        when(jacksonBuildTimeConfigMock.respectJsonpropertyOrder()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.ignorePropertyNamingStrategy()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.skipSubtypeLookup()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.ignoreTypeInfoTransform()).thenReturn(true);

        EnumSet<JacksonOption> expectedOptions = EnumSet.of(
                JacksonOption.RESPECT_JSONPROPERTY_REQUIRED,
                JacksonOption.FLATTENED_ENUMS_FROM_JSONVALUE,
                JacksonOption.FLATTENED_ENUMS_FROM_JSONPROPERTY,
                JacksonOption.INCLUDE_ONLY_JSONPROPERTY_ANNOTATED_METHODS,
                JacksonOption.JSONIDENTITY_REFERENCE_ALWAYS_AS_ID,
                JacksonOption.ALWAYS_REF_SUBTYPES,
                JacksonOption.INLINE_TRANSFORMED_SUBTYPES,
                JacksonOption.RESPECT_JSONPROPERTY_ORDER,
                JacksonOption.IGNORE_PROPERTY_NAMING_STRATEGY,
                JacksonOption.SKIP_SUBTYPE_LOOKUP,
                JacksonOption.IGNORE_TYPE_INFO_TRANSFORM);

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(jacksonModuleCaptor.capture());
        JacksonModule capturedModule = jacksonModuleCaptor.getValue();
        assertNotNull(capturedModule);

        var actualOptions = getOptionsFromModule(capturedModule);
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    void testCustomize_allOptionsFalse() throws Exception {
        // Arrange
        when(jacksonBuildTimeConfigMock.respectJsonpropertyRequired()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.flattenedEnumsFromJsonvalue()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.flattenedEnumsFromJsonproperty()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.includeOnlyJsonpropertyAnnotatedMethods()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.jsonidentityReferenceAlwaysAsId()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.alwaysRefSubtypes()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.inlineTransformedSubtypes()).thenReturn(false);
        // New options
        when(jacksonBuildTimeConfigMock.respectJsonpropertyOrder()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.ignorePropertyNamingStrategy()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.skipSubtypeLookup()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.ignoreTypeInfoTransform()).thenReturn(false);

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(jacksonModuleCaptor.capture());
        JacksonModule capturedModule = jacksonModuleCaptor.getValue();
        assertNotNull(capturedModule);

        var actualOptions = getOptionsFromModule(capturedModule);
        assertTrue(actualOptions.isEmpty(), "Expected no options to be set");
    }

    @Test
    void testCustomize_mixedOptions_respectsDefaults() throws Exception {
        // Arrange: Set specific values, including overriding defaults for new options
        when(jacksonBuildTimeConfigMock.respectJsonpropertyRequired()).thenReturn(true); // Default true
        when(jacksonBuildTimeConfigMock.flattenedEnumsFromJsonvalue()).thenReturn(false); // Default true
        when(jacksonBuildTimeConfigMock.flattenedEnumsFromJsonproperty()).thenReturn(true); // Default true
        when(jacksonBuildTimeConfigMock.includeOnlyJsonpropertyAnnotatedMethods()).thenReturn(false); // Default true
        when(jacksonBuildTimeConfigMock.jsonidentityReferenceAlwaysAsId()).thenReturn(true); // Default true
        when(jacksonBuildTimeConfigMock.alwaysRefSubtypes()).thenReturn(false); // Default true
        when(jacksonBuildTimeConfigMock.inlineTransformedSubtypes()).thenReturn(true); // Default true

        // New options (defaults: respectOrder=true, ignoreStrategy=false, skipSubtype=false, ignoreTypeInfo=false)
        when(jacksonBuildTimeConfigMock.respectJsonpropertyOrder()).thenReturn(false); // Override default true
        when(jacksonBuildTimeConfigMock.ignorePropertyNamingStrategy()).thenReturn(true); // Override default false
        when(jacksonBuildTimeConfigMock.skipSubtypeLookup()).thenReturn(true); // Override default false
        when(jacksonBuildTimeConfigMock.ignoreTypeInfoTransform()).thenReturn(false); // Match default false

        EnumSet<JacksonOption> expectedOptions = EnumSet.noneOf(JacksonOption.class);
        expectedOptions.add(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);
        // false: flattenedEnumsFromJsonvalue
        expectedOptions.add(JacksonOption.FLATTENED_ENUMS_FROM_JSONPROPERTY);
        // false: includeOnlyJsonpropertyAnnotatedMethods
        expectedOptions.add(JacksonOption.JSONIDENTITY_REFERENCE_ALWAYS_AS_ID);
        // false: alwaysRefSubtypes
        expectedOptions.add(JacksonOption.INLINE_TRANSFORMED_SUBTYPES);
        // New options
        // false: respectJsonpropertyOrder
        expectedOptions.add(JacksonOption.IGNORE_PROPERTY_NAMING_STRATEGY);
        expectedOptions.add(JacksonOption.SKIP_SUBTYPE_LOOKUP);
        // false: ignoreTypeInfoTransform

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(jacksonModuleCaptor.capture());
        JacksonModule capturedModule = jacksonModuleCaptor.getValue();
        assertNotNull(capturedModule);

        var actualOptions = getOptionsFromModule(capturedModule);
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    void testCustomize_onlyNewOptionsTrue_othersFalse() throws Exception {
        // Arrange
        when(jacksonBuildTimeConfigMock.respectJsonpropertyRequired()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.flattenedEnumsFromJsonvalue()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.flattenedEnumsFromJsonproperty()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.includeOnlyJsonpropertyAnnotatedMethods()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.jsonidentityReferenceAlwaysAsId()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.alwaysRefSubtypes()).thenReturn(false);
        when(jacksonBuildTimeConfigMock.inlineTransformedSubtypes()).thenReturn(false);

        // New options - all true
        when(jacksonBuildTimeConfigMock.respectJsonpropertyOrder()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.ignorePropertyNamingStrategy()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.skipSubtypeLookup()).thenReturn(true);
        when(jacksonBuildTimeConfigMock.ignoreTypeInfoTransform()).thenReturn(true);

        EnumSet<JacksonOption> expectedOptions = EnumSet.of(
                JacksonOption.RESPECT_JSONPROPERTY_ORDER,
                JacksonOption.IGNORE_PROPERTY_NAMING_STRATEGY,
                JacksonOption.SKIP_SUBTYPE_LOOKUP,
                JacksonOption.IGNORE_TYPE_INFO_TRANSFORM);

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(jacksonModuleCaptor.capture());
        JacksonModule capturedModule = jacksonModuleCaptor.getValue();
        assertNotNull(capturedModule);

        var actualOptions = getOptionsFromModule(capturedModule);
        assertEquals(expectedOptions, actualOptions);
    }

    private Set<JacksonOption> getOptionsFromModule(JacksonModule module)
            throws NoSuchFieldException, IllegalAccessException {
        var optionsField = JacksonModule.class.getDeclaredField("options");
        optionsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var actualOptions = (Set<JacksonOption>) optionsField.get(module);
        return actualOptions;
    }
}
