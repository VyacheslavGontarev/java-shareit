package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody @Valid BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@PathVariable("bookingId") Long bookingId,
                                             @RequestParam("approved") Boolean approved,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @PatchMapping("/{bookingId}/canceled")
    public BookingResponseDto approveBooking(@PathVariable("bookingId") Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.canceledBooking(userId, bookingId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@PathVariable("bookingId") Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping()
    public List<BookingResponseDto> getBookingsByState(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingByState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsAllItemsByState(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingsAllItemsByState(state, userId);
    }
}