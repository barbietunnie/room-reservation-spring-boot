package com.xuxperience.reservations.web.application;

import com.xuxperience.reservations.business.domain.RoomReservation;
import com.xuxperience.reservations.business.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @RequestMapping(method = RequestMethod.GET)
    public String getReservations(@RequestParam(name = "date", required = false) String dateStr, Model model) {
        List<RoomReservation> roomReservationList = this.reservationService.getRoomReservationsForDate(dateStr);
        model.addAttribute("roomReservations", roomReservationList);

        return "reservations";
    }
}
