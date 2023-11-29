package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.Todo;
import com.example.todo.todoapi.repository.TodoRepository;
import com.example.todo.userapi.api.UserController;
import com.example.todo.userapi.entity.User;
import com.example.todo.userapi.repository.UserRepository;
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
    private final UserRepository userRepository;

    public TodoListResponseDTO create(
            final TodoCreateRequestDTO requestDTO,
            final String userId
    )
            throws RuntimeException {        // 전달받은 값을 건들지 않는다 final ,, create

        User user = getUser(userId);
        // 이제는 할 일 등록은 회원만 할 수 있도록 세팅
        // toEntity 의 매개값으로 User 엔터티도 함께 전달해야합니다.
        // => userId로 회원 엔터티를 조회해야함
        todoRepository.save(requestDTO.toEntity(user));
        log.info("할 일 저장 완료! 제목 : {}", requestDTO.getTitle());

        return retrieve(userId);
    }


    public TodoListResponseDTO retrieve(String userId) {

        // 로그인한 유저의 정보를 데이터베이스 조회

        User user = getUser(userId);

        List<Todo> entityList = todoRepository.findAllByUser(user);
        List<TodoDetailResponseDTO> dtoList
                = entityList.stream()
                /*  .map(todo -> new TodoDetailResponseDTO(todo))*/
                .map(TodoDetailResponseDTO::new)
                .collect(Collectors.toList());
        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }

    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("회원정보가 없습니다")
        );
        return user;
    }

    public TodoListResponseDTO delete (final String todoId, final String userId) {
        try {
            todoRepository.deleteById(todoId);
        } catch (Exception e) {
            log.error(" id가 존재 하지 않아 삭제에 실패했습니다 - ID: {}, err:{} ", todoId, e.getMessage());
            throw new RuntimeException(e);
        }

        return retrieve(userId);
    }

    public TodoListResponseDTO update(final TodoModifyRequestDTO requestDTO, final String userId)
        throws RuntimeException {
        Optional<Todo> targetEntity
                = todoRepository.findById(requestDTO.getId());

        targetEntity.ifPresent(todo -> {
            todo.setDone(requestDTO.isDone()); // false => true 화면단에서 뒤집어서 보낵
            todoRepository.save(todo);
        });
        return retrieve(userId);
    }
}
