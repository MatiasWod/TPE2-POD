package ar.edu.itba.pod.utils;

import ar.edu.itba.pod.data.Infraction;
import ar.edu.itba.pod.data.Ticket;

import java.util.UUID;

public enum CityCSVDatasource {
    CHI{
        @Override
        public Ticket ticketFromCSV(String[] csvLine) {
            //TODO verificar que la patente se guarda correctamente
            return new Ticket(UUID.nameUUIDFromBytes(csvLine[1].getBytes()).toString(),csvLine[2],csvLine[5],Double.parseDouble(csvLine[4]),csvLine[3]);
        }

        @Override
        public Infraction infractionFromCSV(String[] csvLine) {
            return new Infraction(csvLine[0],csvLine[1]);
        }
    },
    NYC{
        @Override
        public Ticket ticketFromCSV(String[] csvLine) {
            return new Ticket(csvLine[0],csvLine[2],csvLine[4],Double.parseDouble(csvLine[3]),csvLine[5]);
        }

        @Override
        public Infraction infractionFromCSV(String[] csvLine) {
            return new Infraction(csvLine[0],csvLine[1]);
        }
    };

    public abstract Ticket ticketFromCSV(final String[] csvLine);
    public abstract Infraction infractionFromCSV(final String[] csvLine);
}
