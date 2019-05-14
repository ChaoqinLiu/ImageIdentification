package com.example.sqlbrite.model;

import com.google.gson.annotations.SerializedName;

public class TrainTicketResult {

    public TrainTicketResultWordsResult words_result;

    public class TrainTicketResultWordsResult {

        private String starting_station;
        private String destination_station;
        private String seat_category;
        private String ticket_rates;
        private String ticket_num;
        private String train_num;

        @SerializedName("name")
        private String passenger_name;

        @SerializedName("date")
        private String departure_date;

        public void setStartingStation(String starting_station) {
            this.starting_station = starting_station;
        }

        public String getStartingStation() {
            return starting_station;
        }

        public void setDestinationStation(String destination_station) {
            this.destination_station = destination_station;
        }

        public String getDestinationstation() {
            return destination_station;
        }

        public void setPassengerName(String name) {
            this.passenger_name = name;
        }

        public String getPassengerName() {
            return passenger_name;
        }

        public void setSeatCategory(String seat_category) {
            this.seat_category = seat_category;
        }

        public String getSeatCategory() {
            return seat_category;
        }

        public void setDepartureDate(String date) {
            this.departure_date = date;
        }

        public String getDepartureDate() {
            return departure_date;
        }

        public void setTicketRates(String ticket_rates) {
            this.ticket_rates = ticket_rates;
        }

        public String getTicketRates() {
            return ticket_rates;
        }

        public void setTicketNum(String ticket_num) {
            this.ticket_num = ticket_num;
        }

        public String getTicketNum() {
            return ticket_num;
        }

        public void setTrainNum(String train_num) {
            this.train_num = train_num;
        }

        public String getTrainNum() {
            return train_num;
        }
    }

}
