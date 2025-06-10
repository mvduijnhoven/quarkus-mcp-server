package io.quarkiverse.mcp.server.runtime.jsonschema.javax.validation;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationOption;
import io.quarkiverse.mcp.server.runtime.jsonschema.javax.validation.config.JavaxValidationBuildTimeConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JavaxValidationSchemaGeneratorConfigCustomizerUnitTest {

    @Mock
    JavaxValidationBuildTimeConfig buildTimeConfigMock;

    @Mock
    SchemaGeneratorConfigBuilder builderMock;

    @Captor
    ArgumentCaptor<JavaxValidationModule> moduleCaptor;

    @InjectMocks
    JavaxValidationSchemaGeneratorConfigCustomizer customizer;

    private EnumSet<JavaxValidationOption> getOptionsFromModule(JavaxValidationModule module) throws NoSuchFieldException, IllegalAccessException {
        Field optionsField = JavaxValidationModule.class.getDeclaredField("options");
        optionsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumSet<JavaxValidationOption> actualOptions = (EnumSet<JavaxValidationOption>) optionsField.get(module);
        return actualOptions;
    }

    @Test
    void testCustomize_allConfigOptionsTrue() throws Exception {
        // Arrange
        when(buildTimeConfigMock.notNullableFieldIsRequired).thenReturn(true);
        when(buildTimeConfigMock.notNullableMethodIsRequired).thenReturn(true);
        when(buildTimeConfigMock.includePatternProperties).thenReturn(true);
        // includeValidationMessages is removed from config

        EnumSet<JavaxValidationOption> expectedOptions = EnumSet.of(
                JavaxValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED,
                JavaxValidationOption.NOT_NULLABLE_METHOD_IS_REQUIRED,
                JavaxValidationOption.INCLUDE_PATTERN_PROPERTIES
        );

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(moduleCaptor.capture());
        JavaxValidationModule capturedModule = moduleCaptor.getValue();
        assertNotNull(capturedModule);

        EnumSet<JavaxValidationOption> actualOptions = getOptionsFromModule(capturedModule);
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    void testCustomize_allConfigOptionsFalse_usesDefaultModule() throws Exception {
        // Arrange
        when(buildTimeConfigMock.notNullableFieldIsRequired).thenReturn(false);
        when(buildTimeConfigMock.notNullableMethodIsRequired).thenReturn(false);
        when(buildTimeConfigMock.includePatternProperties).thenReturn(false);

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(moduleCaptor.capture());
        JavaxValidationModule capturedModule = moduleCaptor.getValue();
        assertNotNull(capturedModule);

        EnumSet<JavaxValidationOption> actualOptions = getOptionsFromModule(capturedModule);

        // When all our managed configs are false, the customizer calls `new JavaxValidationModule()`.
        // The default constructor of JavaxValidationModule enables ALL standard JavaxValidationOptions.
        EnumSet<JavaxValidationOption> allStandardJavaxOptions = EnumSet.allOf(JavaxValidationOption.class);
        assertEquals(allStandardJavaxOptions, actualOptions);
    }

    @Test
    void testCustomize_mixedConfigOptions() throws Exception {
        // Arrange
        when(buildTimeConfigMock.notNullableFieldIsRequired).thenReturn(true);
        when(buildTimeConfigMock.notNullableMethodIsRequired).thenReturn(false);
        when(buildTimeConfigMock.includePatternProperties).thenReturn(true);

        EnumSet<JavaxValidationOption> expectedOptions = EnumSet.of(
                JavaxValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED,
                JavaxValidationOption.INCLUDE_PATTERN_PROPERTIES
        );

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(moduleCaptor.capture());
        JavaxValidationModule capturedModule = moduleCaptor.getValue();
        assertNotNull(capturedModule);

        EnumSet<JavaxValidationOption> actualOptions = getOptionsFromModule(capturedModule);
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    void testCustomize_oneOptionTrue_othersFalse() throws Exception {
        // Arrange
        when(buildTimeConfigMock.notNullableFieldIsRequired).thenReturn(false);
        when(buildTimeConfigMock.notNullableMethodIsRequired).thenReturn(true); // This one true
        when(buildTimeConfigMock.includePatternProperties).thenReturn(false);

        EnumSet<JavaxValidationOption> expectedOptions = EnumSet.of(
                JavaxValidationOption.NOT_NULLABLE_METHOD_IS_REQUIRED
        );

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(moduleCaptor.capture());
        JavaxValidationModule capturedModule = moduleCaptor.getValue();
        assertNotNull(capturedModule);

        EnumSet<JavaxValidationOption> actualOptions = getOptionsFromModule(capturedModule);
        assertEquals(expectedOptions, actualOptions);
    }
}
