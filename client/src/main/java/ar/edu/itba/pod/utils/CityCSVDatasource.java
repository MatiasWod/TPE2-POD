package ar.edu.itba.pod.utils;

import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.data.Ticket;

import java.util.UUID;

public enum CityCSVDatasource {
    CHI{
        @Override
        public Ticket ticketFromCSV(String[] csvLine) {
            return new Ticket(UUID.fromString(csvLine[1]).toString(),Integer.parseInt(csvLine[2]),csvLine[5],Integer.parseInt(csvLine[4]));
        }

        @Override
        public Infraction infractionFromCSV(String[] csvLine) {
            return new Infraction(Integer.parseInt(csvLine[0]),csvLine[1]);
        }
    },
    NYC{
        @Override
        public Ticket ticketFromCSV(String[] csvLine) {
            return new Ticket(csvLine[0],Integer.parseInt(csvLine[2]),csvLine[4],Integer.parseInt(csvLine[3]));
        }

        @Override
        public Infraction infractionFromCSV(String[] csvLine) {
            return new Infraction(Integer.parseInt(csvLine[0]),csvLine[1]);
        }
    };

    public abstract Ticket ticketFromCSV(final String[] csvLine);
    public abstract Infraction infractionFromCSV(final String[] csvLine);
}
