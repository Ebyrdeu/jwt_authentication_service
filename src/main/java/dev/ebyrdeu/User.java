package dev.ebyrdeu;

import java.time.LocalDateTime;
import java.util.UUID;

public record User(UUID uuid, LocalDateTime localDateTime) {
}