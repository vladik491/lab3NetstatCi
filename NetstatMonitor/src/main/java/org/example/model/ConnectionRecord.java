package org.example.model;

/**
 * модель данных, представляющая одну запись о сетевом соединении,
 * она хранит информацию о протоколе, адресах, статусе и PID процесса
 */
public class ConnectionRecord {
    private final String protocol;
    private final String localAddress;
    private final String foreignAddress;
    private final String state;
    private final String pidAndProgram;

    /**
     * конструктор записи соединения.
     *
     * @param protocol протокол (TCP/UDP)
     * @param localAddress локальный адрес и порт
     * @param foreignAddress внешний адрес и порт
     * @param state состояние соединения (LISTEN, ESTABLISHED и т.д.)
     * @param pidAndProgram PID процесса или имя программы
     */
    public ConnectionRecord(String protocol, String localAddress, String foreignAddress, String state, String pidAndProgram) {
        this.protocol = protocol;
        this.localAddress = localAddress;
        this.foreignAddress = foreignAddress;
        this.state = state;
        this.pidAndProgram = pidAndProgram;
    }

    public String getLocalAddress() { return localAddress; }
    public String getForeignAddress() { return foreignAddress; }
    public String getState() { return state; }
    public String getPidAndProgram() { return pidAndProgram; }

    @Override
    public String toString() {
        return "PID: " + pidAndProgram + " | Proto: " + protocol + " | Local: " + localAddress + " | State: " + state;
    }
}