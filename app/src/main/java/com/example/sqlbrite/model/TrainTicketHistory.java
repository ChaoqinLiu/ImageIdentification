package com.example.sqlbrite.model;

import java.util.List;


public class TrainTicketHistory {

    public List<TrainTicketHistoryArray> result;

    public class TrainTicketHistoryArray {

        public int id;
        public String date;
        public String name;
        public byte[] bytes;

        public TrainTicketHistoryArray(int id,String date,String name,byte[] bytes) {
            this.id = id;
            this.date = date;
            this.name = name;
            this.bytes = bytes;
        }
    }
}
