package pl.put.poznan.pz.doggo.infrastructure.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UserCalendarIdEmptyException :
        RuntimeException("Calendar event id is empty.")