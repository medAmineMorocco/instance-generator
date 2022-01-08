package com.plugin.actions;

import com.github.javafaker.Faker;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class GenerateInstanceAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Faker faker = new Faker();
    }
}
