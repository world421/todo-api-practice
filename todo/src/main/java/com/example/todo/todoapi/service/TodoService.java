package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.Todo;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoListResponseDTO create(final TodoCreateRequestDTO requestDTO)
            throws RuntimeException {        // 전달받은 값을 건들지 않는다 final ,, create
        todoRepository.save(requestDTO.toEntity());
        log.info("할 일 저장 완료! 제목:{}", requestDTO.getTitle());

        return retrieve();
    }


    public TodoListResponseDTO retrieve() {
        List<Todo> entityList = todoRepository.findAll();
        List<TodoDetailResponseDTO> dtoList
                = entityList.stream()
                /*  .map(todo -> new TodoDetailResponseDTO(todo))*/
                .map(TodoDetailResponseDTO::new)
                .collect(Collectors.toList());
        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }

    public TodoListResponseDTO delete (final String todoId) {
        try {
            todoRepository.deleteById(todoId);
        } catch (Exception e) {
            log.error(" id가 존재 하지 않아 삭제에 실패했습니다 - ID: {}, err:{} ", todoId, e.getMessage());
            throw new RuntimeException(e);
        }

        return retrieve();
    }

    public TodoListResponseDTO update(TodoModifyRequestDTO requestDTO)
        throws RuntimeException {
        Optional<Todo> targetEntity
                = todoRepository.findById(requestDTO.getId());

        targetEntity.ifPresent(todo -> {
            todo.setDone(requestDTO.isDone()); // false => true 화면단에서 뒤집어서 보낵
            todoRepository.save(todo);
        });
        return retrieve();
    }
}
