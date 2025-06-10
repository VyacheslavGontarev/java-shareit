package ru.practicum.shareit.comment.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public CommentDto createdComment(Long userId, Long itemId, CommentDto commentDto) {
        log.info("Создание комментария от пользователя {} на вещь {}", userId, itemId);
        User user = checkUser(userId);
        Item item = checkItem(itemId);
        checkBooking(itemId, user.getId());
        commentDto.setCreated(LocalDateTime.now());
        Comment commentEntity = CommentMapper.mapComment(commentDto, user, item);
        return CommentMapper.mapCommentDto(repository.save(commentEntity));
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти пользователя с id:" + userId));
    }

    private Item checkItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти вещь с id:" + itemId));
    }

    private Booking checkBooking(Long itemId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.getPostBooking(itemId, userId, now)
                .orElseThrow(() -> new ValidationException("Нельзя оставить комментарий, если не было брони"));
    }
}