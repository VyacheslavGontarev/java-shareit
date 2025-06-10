package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.CommentDto;

public interface CommentService {

    CommentDto createdComment(Long userId, Long itemId, CommentDto commentDto);
}
