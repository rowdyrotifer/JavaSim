package com.marklalor.javasim.content.interfacee;

import javafx.scene.Node;

public interface Content<T extends Node>
{
    OutputMedium<T> getOutput();
}
