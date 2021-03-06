package pl.put.poznan.pz.doggo.infrastructure.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserCalendarEventNotFoundException(calendarEventId: UUID, userId: UUID) :
        RuntimeException("Calendar event with id: $calendarEventId not exists for user with id: $userId.")