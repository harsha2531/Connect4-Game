package org.example.service;

public interface BoardUi {
    void update(int col, boolean isHuman);

    void notifyWinner(Winner winner);
}
