package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<Long, Item>();

    @Override
    public ItemDto add(ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        Long id = getId();
        item.setId(id);
        items.put(id, item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Optional<Item> getItem(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public ItemDto update(ItemDto itemDto) {
        Item newItem = ItemMapper.toItem(itemDto);
        if (newItem.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        Long id = itemDto.getId();
        Item oldItem = getItem(id)
                .orElseThrow(() -> new ValidationException("Вещь с указанным id не найдена"));
        items.remove(id);
        if (newItem.getName() != null) {
            oldItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            oldItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            oldItem.setAvailable(newItem.getAvailable());
        }
        if (newItem.getOwner() != null) {
            oldItem.setOwner(newItem.getOwner());
        }
        if (newItem.getRequest() != null) {
            oldItem.setRequest(newItem.getRequest());
        }
        items.put(id, oldItem);
        return ItemMapper.toItemDto(oldItem);
    }

    @Override
    public List<ItemDto> getAllUsersItems(User user) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(user))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getRequiredItems(String request) {
        if (request.isBlank()) {
            return new ArrayList<ItemDto>();
        }
        return items.values().stream()
                .filter(item -> item.getAvailable() == true &&
                        (item.getName().toLowerCase().contains(request.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(request.toLowerCase())))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private Long getId() {
        long lastId = items.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);
        return lastId + 1L;
    }
}
