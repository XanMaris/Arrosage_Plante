package com.polytech.arrosageplante.esp32.communication.websocket.input.command;

import com.polytech.arrosageplante.esp32.communication.websocket.input.InputAction;

public sealed abstract class InputCommand permits InputArrosageKo, InputArrosageOk, InputPlantDetailsCommand, InputSyncCommand {
    public abstract InputAction getAction();
}
