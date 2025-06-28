package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto item) {
        return itemService.createItem(userId, item);
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping
    public List<ItemWithCommentDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam("text") String text) {
        return itemService.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createdComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long itemId,
                                     @RequestBody CommentDto comment) {
        return commentService.createdComment(userId, itemId, comment);
    }
}