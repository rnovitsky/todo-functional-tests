package com.bhft.todo

import com.bhft.todo.domain.controller.dto.TodoItem
import io.ktor.http.*
import io.qameta.allure.Description
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GET /todos")
class GetTodoTest : BaseTest() {
    private lateinit var generatedTodos: List<TodoItem>

    @BeforeEach
    fun generateTodos() {
        generatedTodos =
            step("Generate 5 items") { todoGenerator.createTodos(5) }
    }

    @Test
    @DisplayName("Should return empty list")
    @Description("GET should return empty list if there are no items")
    fun shouldReturnEmptyList() {
        todoGenerator.deleteAllGeneratedTodos()

        val todoListResponse =
            step("Retrieve list of items (no items created)") { todoController.getTodoList() }

        step("Check that empty list is returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.OK)
            assertThat(todoListResponse.body).isEmpty()
        }
    }

    @Test
    @DisplayName("Should return full list of items")
    @Description("GET should return all items if offset and limit are not passed")
    fun shouldGetFullTodoList() {
        val todoListResponse =
            step("Retrieve list of items") { todoController.getTodoList() }

        step("Check that all items are returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.OK)
            assertThat(todoListResponse.body).isEqualTo(generatedTodos)
        }
    }

    @Test
    @DisplayName("Should limit list")
    @Description("GET should return only specified number of items if limit is passed")
    fun shouldLimitList() {
        val limit = 2

        val todoListResponse =
            step("Retrieve list of items with limit 2") { todoController.getTodoList(limit = limit) }

        step("Check that only 2 first items are returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.OK)
            assertThat(todoListResponse.body).isEqualTo(generatedTodos.subList(0, limit))
        }
    }

    @Test
    @DisplayName("Should return list from given offset")
    @Description("GET should return only items after specified offset")
    fun shouldReturnListFromOffset() {
        val offset = 2

        val todoListResponse =
            step("Retrieve list of items with offset 2") { todoController.getTodoList(offset = offset) }

        step("Check that only items after position 2 are returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.OK)
            assertThat(todoListResponse.body).isEqualTo(generatedTodos.subList(offset, generatedTodos.size))
        }
    }

    @Test
    @DisplayName("Should return limited list from given offset")
    @Description("GET should return only specified amount of items after specified offset")
    fun shouldReturnLimitedListFromOffset() {
        val offset = 2
        val limit = 2

        val todoListResponse =
            step("Retrieve list of items with offset 2 and limit 2") {
                todoController.getTodoList(offset = offset, limit = limit)
            }

        step("Check that only 2 items after position 2 are returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.OK)
            assertThat(todoListResponse.body).isEqualTo(generatedTodos.subList(offset, offset + limit))
        }
    }

    @Test
    @DisplayName("Should return empty list if limit=0")
    @Description("GET should return no items if limit is 0")
    fun shouldGetEmptyListIfLimitZero() {
        val todoListResponse =
            step("Retrieve list of items with limit=0") { todoController.getTodoList(limit = 0) }

        step("Check that all items are returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.OK)
            assertThat(todoListResponse.body).isEmpty()
        }
    }

    @Test
    @DisplayName("Should return empty list if offset>size")
    @Description("GET should return no items if offset is greater than list size")
    fun shouldGetEmptyListIfOffsetGreateThanSize() {
        val todoListResponse =
            step("Retrieve list of items with offset 6") { todoController.getTodoList(offset = 6) }

        step("Check that all items are returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.OK)
            assertThat(todoListResponse.body).isEmpty()
        }
    }

    @Test
    @DisplayName("Should return error if offset is invalid")
    @Description("GET should return error if offset is negative")
    fun shouldReturnErrorOnInvalidOffset() {
        val todoListResponse =
            step("Retrieve list of items with negative offset") { todoController.getTodoList(offset = -1) }

        step("Check that error returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(todoListResponse.error).isEqualTo(INVALID_QUERY_STRING_ERROR)
        }
    }

    @Test
    @DisplayName("Should return error if limit is invalid")
    @Description("GET should return error if limit is negative")
    fun shouldReturnErrorOnInvalidLimit() {
        val todoListResponse =
            step("Retrieve list of items with negative limit") { todoController.getTodoList(limit = -1) }

        step("Check that error returned") {
            assertThat(todoListResponse.status).isEqualTo(HttpStatusCode.BadRequest)
            assertThat(todoListResponse.error).isEqualTo(INVALID_QUERY_STRING_ERROR)
        }
    }
}

private const val INVALID_QUERY_STRING_ERROR = "Invalid query string"
