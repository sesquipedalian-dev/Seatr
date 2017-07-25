/**
 * Copyright 2017 sesquipedalian.dev@gmail.com

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sesquipedalian_dev.seatr.data.enums;

import com.sesquipedalian_dev.seatr.data.Seat;
import com.sesquipedalian_dev.seatr.data.TableSeatsGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * TableShape represents the shape of a table at an event
 */
public enum TableShape implements TableSeatsGenerator {
    CIRCLE((position, seats) -> {
        List<Seat.SeatRelation> retVal = new ArrayList<>();

        // wrap around to other end of circle table when we get to
        int leftNeighbor  = ((position - 1) <  0           ) ? seats.size() - 1 : position - 1;
        int rightNeighbor = ((position + 1) >= seats.size()) ? 0                : position + 1;

        retVal.add(new Seat.SeatRelation(seats.get(leftNeighbor), SeatRelationType.NEXT));
        retVal.add(new Seat.SeatRelation(seats.get(rightNeighbor), SeatRelationType.NEXT));

        return retVal;
    }),
    RECTANGLE((position, seats) -> {
        List<Seat.SeatRelation> retVal = new ArrayList<>();

        int totalSeats = seats.size();

        if(totalSeats % 2 != 0) {
            throw new IllegalArgumentException("rectangle must have even # of seats");
        } else if (totalSeats < 4) {
            throw new IllegalArgumentException("rectangle with end must have at least 4 seats");
        }

        int seatsPerSide = (totalSeats - 2) / 2;

        if(position == 0) { // left edge top
            retVal.add(new Seat.SeatRelation(seats.get(1), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(totalSeats - 1), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(totalSeats - 2), SeatRelationType.ACROSS));
        } else if (position == (totalSeats - 1)) { // left side
            retVal.add(new Seat.SeatRelation(seats.get(0), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(position - 1), SeatRelationType.NEXT));
        } else if (position == seatsPerSide) { // right side
            retVal.add(new Seat.SeatRelation(seats.get(position - 1), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(position + 1), SeatRelationType.NEXT));
        } else {
            retVal.add(new Seat.SeatRelation(seats.get(position - 1), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(position + 1), SeatRelationType.NEXT));
            int across = totalSeats - position - 2;
            retVal.add(new Seat.SeatRelation(seats.get(across), SeatRelationType.ACROSS));
        }

        return retVal;
    }),
    RECTANGLE_ONE_SIDED((position, seats) -> {
        List<Seat.SeatRelation> retVal = new ArrayList<>();

        if(position == 0) { // handle wraparound
            retVal.add(new Seat.SeatRelation(seats.get(1), SeatRelationType.NEXT));
        } else if (position == (seats.size() - 1)) { // handle wraparound
            retVal.add(new Seat.SeatRelation(seats.get(position - 1), SeatRelationType.NEXT));
        } else {
            retVal.add(new Seat.SeatRelation(seats.get(position - 1), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(position + 1), SeatRelationType.NEXT));
        }

        return retVal;
    }),
    RECTANGLE_NO_END((position, seats) -> {
        List<Seat.SeatRelation> retVal = new ArrayList<>();

        int totalSeats = seats.size();

        if(totalSeats % 2 != 0) {
            throw new IllegalArgumentException("no-end rectangle must have even # of seats");
        }

        int seatsPerSide = totalSeats / 2;

        if(position == 0) { // left edge top
            retVal.add(new Seat.SeatRelation(seats.get(1), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(totalSeats), SeatRelationType.ACROSS));
        } else if (position == (totalSeats - 1)) { // left edge bottom
            retVal.add(new Seat.SeatRelation(seats.get(0), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(position - 1), SeatRelationType.ACROSS));
        } else if (position == seatsPerSide - 1) { // right edge top
            retVal.add(new Seat.SeatRelation(seats.get(seatsPerSide), SeatRelationType.ACROSS));
            retVal.add(new Seat.SeatRelation(seats.get(position - 1), SeatRelationType.NEXT));
        } else if (position == seatsPerSide) { // right edge bottom
            retVal.add(new Seat.SeatRelation(seats.get(position + 1), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(seatsPerSide - 1), SeatRelationType.ACROSS));
        } else {
            retVal.add(new Seat.SeatRelation(seats.get(position - 1), SeatRelationType.NEXT));
            retVal.add(new Seat.SeatRelation(seats.get(position + 1), SeatRelationType.NEXT));
            int across = totalSeats - position - 1;
            retVal.add(new Seat.SeatRelation(seats.get(across), SeatRelationType.ACROSS));
        }

        return retVal;
    })
    ;
//
//    private static class Config {
//        // weighted relations - across not as important as 'next to'
//        private static final Double ACROSS = .5;
//        private static final Double NEXT = 1.0;
//        private static final Double AT_TABLE = .1;
//    }

    final TableSeatsGenerator generator;
    TableShape(TableSeatsGenerator generator) {
        this.generator = generator;
    }

    @Override
    public List<Seat> generateSeats(int count) {
        return generator.generateSeats(count);
    }

    @Override
    public List<Seat.SeatRelation> getRelationsFor(int position, List<Seat> seats) {
        List<Seat.SeatRelation> retVal = generator.getRelationsFor(position, seats);
        for(Seat s: seats) {
            if(s == seats.get(position)) {
                // skip
            } else {
                // find if seat already has a defined relation
                boolean found = false;
                for (Seat.SeatRelation r : retVal) {
                    if (r.getOtherSeat() == s) {
                        found = true;
                        break;
                    }
                }

                if(!found) {
                    retVal.add(new Seat.SeatRelation(s, SeatRelationType.AT_TABLE));
                }
            }
        }
        return retVal;
    }
}



