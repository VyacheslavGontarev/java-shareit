package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    ItemDto add(ItemDto itemDto);

    Optional<Item> getItem(Long id);

    ItemDto update(ItemDto itemDto);

    List<ItemDto> getAllUsersItems(User user);

    List<ItemDto> getRequiredItems(String request);
}
