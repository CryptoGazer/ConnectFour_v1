package org.example.game.domain;

public enum Player {
    X,
    O;

    public Player next() {
        return this == X ? O : X;
    }
}