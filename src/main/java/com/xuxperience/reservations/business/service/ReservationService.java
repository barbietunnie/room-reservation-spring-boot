package com.xuxperience.reservations.business.service;

import com.xuxperience.reservations.business.domain.RoomReservation;
import com.xuxperience.reservations.data.entity.Guest;
import com.xuxperience.reservations.data.entity.Reservation;
import com.xuxperience.reservations.data.entity.Room;
import com.xuxperience.reservations.data.repository.GuestRepository;
import com.xuxperience.reservations.data.repository.ReservationRepository;
import com.xuxperience.reservations.data.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReservationService {
    private RoomRepository roomRepository;
    private GuestRepository guestRepository;
    private ReservationRepository reservationRepository;

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ReservationService(RoomRepository roomRepository,
                              GuestRepository guestRepository,
                              ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RoomReservation> getRoomReservationsForDate(String dateStr) {
        Date date = this.createDateFromDateString(dateStr);

        Iterable<Room> rooms = this.roomRepository.findAll();
        Map<Long, RoomReservation> roomReservationMap = new HashMap<>();
        rooms.forEach(room -> {
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomId(room.getId());
            roomReservation.setRoomName(room.getName());
            roomReservation.setRoomNumber(room.getNumber());
            roomReservationMap.put(room.getId(), roomReservation);
        });

        Iterable<Reservation> reservations = this.reservationRepository.findByDate(
                                                    new java.sql.Date(date.getTime()));
        if(null != reservations) {
            reservations.forEach(reservation -> {
                Guest guest = this.guestRepository.findOne(reservation.getGuestId());
                if(null != guest) {
                    RoomReservation roomReservation = roomReservationMap.get(reservation.getId());
                    roomReservation.setDate(date);
                    roomReservation.setFirstName(guest.getFirstName());
                    roomReservation.setLastName(guest.getLastName());
                    roomReservation.setGuestId(guest.getId());
                }
            });
        }

        List<RoomReservation> roomReservations = new ArrayList<>();
        for (Long roomId :
                roomReservationMap.keySet()) {
            roomReservations.add(roomReservationMap.get(roomId));
        }

        return roomReservations;
    }

    private Date createDateFromDateString(String dateStr) {
        Date date = null;
        if(null != dateStr) {
            try {
                date = DATE_FORMAT.parse(dateStr);
            } catch (ParseException e) {
                date = new Date();
            }
        } else {
            date = new Date();
        }

        return date;
    }
}
