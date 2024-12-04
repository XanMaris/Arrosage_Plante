package com.polytech.arrosageplante.esp32.communication.websocket.input.command;

import com.polytech.arrosageplante.esp32.communication.websocket.input.InputAction;

public final class InputSyncCommand extends InputCommand {
    private final String privateCode;

    public InputSyncCommand(String privateCode) {
        this.privateCode = privateCode;
    }

    @Override
    public InputAction getAction() {
        return InputAction.SYNC;
    }

    public String getPrivateCode() {
        return privateCode;
    }
}
