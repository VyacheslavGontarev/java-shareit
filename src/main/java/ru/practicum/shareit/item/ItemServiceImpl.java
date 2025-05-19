package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        itemDto.setOwner(findOwner(userId));
        return itemRepository.add(itemDto);
    }

    @Override
    public ItemDto getItem(Long id) {
        return ItemMapper.toItemDto(itemRepository.getItem(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещь с таким id не найдена")));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item item = itemRepository.getItem(itemId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещь с таким id не найдена"));
        User user = findOwner(userId);
        if (!user.equals(item.getOwner())) {
            throw new ValidationException("Указанный пользователь не имеет прав на редактирование этой вещи");
        }
        itemDto.setId(itemId);
        itemDto.setOwner(user);
        return itemRepository.update(itemDto);
    }

    @Override
    public List<ItemDto> getAllUsersItems(Long userId) {
        return itemRepository.getAllUsersItems(findOwner(userId));
    }

    @Override
    public List<ItemDto> getAllRequestedItems(String request) {
        return itemRepository.getRequiredItems(request);
    }

    private User findOwner(Long userId) {
        return userRepository.findUser(userId)
                .orElseThrow(() -> new
                        ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден"));
    }
}
