package io.github.catalogo.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.catalogo.admin.ControllerTest;
import io.github.catalogo.admin.application.category.create.CreateCategoryOutput;
import io.github.catalogo.admin.application.category.create.CreateCategoryUseCase;
import io.github.catalogo.admin.application.category.retrieve.get.CategoryOutput;
import io.github.catalogo.admin.application.category.retrieve.get.GetCategoryByIddUseCase;
import io.github.catalogo.admin.application.category.update.UpdateCategoryOutput;
import io.github.catalogo.admin.application.category.update.UpdateCategoryUseCase;
import io.github.catalogo.admin.domain.category.Category;
import io.github.catalogo.admin.domain.category.CategoryId;
import io.github.catalogo.admin.domain.exceptions.DomainException;
import io.github.catalogo.admin.domain.exceptions.NotFoundException;
import io.github.catalogo.admin.domain.validation.Error;
import io.github.catalogo.admin.domain.validation.handler.Notification;
import io.github.catalogo.admin.infrastructure.category.models.CreateCategoryApiInput;
import io.github.catalogo.admin.infrastructure.category.models.UpdateCategoryApiInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITests {

    final static String FIELD_CANNOT_BE_NULL_ERROR_MESSAGE = "'%s' should not be null";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIddUseCase getCategoryByIddUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenAnInvalidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectIsActive = true;

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from(CategoryId.from("123"))));

        final var anInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectIsActive);

        this.mvc.perform(MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput)))
                .andExpectAll(
                        status().isCreated(),
                        header().string("location", "/categories/123")
                );
        verify(createCategoryUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedDescription, cmd.description())
                && Objects.equals(expectIsActive, cmd.isActive())));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateCategory_shouldThrowAnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var expectedMessage = format(FIELD_CANNOT_BE_NULL_ERROR_MESSAGE, expectedName);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var anInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectIsActive);

        this.mvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(anInput)))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("location", nullValue()))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createCategoryUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectIsActive, cmd.isActive())));
    }

    @Test
    void givenAnInvalidCommand_whenCallsCreateCategory_shouldThrowAnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectIsActive = true;
        final var expectedMessage = format(FIELD_CANNOT_BE_NULL_ERROR_MESSAGE, expectedName);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final var anInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectIsActive);

        this.mvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(anInput)))
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("location", nullValue()))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createCategoryUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectIsActive, cmd.isActive())));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIddUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(aCategory));

        mvc.perform(get("/categories/{id}", expectedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andDo(print());

        verify(getCategoryByIddUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Category with id 123 was not found";
        final var expectedId = CategoryId.from("123");

        when(getCategoryByIddUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));;


        mvc.perform(get("/categories/{id}", expectedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andDo(print());
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        final var aCommand =
                new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);


        mvc.perform(put("/categories/{id}", expectedId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(aCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedMessage = "'name' should not be null";

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var aCommand =
                new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        mvc.perform(put("/categories/{id}", expectedId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(aCommand)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)))
                .andDo(print());

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() throws Exception {
        final var expectedId = "not-found";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with id not-found was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryId.from(expectedId)));

        final var aCommand =
                new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        mvc.perform(put("/categories/{id}", expectedId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(aCommand)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andDo(print());;

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
}
